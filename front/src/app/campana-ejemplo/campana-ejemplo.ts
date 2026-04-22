import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-campana-ejemplo',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslateModule],
  templateUrl: './campana-ejemplo.html',
  styleUrls: ['./campana-ejemplo.css']
})
export class CampanaEjemplo {

  // Datos de la campaña de ejemplo
  campana = {
    titulo: 'Fondo de Medicamentos para Mujeres sin Recursos',
    categoria: 'Medicina',
    emoji: '💊',
    promotora: 'Dra. Isabel Cano · Asociación Salud Solidaria',
    ubicacion: 'Sevilla, España',
    verificada: true,
    estado: 'activa' as const,
    montoObjetivo: 15000,
    montoActual: 10875,
    donantes: 312,
    fechaFin: '2026-05-30',
  };

  get porcentaje(): number {
    return Math.min(100, Math.round((this.campana.montoActual / this.campana.montoObjetivo) * 100));
  }

  get diasRestantes(): number {
    const hoy = new Date();
    const fin = new Date(this.campana.fechaFin);
    const diff = fin.getTime() - hoy.getTime();
    return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)));
  }


  donacionesRecientes = [
    { inicial: 'M', nombre: 'María G.', importe: '50 €', tiempo: 'Hace 2 min' },
    { inicial: 'L', nombre: 'Laura P.', importe: '30 €', tiempo: 'Hace 15 min' },
    { inicial: 'A', nombre: 'Anónima', importe: '100 €', tiempo: 'Hace 1 h' },
    { inicial: 'S', nombre: 'Sara T.', importe: '20 €', tiempo: 'Hace 3 h' },
  ];
  updates = [
    {
      fecha: '18 abril 2026',
      titulo: '¡Superamos el 70%! Primeras entregas realizadas',
      cuerpo: 'Gracias a vuestra generosidad hemos podido entregar los primeros lotes de medicamentos a 45 mujeres en situación de exclusión social en los barrios de Triana y Polígono Sur. Las fotos y los testimonios están en nuestra web.',
    },
    {
      fecha: '1 abril 2026',
      titulo: 'Campaña validada y en marcha',
      cuerpo: 'Tras pasar la validación manual de People, lanzamos oficialmente la campaña. Nuestro objetivo es cubrir tratamientos crónicos (diabetes, hipertensión, tiroides) para mujeres que no pueden costeárselos. Cada 30 € cubre un mes de tratamiento completo.',
    },
  ];
}