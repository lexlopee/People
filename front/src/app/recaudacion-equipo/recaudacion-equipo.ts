import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-recaudacion-equipo',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslateModule],
  templateUrl: './recaudacion-equipo.html',
  styleUrls: ['./recaudacion-equipo.css']
})
export class RecaudacionEquipo {

  pasos = [
    {
      numero: 1,
      emoji: '🔍',
      titulo: 'Elige una campaña',
      desc: 'Navega por las categorías y escoge la causa que más te inspire. Puede ser de Educación, Salud, Medioambiente... cualquier campaña activa admite equipos.'
    },
    {
      numero: 2,
      emoji: '👥',
      titulo: 'Crea tu equipo',
      desc: 'Dale un nombre a tu equipo, escribe un mensaje motivador y comparte el enlace con tus amigas, compañeras de trabajo o familia. Cada una tendrá su propia página.'
    },
    {
      numero: 3,
      emoji: '📣',
      titulo: 'Difunde y recauda',
      desc: 'Cada miembro comparte su enlace personal en redes sociales, WhatsApp o email. Todas las donaciones se acumulan en el marcador del equipo en tiempo real.'
    },
    {
      numero: 4,
      emoji: '🏆',
      titulo: 'Celebrad juntas el impacto',
      desc: 'Cuando el equipo alcanza la meta, People os envía un certificado de impacto colectivo. La promotora de la campaña os agradecerá personalmente vuestra contribución.'
    }
  ];

  equiposEjemplo = [
    {
      nombre: 'Las Chicas de Recursos Humanos',
      campana: 'Fondo de Medicamentos para Mujeres sin Recursos',
      categoria: 'Medicina',
      emoji: '💊',
      miembros: 8,
      recaudado: 640,
      meta: 800,
    },
    {
      nombre: 'Mamás del Cole Cervantes',
      campana: 'Taller de Programación para Niñas Rurales',
      categoria: 'Educación',
      emoji: '💻',
      miembros: 12,
      recaudado: 1080,
      meta: 1200,
    },
    {
      nombre: 'Voluntarias Verdes BCN',
      campana: 'GreenMujer: Huertos Urbanos',
      categoria: 'Medioambiente',
      emoji: '🌱',
      miembros: 5,
      recaudado: 350,
      meta: 600,
    }
  ];

  getPct(eq: any): number {
    return Math.min(100, Math.round((eq.recaudado / eq.meta) * 100));
  }

  porQueEquipo = [
    { emoji: '🌐', titulo: 'Alcance multiplicado', desc: 'Si cada miembro tiene 200 contactos, un equipo de 10 personas alcanza potencialmente 2.000 donantes nuevos.' },
    { emoji: '🔥', titulo: 'Motivación colectiva', desc: 'Ver el marcador subir gracias a las demás genera un efecto de arrastre: nadie quiere ser la que menos aporta.' },
    { emoji: '🤝', titulo: 'Vínculo de causa', desc: 'Recaudar juntas por algo en lo que creéis crea lazos reales. Muchos equipos repiten campaña tras campaña.' },
    { emoji: '📊', titulo: 'Transparencia total', desc: 'Cada miembro ve en tiempo real cuánto ha aportado su red. People publica el impacto final con nombres y cifras.' },
  ];
}