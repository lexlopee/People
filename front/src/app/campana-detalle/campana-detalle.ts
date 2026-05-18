import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
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
  cargando = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const id = this.route.snapshot.paramMap.get('id');
      this.http.get<Campana>(`http://localhost:8080/api/campanias/${id}`).subscribe({
        next: (data) => { this.campana = data; this.cargando = false; },
        error: () => { this.error = 'No se pudo cargar la campaña'; this.cargando = false; }
      });
    }
  }
}