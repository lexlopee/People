import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

interface CategoriaItem {
  idCategoria: number;
  nombre: string;
  descripcion: string;
}

const ICONOS: Record<string, string> = {
  'medicina': '🏥', 'in-memoriam': '🕊️', 'emergencia': '🚨',
  'sin-animo-de-lucro': '🤝', 'educacion': '📚', 'animales': '🐾',
  'medioambiente': '🌿', 'empresa': '💼', 'comunidad': '🏘️',
  'competicion': '🏆', 'artes-creativas': '🎨', 'evento': '🎉',
  'religion': '🙏', 'familia': '👨‍👩‍👧', 'deportes': '⚽',
  'viajes': '✈️', 'voluntariado': '💚', 'deseos': '⭐'
};

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [CommonModule, TranslateModule, RouterLink, FormsModule],
  templateUrl: './categoria.html',
  styleUrls: ['./categoria.css']
})
export class Categoria implements OnInit {

  categorias: CategoriaItem[] = [];
  cargando = false;
  error = '';
  mostrarFormulario = false;
  nuevaNombre = '';
  nuevaDescripcion = '';
  guardando = false;
  mensajeExito = '';

  constructor(
    private http: HttpClient,
    public authService: AuthService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.cargarCategorias();
    }
  }

  cargarCategorias(): void {
    this.cargando = true;
    this.cdr.detectChanges();

    this.ngZone.run(() => {
      this.http.get<CategoriaItem[]>('http://localhost:8080/api/categorias').subscribe({
        next: (data) => {
          this.categorias = data;
          this.cargando = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.error = 'No se pudieron cargar las categorías';
          this.cargando = false;
          this.cdr.detectChanges();
        }
      });
    });
  }

  getIcono(nombre: string): string {
    const slug = nombre.toLowerCase()
      .normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\s+/g, '-');
    return ICONOS[slug] ?? '📁';
  }

  getSlug(nombre: string): string {
    return nombre.toLowerCase()
      .normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/\s+/g, '-');
  }

  crearCategoria(): void {
    if (!this.nuevaNombre.trim()) return;
    this.guardando = true;
    const token = this.authService.getToken();
    const headers: Record<string, string> = {};
    if (token) headers['Authorization'] = `Bearer ${token}`;

    this.ngZone.run(() => {
      this.http.post<CategoriaItem>(
        'http://localhost:8080/api/categorias',
        { nombre: this.nuevaNombre.trim(), descripcion: this.nuevaDescripcion.trim() },
        { headers }
      ).subscribe({
        next: (nueva) => {
          this.categorias.push(nueva);
          this.nuevaNombre = '';
          this.nuevaDescripcion = '';
          this.mostrarFormulario = false;
          this.guardando = false;
          this.mensajeExito = '¡Categoría creada!';
          this.cdr.detectChanges();
          setTimeout(() => { this.mensajeExito = ''; this.cdr.detectChanges(); }, 3000);
        },
        error: () => {
          this.guardando = false;
          this.error = 'Error al crear la categoría.';
          this.cdr.detectChanges();
        }
      });
    });
  }
}