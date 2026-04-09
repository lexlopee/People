import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [CommonModule, TranslateModule, RouterLink],
  templateUrl: './categoria.html',
  styleUrls: ['./categoria.css']
})
export class Categoria {
  categorias = [
    { key: 'CATEGORIES.MEDICINE',      slug: 'medicina',           icon: '🏥' },
    { key: 'CATEGORIES.IN_MEMORIAM',   slug: 'in-memoriam',        icon: '🕊️' },
    { key: 'CATEGORIES.EMERGENCY',     slug: 'emergencia',         icon: '🚨' },
    { key: 'CATEGORIES.NONPROFIT',     slug: 'sin-animo-de-lucro', icon: '🤝' },
    { key: 'CATEGORIES.EDUCATION',     slug: 'educacion',          icon: '📚' },
    { key: 'CATEGORIES.ANIMALS',       slug: 'animales',           icon: '🐾' },
    { key: 'CATEGORIES.ENVIRONMENT',   slug: 'medioambiente',      icon: '🌿' },
    { key: 'CATEGORIES.BUSINESS',      slug: 'empresa',            icon: '💼' },
    { key: 'CATEGORIES.COMMUNITY',     slug: 'comunidad',          icon: '🏘️' },
    { key: 'CATEGORIES.COMPETITION',   slug: 'competicion',        icon: '🏆' },
    { key: 'CATEGORIES.CREATIVE_ARTS', slug: 'artes-creativas',    icon: '🎨' },
    { key: 'CATEGORIES.EVENT',         slug: 'evento',             icon: '🎉' },
    { key: 'CATEGORIES.RELIGION',      slug: 'religion',           icon: '🙏' },
    { key: 'CATEGORIES.FAMILY',        slug: 'familia',            icon: '👨‍👩‍👧' },
    { key: 'CATEGORIES.SPORTS',        slug: 'deportes',           icon: '⚽' },
    { key: 'CATEGORIES.TRAVEL',        slug: 'viajes',             icon: '✈️' },
    { key: 'CATEGORIES.VOLUNTEERING',  slug: 'voluntariado',       icon: '💚' },
    { key: 'CATEGORIES.WISHES',        slug: 'deseos',             icon: '⭐' },
  ];
}