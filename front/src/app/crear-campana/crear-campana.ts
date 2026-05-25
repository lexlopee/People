import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

interface CategoriaItem { idCategoria: number; nombre: string; }

@Component({
  selector: 'app-crear-campana',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './crear-campana.html',
  styleUrls: ['./crear-campana.css']
})
export class CrearCampana implements OnInit {

  form: FormGroup;
  categorias: CategoriaItem[] = [];
  paso = 1;
  guardando = false;
  errorMsg = '';

  // Imagen
  imagenSeleccionada: File | null = null;
  imagenPreview: string | null = null;

  get fechaMinima(): string {
    const d = new Date(); d.setDate(d.getDate() + 1);
    return d.toISOString().split('T')[0];
  }

  get diasPreview(): number {
    const fin = this.form.value.fechaFin;
    if (!fin) return 0;
    return Math.max(0, Math.ceil((new Date(fin).getTime() - Date.now()) / 86400000));
  }

  get nombreCategoria(): string {
    const id = this.form.value.idCategoria;
    return this.categorias.find(c => c.idCategoria == id)?.nombre ?? '';
  }

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    public authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.form = this.fb.group({
      titulo:           ['', [Validators.required, Validators.minLength(10)]],
      descripcionLarga: ['', [Validators.required, Validators.minLength(50)]],
      montoObjetivo:    [null, [Validators.required, Validators.min(100)]],
      fechaFin:         ['', Validators.required],
      idCategoria:      [null, Validators.required],
      ubicacion:        ['España']
    });
  }

  ngOnInit(): void {
    if (!this.authService.estaLogueado()) { this.router.navigate(['/login']); return; }
    if (isPlatformBrowser(this.platformId)) {
      this.http.get<CategoriaItem[]>('http://localhost:8080/api/categorias').subscribe({
        next: (data) => this.categorias = data,
        error: () => this.errorMsg = 'No se pudieron cargar las categorías'
      });
    }
  }

  onImagenSeleccionada(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];

      // Validar tipo
      if (!file.type.startsWith('image/')) {
        this.errorMsg = 'Solo se permiten imágenes (JPG, PNG, etc.)';
        return;
      }
      // Validar tamaño (5MB)
      if (file.size > 5 * 1024 * 1024) {
        this.errorMsg = 'La imagen no puede superar 5MB';
        return;
      }

      this.imagenSeleccionada = file;
      this.errorMsg = '';

      // Mostrar preview
      const reader = new FileReader();
      reader.onload = (e) => this.imagenPreview = e.target?.result as string;
      reader.readAsDataURL(file);
    }
  }

  quitarImagen(): void {
    this.imagenSeleccionada = null;
    this.imagenPreview = null;
  }

  previsualizacion(): void {
    if (this.form.valid) this.paso = 2;
    else this.form.markAllAsTouched();
  }

  volver(): void { this.paso = 1; }

  publicar(): void {
    if (!this.form.valid) return;
    this.guardando = true;
    this.errorMsg = '';

    const headers = { Authorization: `Bearer ${this.authService.getToken()}` };
    const payload = {
      titulo:           this.form.value.titulo,
      descripcionLarga: this.form.value.descripcionLarga,
      montoObjetivo:    this.form.value.montoObjetivo,
      fechaFin:         this.form.value.fechaFin,
      idCategoria:      this.form.value.idCategoria
    };

    this.http.post<any>('http://localhost:8080/api/campanias/crear', payload, { headers })
      .subscribe({
        next: (campana) => {
          // Si hay imagen, subirla después de crear la campaña
          if (this.imagenSeleccionada) {
            this.subirImagen(campana.idCampania, headers);
          } else {
            this.guardando = false;
            this.router.navigate(['/campana', campana.idCampania]);
          }
        },
        error: (err) => {
          this.guardando = false;
          this.errorMsg = err.error ?? 'Error al publicar la campaña.';
        }
      });
  }

  private subirImagen(idCampania: number, headers: any): void {
    const formData = new FormData();
    formData.append('imagen', this.imagenSeleccionada!);

    this.http.post(`http://localhost:8080/api/campanias/${idCampania}/imagen`, formData, { headers, responseType: 'text' })
      .subscribe({
        next: () => {
          this.guardando = false;
          this.router.navigate(['/campana', idCampania]);
        },
        error: () => {
          // La campaña se creó bien, solo falló la imagen
          this.guardando = false;
          this.router.navigate(['/campana', idCampania]);
        }
      });
  }

  campoInvalido(campo: string): boolean {
    const c = this.form.get(campo);
    return !!(c?.invalid && c?.touched);
  }
}