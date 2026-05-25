import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface Campana {
  idCampania: number;
  titulo: string;
  descripcionLarga: string;
  montoObjetivo: number;
  montoActual: number;
  fechaFin: string;
  estado: string;
  nombreCreador: string;
  nombreCategoria: string;
  porcentajeCompletado: number;
  diasRestantes: number;
  imagenUrl: string | null;
}

@Component({
  selector: 'app-campana-detalle',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './campana-detalle.html',
  styleUrls: ['./campana-detalle.css']
})
export class CampanaDetalle implements OnInit {

  campana: Campana | null = null;
  cargando = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const id = this.route.snapshot.paramMap.get('id');
      this.cargando = true;
      this.cdr.detectChanges();

      this.ngZone.run(() => {
        this.http.get<Campana>(`http://localhost:8080/api/campanias/${id}`).subscribe({
          next: (data) => {
            this.campana = data;
            this.cargando = false;
            this.cdr.detectChanges();
          },
          error: () => {
            this.error = 'No se pudo cargar la campana';
            this.cargando = false;
            this.cdr.detectChanges();
          }
        });
      });
    }
  }

  // URL de la imagen: si tiene imagenUrl la usamos, si no el placeholder
  getImagenUrl(): string {
    if (this.campana?.imagenUrl) {
      return `http://localhost:8080${this.campana.imagenUrl}`;
    }
    return '';
  }
}