import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

interface CategoriaItem {
  idCategoria: number;
  nombre: string;
}

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
  paso = 1;          // 1 = formulario  |  2 = previsualización
  guardando = false;
  errorMsg = '';

  // Fecha mínima para el datepicker: mañana
  get fechaMinima(): string {
    const d = new Date();
    d.setDate(d.getDate() + 1);
    return d.toISOString().split('T')[0];
  }

  // Porcentaje ficticio para la preview (siempre 0 al crear)
  get porcentajePreview(): number { return 0; }

  // Días restantes calculados desde la fecha fin elegida
  get diasPreview(): number {
    const fin = this.form.value.fechaFin;
    if (!fin) return 0;
    const diff = new Date(fin).getTime() - Date.now();
    return Math.max(0, Math.ceil(diff / 86400000));
  }

  get nombreCategoria(): string {
    const id = this.form.value.idCategoria;
    return this.categorias.find(c => c.idCategoria == id)?.nombre ?? '';
  }

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    public authService: AuthService
  ) {
    this.form = this.fb.group({
      titulo:          ['', [Validators.required, Validators.minLength(10)]],
      descripcionLarga:['', [Validators.required, Validators.minLength(50)]],
      montoObjetivo:   [null, [Validators.required, Validators.min(100)]],
      fechaFin:        ['', Validators.required],
      idCategoria:     [null, Validators.required],
      ubicacion:       ['España'],
    });
  }

  ngOnInit(): void {
    if (!this.authService.estaLogueado()) {
      this.router.navigate(['/login']);
      return;
    }
    this.http.get<CategoriaItem[]>('http://localhost:8080/api/categorias').subscribe({
      next: (data) => this.categorias = data,
      error: () => this.errorMsg = 'No se pudieron cargar las categorías'
    });
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

    const token = this.authService.getToken();
    const headers = { Authorization: `Bearer ${token}` };

    const payload = {
      titulo:           this.form.value.titulo,
      descripcionLarga: this.form.value.descripcionLarga,
      montoObjetivo:    this.form.value.montoObjetivo,
      fechaFin:         this.form.value.fechaFin,
      idCategoria:      this.form.value.idCategoria
    };

    this.http.post('http://localhost:8080/api/campanias/crear', payload, { headers, responseType: 'text' })
      .subscribe({
        next: () => {
          this.guardando = false;
          this.router.navigate(['/categoria']);
        },
        error: (err) => {
          this.guardando = false;
          this.errorMsg = err.error ?? 'Error al publicar la campaña. Inténtalo de nuevo.';
        }
      });
  }

  campoInvalido(campo: string): boolean {
    const c = this.form.get(campo);
    return !!(c?.invalid && c?.touched);
  }
}