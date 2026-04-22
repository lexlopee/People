import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

export interface Campaña {
  id: number;
  titulo: string;
  descripcion: string;
  promotora: string;
  categorias: string[];
  montoObjetivo: number;
  montoActual: number;
  fechaFin: string;
  estado: 'activa' | 'finalizada' | 'pendiente';
  verificada: boolean;
  emoji: string;
}

@Component({
  selector: 'app-campana-ejemplo',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslateModule],
  templateUrl: './campana-ejemplo.html',
  styleUrls: ['./campana-ejemplo.css']
})
export class CampanaEjemplo {

  campanas: Campaña[] = [
    {
      id: 1,
      titulo: 'Taller de Costura Creativa',
      descripcion: 'Ampliamos nuestro taller en Madrid para dar empleo a 10 mujeres de Lavapiés. Formación profesional + máquinas industriales + mercado online.',
      promotora: 'María García · Coser con Arte S.L.',
      categorias: ['Empresa', 'Comunidad'],
      montoObjetivo: 8000,
      montoActual: 6240,
      fechaFin: '2025-12-01',
      estado: 'activa',
      verificada: true,
      emoji: '🧵'
    },
    {
      id: 2,
      titulo: 'GreenMujer: Huertos Urbanos',
      descripcion: 'Transformamos solares abandonados en Barcelona en huertos urbanos productivos. Formación en agricultura ecológica para 20 mujeres.',
      promotora: 'Ana Rodríguez · GreenMujer Cooperativa',
      categorias: ['Medioambiente', 'Comunidad'],
      montoObjetivo: 12000,
      montoActual: 4800,
      fechaFin: '2026-02-15',
      estado: 'activa',
      verificada: true,
      emoji: '🌱'
    },
    {
      id: 3,
      titulo: 'Colección "Todas Caben"',
      descripcion: 'Primera colección de moda inclusiva tallas 34-60, producida en talleres locales con materiales sostenibles. Diseño para todos los cuerpos.',
      promotora: 'Sofía Martínez · Diseñadora Independiente',
      categorias: ['Artes creativas', 'Empresa'],
      montoObjetivo: 5000,
      montoActual: 750,
      fechaFin: '2026-03-01',
      estado: 'activa',
      verificada: false,
      emoji: '👗'
    }
  ];

  getPorcentaje(campaña: Campaña): number {
    return Math.min(100, Math.round((campaña.montoActual / campaña.montoObjetivo) * 100));
  }

  getDiasRestantes(fechaFin: string): number {
    const hoy = new Date();
    const fin = new Date(fechaFin);
    const diff = fin.getTime() - hoy.getTime();
    return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)));
  }
}