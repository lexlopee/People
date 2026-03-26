import { Component } from '@angular/core';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-categoria',
  standalone: true,
  imports: [NgFor],
  templateUrl: './categoria.html',
  styleUrls: ['./categoria.css']
})
export class Categoria {
  categorias = [
    'Medicina',
    'In memoriam',
    'Emergencia',
    'Organización sin ánimo de lucro',
    'Educación',
    'Animales',
    'Medioambiente',
    'Empresa',
    'Comunidad',
    'Competición',
    'Artes creativas',
    'Evento',
    'Religión',
    'Familia',
    'Deportes',
    'Viajes',
    'Voluntariado',
    'Deseos'
  ];
}
