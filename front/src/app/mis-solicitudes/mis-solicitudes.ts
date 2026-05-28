import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

interface Solicitud {
  id: number;
  estado: string;
  titulo: string;
  descripcion: string;
  montoObjetivo: number;
  fechaFin: string;
  motivo: string;
  organizacion: string;
  motivoRechazo: string;
  idCampanaCreada: number;
  fechaSolicitud: string;
  fechaResolucion: string;
}

@Component({
  selector: 'app-mis-solicitudes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './mis-solicitudes.html',
  styleUrls: ['./mis-solicitudes.css']
})
export class MisSolicitudes implements OnInit {

  solicitudes: Solicitud[] = [];
  cargando = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    public authService: AuthService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    if (!this.authService.estaLogueado()) { this.router.navigate(['/login']); return; }
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.cdr.detectChanges();
    const headers = { Authorization: `Bearer ${this.authService.getToken()}` };
    this.ngZone.run(() => {
      this.http.get<Solicitud[]>('http://localhost:8080/api/solicitudes/mias', { headers })
        .subscribe({
          next: (d) => { this.solicitudes = d; this.cargando = false; this.cdr.detectChanges(); },
          error: () => { this.cargando = false; this.cdr.detectChanges(); }
        });
    });
  }

  estadoTexto(estado: string): string {
    switch (estado) {
      case 'PENDIENTE': return 'En revisión';
      case 'APROBADA': return 'Aprobada y publicada';
      case 'RECHAZADA': return 'No aprobada';
      default: return estado;
    }
  }

  estadoEmoji(estado: string): string {
    switch (estado) {
      case 'PENDIENTE': return '⏳';
      case 'APROBADA': return '✅';
      case 'RECHAZADA': return '❌';
      default: return '📄';
    }
  }
}