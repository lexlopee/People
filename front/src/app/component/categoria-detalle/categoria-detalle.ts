import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

interface CategoriaItem { idCategoria: number; nombre: string; }

interface Campana {
  idCampania: number;
  titulo: string;
  montoObjetivo: number;
  montoActual: number;
  nombreCreador: string;
  nombreCategoria: string;
  categorias: string[];
  porcentajeCompletado: number;
  diasRestantes: number;
  estado: string;
  imagenUrl: string | null;
}

@Component({
  selector: 'app-categoria-detalle',
  standalone: true,
  imports: [CommonModule, TranslateModule, RouterLink],
  templateUrl: './categoria-detalle.html',
  styleUrls: ['./categoria-detalle.css']
})
export class CategoriaDetalle implements OnInit {

  private slugMap: Record<string, string> = {
    'medicina': 'CATEGORIES.MEDICINE', 'in-memoriam': 'CATEGORIES.IN_MEMORIAM',
    'emergencia': 'CATEGORIES.EMERGENCY', 'sin-animo-de-lucro': 'CATEGORIES.NONPROFIT',
    'educacion': 'CATEGORIES.EDUCATION', 'animales': 'CATEGORIES.ANIMALS',
    'medioambiente': 'CATEGORIES.ENVIRONMENT', 'empresa': 'CATEGORIES.BUSINESS',
    'comunidad': 'CATEGORIES.COMMUNITY', 'competicion': 'CATEGORIES.COMPETITION',
    'artes-creativas': 'CATEGORIES.CREATIVE_ARTS', 'evento': 'CATEGORIES.EVENT',
    'religion': 'CATEGORIES.RELIGION', 'familia': 'CATEGORIES.FAMILY',
    'deportes': 'CATEGORIES.SPORTS', 'viajes': 'CATEGORIES.TRAVEL',
    'voluntariado': 'CATEGORIES.VOLUNTEERING', 'deseos': 'CATEGORIES.WISHES'
  };

  private iconMap: Record<string, string> = {
    'medicina': '🏥', 'in-memoriam': '🕊️', 'emergencia': '🚨',
    'sin-animo-de-lucro': '🤝', 'educacion': '📚', 'animales': '🐾',
    'medioambiente': '🌿', 'empresa': '💼', 'comunidad': '🏘️',
    'competicion': '🏆', 'artes-creativas': '🎨', 'evento': '🎉',
    'religion': '🙏', 'familia': '👨‍👩‍👧', 'deportes': '⚽',
    'viajes': '✈️', 'voluntariado': '💚', 'deseos': '⭐'
  };

  slug = '';
  translateKey = '';
  icon = '';
  notFound = false;
  campanas: Campana[] = [];
  cargandoCampanas = false;
  idCategoriaActual: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    public authService: AuthService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.slug = params.get('slug') ?? '';
      this.translateKey = this.slugMap[this.slug] ?? '';
      this.icon = this.iconMap[this.slug] ?? '📁';
      this.notFound = !this.translateKey;

      if (!this.notFound && isPlatformBrowser(this.platformId)) {
        // Primero buscamos el idCategoria por nombre desde el backend
        this.buscarIdCategoria();
      }
    });
  }

  // Obtiene todas las categorías y busca el id que corresponde al slug actual
  buscarIdCategoria(): void {
    this.cargandoCampanas = true;
    this.cdr.detectChanges();

    this.ngZone.run(() => {
      this.http.get<CategoriaItem[]>('http://localhost:8080/api/categorias').subscribe({
        next: (cats) => {
          // Busca la categoría cuyo slug coincide
          const cat = cats.find(c => this.toSlug(c.nombre) === this.slug);
          if (cat) {
            this.idCategoriaActual = cat.idCategoria;
            this.cargarCampanas(cat.idCategoria);
          } else {
            this.cargandoCampanas = false;
            this.cdr.detectChanges();
          }
        },
        error: () => { this.cargandoCampanas = false; this.cdr.detectChanges(); }
      });
    });
  }

  // Llama al endpoint del backend que ya filtra por categoría en BD
  cargarCampanas(idCategoria: number): void {
    this.ngZone.run(() => {
      this.http.get<Campana[]>(`http://localhost:8080/api/campanias/categoria/${idCategoria}`).subscribe({
        next: (data) => {
          this.campanas = data;
          this.cargandoCampanas = false;
          this.cdr.detectChanges();
        },
        error: () => { this.cargandoCampanas = false; this.cdr.detectChanges(); }
      });
    });
  }

  // Convierte nombre a slug igual que en el backend
  private toSlug(nombre: string): string {
    return nombre.toLowerCase()
      .normalize('NFD').replace(/[\u0300-\u036f]/g, '')
      .replace(/\s+/g, '-');
  }

  irACrearCampana(): void {
    if (!this.authService.estaLogueado()) {
      this.router.navigate(['/login']);
    } else if (!this.authService.puedeCrearCampana()) {
      alert('Solo las creadoras pueden publicar campañas.');
    } else {
      this.router.navigate(['/crear-campana'], { queryParams: { categoriaId: this.idCategoriaActual } });
    }
  }
}