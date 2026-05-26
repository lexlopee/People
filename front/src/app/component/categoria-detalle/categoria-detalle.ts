import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, NgZone } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

interface Campana {
  idCampania: number;
  titulo: string;
  montoObjetivo: number;
  montoActual: number;
  nombreCreador: string;
  nombreCategoria: string;
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

  private nombreMap: Record<string, string> = {
    'medicina': 'Medicina', 'in-memoriam': 'In Memoriam',
    'emergencia': 'Emergencia', 'sin-animo-de-lucro': 'Sin ánimo de lucro',
    'educacion': 'Educación', 'animales': 'Animales',
    'medioambiente': 'Medioambiente', 'empresa': 'Empresa',
    'comunidad': 'Comunidad', 'competicion': 'Competición',
    'artes-creativas': 'Artes creativas', 'evento': 'Evento',
    'religion': 'Religión', 'familia': 'Familia',
    'deportes': 'Deportes', 'viajes': 'Viajes',
    'voluntariado': 'Voluntariado', 'deseos': 'Deseos'
  };

  slug = '';
  translateKey = '';
  icon = '';
  notFound = false;
  campanas: Campana[] = [];
  cargandoCampanas = false;

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
      this.cdr.detectChanges();

      if (!this.notFound && isPlatformBrowser(this.platformId)) {
        this.cargarCampanas();
      }
    });
  }

  cargarCampanas(): void {
    this.cargandoCampanas = true;
    this.cdr.detectChanges();

    // Usamos ngZone.run para asegurar que Angular detecta los cambios
    this.ngZone.run(() => {
      this.http.get<Campana[]>('http://localhost:8080/api/campanias').subscribe({
        next: (todas) => {
          const nombreCat = this.nombreMap[this.slug] ?? '';
          this.campanas = todas.filter(c =>
            c.nombreCategoria?.toLowerCase() === nombreCat.toLowerCase()
          );
          this.cargandoCampanas = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.cargandoCampanas = false;
          this.cdr.detectChanges();
        }
      });
    });
  }

  irACrearCampana(): void {
    if (!this.authService.estaLogueado()) {
      this.router.navigate(['/login']);
    } else if (!this.authService.puedeCrearCampana()) {
      alert('Solo las creadoras pueden publicar campañas.');
    } else {
      this.router.navigate(['/crear-campana']);
    }
  }
}