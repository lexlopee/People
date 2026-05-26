import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';

interface Campana {
  idCampania: number;
  titulo: string;
  descripcionLarga: string;
  montoObjetivo: number;
  montoActual: number;
  fechaFin: string;
  fechaInicio: string;
  estado: string;
  nombreCreador: string;
  nombreCategoria: string;
  porcentajeCompletado: number;
  diasRestantes: number;
  imagenUrl: string | null;
}

interface Comentario {
  id: number;
  contenido: string;
  fecha: string;
  nombreUsuario: string;
  inicial: string;
}

@Component({
  selector: 'app-campana-detalle',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './campana-detalle.html',
  styleUrls: ['./campana-detalle.css']
})
export class CampanaDetalle implements OnInit {

  campana: Campana | null = null;
  cargando = false;
  error = '';

  // Comentarios
  comentarios: Comentario[] = [];
  nuevoComentario = '';
  enviandoComentario = false;

  // Compartir
  mostrarCompartir = false;
  urlCopiada = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
    public authService: AuthService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    const id = this.route.snapshot.paramMap.get('id');
    this.cargando = true;
    this.cdr.detectChanges();

    this.ngZone.run(() => {
      this.http.get<Campana>(`http://localhost:8080/api/campanias/${id}`).subscribe({
        next: (data) => {
          this.campana = data;
          this.cargando = false;
          this.cdr.detectChanges();
          this.cargarComentarios(Number(id));
        },
        error: () => {
          this.error = 'No se pudo cargar la campaña';
          this.cargando = false;
          this.cdr.detectChanges();
        }
      });
    });
  }

  getImagenUrl(): string {
    return this.campana?.imagenUrl ? `http://localhost:8080${this.campana.imagenUrl}` : '';
  }

  // ── COMENTARIOS ────────────────────────────────────────────────────────
  cargarComentarios(idCampania: number): void {
    this.ngZone.run(() => {
      this.http.get<Comentario[]>(`http://localhost:8080/api/campanias/${idCampania}/comentarios`)
        .subscribe({
          next: (data) => { this.comentarios = data; this.cdr.detectChanges(); },
          error: () => {}
        });
    });
  }

  enviarComentario(): void {
    if (!this.nuevoComentario.trim() || !this.campana) return;
    this.enviandoComentario = true;

    const headers = { Authorization: `Bearer ${this.authService.getToken()}` };
    this.http.post<Comentario>(
      `http://localhost:8080/api/campanias/${this.campana.idCampania}/comentarios`,
      { contenido: this.nuevoComentario.trim() },
      { headers }
    ).subscribe({
      next: (c) => {
        this.comentarios = [c, ...this.comentarios];
        this.nuevoComentario = '';
        this.enviandoComentario = false;
        this.cdr.detectChanges();
      },
      error: () => { this.enviandoComentario = false; this.cdr.detectChanges(); }
    });
  }

  // ── COMPARTIR ──────────────────────────────────────────────────────────
  get urlActual(): string {
    return isPlatformBrowser(this.platformId) ? window.location.href : '';
  }

  copiarUrl(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    navigator.clipboard.writeText(this.urlActual).then(() => {
      this.urlCopiada = true;
      this.cdr.detectChanges();
      setTimeout(() => { this.urlCopiada = false; this.cdr.detectChanges(); }, 2000);
    });
  }

  compartirEn(red: string): void {
    if (!isPlatformBrowser(this.platformId)) return;
    const url = encodeURIComponent(this.urlActual);
    const titulo = encodeURIComponent(this.campana?.titulo ?? '');
    const urls: Record<string, string> = {
      twitter:   `https://twitter.com/intent/tweet?text=${titulo}&url=${url}`,
      facebook:  `https://www.facebook.com/sharer/sharer.php?u=${url}`,
      whatsapp:  `https://api.whatsapp.com/send?text=${titulo}%20${url}`,
      telegram:  `https://t.me/share/url?url=${url}&text=${titulo}`,
      linkedin:  `https://www.linkedin.com/sharing/share-offsite/?url=${url}`
    };
    if (urls[red]) window.open(urls[red], '_blank', 'width=600,height=400');
  }
}