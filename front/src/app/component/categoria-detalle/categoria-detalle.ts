import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-categoria-detalle',
  standalone: true,
  imports: [CommonModule, TranslateModule, RouterLink],
  templateUrl: './categoria-detalle.html',
  styleUrls: ['./categoria-detalle.css']
})
export class CategoriaDetalle implements OnInit {

  // Mapa slug → clave de traducción
  private slugMap: Record<string, string> = {
    'medicina':              'CATEGORIES.MEDICINE',
    'in-memoriam':           'CATEGORIES.IN_MEMORIAM',
    'emergencia':            'CATEGORIES.EMERGENCY',
    'sin-animo-de-lucro':    'CATEGORIES.NONPROFIT',
    'educacion':             'CATEGORIES.EDUCATION',
    'animales':              'CATEGORIES.ANIMALS',
    'medioambiente':         'CATEGORIES.ENVIRONMENT',
    'empresa':               'CATEGORIES.BUSINESS',
    'comunidad':             'CATEGORIES.COMMUNITY',
    'competicion':           'CATEGORIES.COMPETITION',
    'artes-creativas':       'CATEGORIES.CREATIVE_ARTS',
    'evento':                'CATEGORIES.EVENT',
    'religion':              'CATEGORIES.RELIGION',
    'familia':               'CATEGORIES.FAMILY',
    'deportes':              'CATEGORIES.SPORTS',
    'viajes':                'CATEGORIES.TRAVEL',
    'voluntariado':          'CATEGORIES.VOLUNTEERING',
    'deseos':                'CATEGORIES.WISHES'
  };

  // Mapa slug → emoji representativo
  private iconMap: Record<string, string> = {
    'medicina':           '🏥',
    'in-memoriam':        '🕊️',
    'emergencia':         '🚨',
    'sin-animo-de-lucro': '🤝',
    'educacion':          '📚',
    'animales':           '🐾',
    'medioambiente':      '🌿',
    'empresa':            '💼',
    'comunidad':          '🏘️',
    'competicion':        '🏆',
    'artes-creativas':    '🎨',
    'evento':             '🎉',
    'religion':           '🙏',
    'familia':            '👨‍👩‍👧',
    'deportes':           '⚽',
    'viajes':             '✈️',
    'voluntariado':       '💚',
    'deseos':             '⭐'
  };

  slug = '';
  translateKey = '';
  icon = '';
  notFound = false;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.slug = params.get('slug') ?? '';
      this.translateKey = this.slugMap[this.slug] ?? '';
      this.icon = this.iconMap[this.slug] ?? '📁';
      this.notFound = !this.translateKey;
    });
  }
}