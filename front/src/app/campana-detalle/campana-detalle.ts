import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, NgZone, inject } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

interface Campana {
  idCampania: number;
  idCreador: number;
  titulo: string;
  descripcionLarga: string;
  montoObjetivo: number;
  montoActual: number;
  fechaFin: string;
  estado: string;
  nombreCreador: string;
  nombreCategoria: string;
  categorias: string[];
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
  likes: number;
}

interface Actualizacion {
  id: number;
  titulo: string;
  descripcion: string;
  fechaPublicacion: string;
  nombreAutor: string;
}

@Component({
  selector: 'app-campana-detalle',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './campana-detalle.html',
  styleUrls: ['./campana-detalle.css']
})
export class CampanaDetalle implements OnInit {

  private sanitizer = inject(DomSanitizer);

  campana: Campana | null = null;
  cargando = false;
  error = '';

  // Comentarios
  comentarios: Comentario[] = [];
  nuevoComentario = '';
  enviandoComentario = false;

  // Actualizaciones
  actualizaciones: Actualizacion[] = [];
  mostrarFormUpdate = false;
  nuevaUpdateTitulo = '';
  nuevaUpdateDesc = '';
  enviandoUpdate = false;
  mensajeUpdate = '';

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
          this.cargarActualizaciones(Number(id));
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

  esDuenoCampana(): boolean {
    if (!this.campana || !this.authService.estaLogueado()) return false;
    if (this.authService.esAdmin()) return true;
    return this.authService.usuario?.id === this.campana.idCreador;
  }

  get descripcionHtml(): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(this.markdownToHtml(this.campana?.descripcionLarga ?? ''));
  }

  private markdownToHtml(md: string): string {
    if (!md) return '';
    let html = md
      .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
      .replace(/^### (.+)$/gm, '<h3 class="md-h3">$1</h3>')
      .replace(/^## (.+)$/gm, '<h2 class="md-h2">$1</h2>')
      .replace(/^# (.+)$/gm, '<h1 class="md-h1">$1</h1>')
      .replace(/\*\*\*(.+?)\*\*\*/g, '<strong><em>$1</em></strong>')
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      .replace(/\*(.+?)\*/g, '<em>$1</em>')
      .replace(/^---$/gm, '<hr class="md-hr">')
      .replace(/^- (.+)$/gm, '<li class="md-li">$1</li>')
      .replace(/^\d+\. (.+)$/gm, '<li class="md-li md-li-num">$1</li>')
      .replace(/\n\n/g, '</p><p class="md-p">')
      .replace(/\n/g, '<br>');
    html = html
      .replace(/(<li class="md-li">.*?<\/li>(\s*<br>)*)+/g, m => `<ul class="md-ul">${m}</ul>`)
      .replace(/(<li class="md-li md-li-num">.*?<\/li>(\s*<br>)*)+/g, m => `<ol class="md-ol">${m}</ol>`);
    return `<p class="md-p">${html}</p>`;
  }

  // ── COMENTARIOS ─────────────────────────────────────────────────────
  cargarComentarios(id: number): void {
    this.ngZone.run(() => {
      this.http.get<Comentario[]>(`http://localhost:8080/api/campanias/${id}/comentarios`)
        .subscribe({ next: (d) => { this.comentarios = d; this.cdr.detectChanges(); }, error: () => {} });
    });
  }

  enviarComentario(): void {
    if (!this.nuevoComentario.trim() || !this.campana) return;
    this.enviandoComentario = true;
    const headers = { Authorization: `Bearer ${this.authService.getToken()}` };
    this.http.post<Comentario>(
      `http://localhost:8080/api/campanias/${this.campana.idCampania}/comentarios`,
      { contenido: this.nuevoComentario.trim() }, { headers }
    ).subscribe({
      next: (c) => { this.comentarios = [c, ...this.comentarios]; this.nuevoComentario = ''; this.enviandoComentario = false; this.cdr.detectChanges(); },
      error: () => { this.enviandoComentario = false; this.cdr.detectChanges(); }
    });
  }

  darLike(comentario: Comentario): void {
    if (!this.authService.estaLogueado() || !this.campana) return;
    const headers = { Authorization: `Bearer ${this.authService.getToken()}` };
    this.http.post<{id: number, likes: number}>(
      `http://localhost:8080/api/campanias/${this.campana.idCampania}/comentarios/${comentario.id}/like`,
      {}, { headers }
    ).subscribe({
      next: (res) => { comentario.likes = res.likes; this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  // ── ACTUALIZACIONES ─────────────────────────────────────────────────
  cargarActualizaciones(id: number): void {
    this.ngZone.run(() => {
      this.http.get<Actualizacion[]>(`http://localhost:8080/api/campanias/${id}/updates`)
        .subscribe({ next: (d) => { this.actualizaciones = d; this.cdr.detectChanges(); }, error: () => {} });
    });
  }

  publicarActualizacion(): void {
    if (!this.nuevaUpdateTitulo.trim() || !this.nuevaUpdateDesc.trim() || !this.campana) return;
    this.enviandoUpdate = true;
    const headers = { Authorization: `Bearer ${this.authService.getToken()}` };
    this.http.post<Actualizacion>(
      `http://localhost:8080/api/campanias/${this.campana.idCampania}/updates`,
      { titulo: this.nuevaUpdateTitulo.trim(), descripcion: this.nuevaUpdateDesc.trim() }, { headers }
    ).subscribe({
      next: (u) => {
        this.actualizaciones = [u, ...this.actualizaciones];
        this.nuevaUpdateTitulo = ''; this.nuevaUpdateDesc = '';
        this.mostrarFormUpdate = false; this.enviandoUpdate = false;
        this.mensajeUpdate = '✅ Actualización publicada';
        setTimeout(() => { this.mensajeUpdate = ''; this.cdr.detectChanges(); }, 3000);
        this.cdr.detectChanges();
      },
      error: () => { this.enviandoUpdate = false; this.cdr.detectChanges(); }
    });
  }

  borrarActualizacion(id: number): void {
    if (!confirm('¿Eliminar esta actualización?') || !this.campana) return;
    const headers = { Authorization: `Bearer ${this.authService.getToken()}` };
    this.http.delete(
      `http://localhost:8080/api/campanias/${this.campana.idCampania}/updates/${id}`,
      { headers, responseType: 'text' }
    ).subscribe({
      next: () => { this.actualizaciones = this.actualizaciones.filter(u => u.id !== id); this.cdr.detectChanges(); },
      error: () => {}
    });
  }

  // ── COMPARTIR ────────────────────────────────────────────────────────
  get urlActual(): string {
    return isPlatformBrowser(this.platformId) ? window.location.href : '';
  }

  copiarUrl(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    navigator.clipboard.writeText(this.urlActual).then(() => {
      this.urlCopiada = true; this.cdr.detectChanges();
      setTimeout(() => { this.urlCopiada = false; this.cdr.detectChanges(); }, 2000);
    });
  }

  compartirEn(red: string): void {
    if (!isPlatformBrowser(this.platformId)) return;
    const url = encodeURIComponent(this.urlActual);
    const titulo = encodeURIComponent(this.campana?.titulo ?? '');
    const urls: Record<string, string> = {
      twitter:  `https://twitter.com/intent/tweet?text=${titulo}&url=${url}`,
      facebook: `https://www.facebook.com/sharer/sharer.php?u=${url}`,
      whatsapp: `https://api.whatsapp.com/send?text=${titulo}%20${url}`,
      telegram: `https://t.me/share/url?url=${url}&text=${titulo}`,
      linkedin: `https://www.linkedin.com/sharing/share-offsite/?url=${url}`
    };
    if (urls[red]) window.open(urls[red], '_blank', 'width=600,height=400');
  }
}