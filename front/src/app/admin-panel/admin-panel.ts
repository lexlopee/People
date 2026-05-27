import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';

interface Stats {
  totalCampanias: number;
  totalUsuarios: number;
  totalCreadores: number;
  totalDonantes: number;
  totalRecaudado: number;
}

interface CampaniaAdmin {
  idCampania: number;
  titulo: string;
  nombreCreador: string;
  nombreCategoria: string;
  montoObjetivo: number;
  montoActual: number;
  estado: string;
  porcentajeCompletado: number;
  diasRestantes: number;
}

interface UsuarioAdmin {
  idUsuario: number;
  nombre: string;
  email: string;
  rol: string;
  fechaAlta: string;
  activo: boolean;
}

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './admin-panel.html',
  styleUrls: ['./admin-panel.css']
})
export class AdminPanel implements OnInit {

  seccionActiva: 'dashboard' | 'campanias' | 'usuarios' = 'dashboard';
  stats: Stats | null = null;
  campanias: CampaniaAdmin[] = [];
  usuarios: UsuarioAdmin[] = [];
  mensajeExito = '';
  mensajeError = '';
  filtroCampania = '';
  filtroUsuario = '';

  // Modal de confirmacion personalizado
  mostrarModal = false;
  modalMensaje = '';
  modalCallback: (() => void) | null = null;

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
    if (!this.authService.esAdmin()) { this.router.navigate(['/']); return; }
    this.cargarStats();
    this.cargarCampanias();
    this.cargarUsuarios();
  }

  get headers() {
    return { Authorization: `Bearer ${this.authService.getToken()}` };
  }

  cargarStats(): void {
    this.ngZone.run(() => {
      this.http.get<Stats>('http://localhost:8080/api/admin/stats', { headers: this.headers })
        .subscribe({ next: (d) => { this.stats = d; this.cdr.detectChanges(); }, error: () => {} });
    });
  }

  cargarCampanias(): void {
    this.ngZone.run(() => {
      this.http.get<CampaniaAdmin[]>('http://localhost:8080/api/admin/campanias', { headers: this.headers })
        .subscribe({ next: (d) => { this.campanias = d; this.cdr.detectChanges(); }, error: () => {} });
    });
  }

  cargarUsuarios(): void {
    this.ngZone.run(() => {
      this.http.get<UsuarioAdmin[]>('http://localhost:8080/api/admin/usuarios', { headers: this.headers })
        .subscribe({ next: (d) => { this.usuarios = d; this.cdr.detectChanges(); }, error: () => {} });
    });
  }

  // ── MODAL ────────────────────────────────────────────────────────────────
  abrirModal(mensaje: string, callback: () => void): void {
    this.modalMensaje = mensaje;
    this.modalCallback = callback;
    this.mostrarModal = true;
    this.cdr.detectChanges();
  }

  ejecutarModal(): void {
    this.mostrarModal = false;
    if (this.modalCallback) this.modalCallback();
    this.modalCallback = null;
    this.cdr.detectChanges();
  }

  cancelarModal(): void {
    this.mostrarModal = false;
    this.modalCallback = null;
    this.cdr.detectChanges();
  }

  // ── CAMPAÑAS ─────────────────────────────────────────────────────────────
  borrarCampania(id: number, titulo: string): void {
    this.abrirModal(
      `¿Eliminar la campaña "${titulo}"? Esta acción no se puede deshacer.`,
      () => {
        this.http.delete(`http://localhost:8080/api/admin/campanias/${id}`,
          { headers: this.headers, responseType: 'text' })
          .subscribe({
            next: () => {
              this.campanias = this.campanias.filter(c => c.idCampania !== id);
              this.ok('Campaña eliminada correctamente');
              this.cdr.detectChanges();
            },
            error: () => this.err('Error al eliminar la campaña')
          });
      }
    );
  }

  cambiarEstado(id: number, estado: string): void {
    this.http.put(`http://localhost:8080/api/admin/campanias/${id}/estado?estado=${estado}`,
      {}, { headers: this.headers, responseType: 'text' })
      .subscribe({
        next: () => {
          const c = this.campanias.find(x => x.idCampania === id);
          if (c) { c.estado = estado.toUpperCase(); this.cdr.detectChanges(); }
          this.ok('Estado actualizado');
        },
        error: () => this.err('Error al cambiar estado')
      });
  }

  // ── USUARIOS ─────────────────────────────────────────────────────────────
  cambiarRol(id: number, rol: string): void {
    this.http.put(`http://localhost:8080/api/admin/usuarios/${id}/rol?rol=${rol}`,
      {}, { headers: this.headers, responseType: 'text' })
      .subscribe({
        next: () => {
          const u = this.usuarios.find(x => x.idUsuario === id);
          if (u) { u.rol = rol; this.cdr.detectChanges(); }
          this.ok('Rol actualizado');
        },
        error: () => this.err('Error al cambiar rol')
      });
  }

  darDeBaja(id: number, nombre: string): void {
    this.abrirModal(
      `¿Dar de baja al usuario "${nombre}"? Perderá acceso a la plataforma.`,
      () => {
        this.http.put(`http://localhost:8080/api/admin/usuarios/${id}/baja`,
          {}, { headers: this.headers, responseType: 'text' })
          .subscribe({
            next: () => {
              const u = this.usuarios.find(x => x.idUsuario === id);
              if (u) { u.activo = false; this.cdr.detectChanges(); }
              this.ok('Usuario dado de baja');
            },
            error: () => this.err('Error al dar de baja')
          });
      }
    );
  }

  // ── FILTROS ──────────────────────────────────────────────────────────────
  get campaniasFiltradas(): CampaniaAdmin[] {
    if (!this.filtroCampania) return this.campanias;
    const f = this.filtroCampania.toLowerCase();
    return this.campanias.filter(c =>
      c.titulo?.toLowerCase().includes(f) ||
      c.nombreCreador?.toLowerCase().includes(f) ||
      c.nombreCategoria?.toLowerCase().includes(f));
  }

  get usuariosFiltrados(): UsuarioAdmin[] {
    if (!this.filtroUsuario) return this.usuarios;
    const f = this.filtroUsuario.toLowerCase();
    return this.usuarios.filter(u =>
      u.nombre?.toLowerCase().includes(f) ||
      u.email?.toLowerCase().includes(f) ||
      u.rol?.toLowerCase().includes(f));
  }

  // ── HELPERS ──────────────────────────────────────────────────────────────
  private ok(msg: string): void {
    this.mensajeExito = msg;
    setTimeout(() => { this.mensajeExito = ''; this.cdr.detectChanges(); }, 3000);
  }

  private err(msg: string): void {
    this.mensajeError = msg;
    setTimeout(() => { this.mensajeError = ''; this.cdr.detectChanges(); }, 4000);
  }
}