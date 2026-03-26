import { Component } from '@angular/core';
import { NgFor } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [NgFor, TranslateModule],
  templateUrl: './categoria.html',
  styleUrls: ['./categoria.css']
})
export class Categoria {
  categorias = [
    'CATEGORIES.MEDICINE',
    'CATEGORIES.IN_MEMORIAM',
    'CATEGORIES.EMERGENCY',
    'CATEGORIES.NONPROFIT',
    'CATEGORIES.EDUCATION',
    'CATEGORIES.ANIMALS',
    'CATEGORIES.ENVIRONMENT',
    'CATEGORIES.BUSINESS',
    'CATEGORIES.COMMUNITY',
    'CATEGORIES.COMPETITION',
    'CATEGORIES.CREATIVE_ARTS',
    'CATEGORIES.EVENT',
    'CATEGORIES.RELIGION',
    'CATEGORIES.FAMILY',
    'CATEGORIES.SPORTS',
    'CATEGORIES.TRAVEL',
    'CATEGORIES.VOLUNTEERING',
    'CATEGORIES.WISHES'
  ];
}