import { Component, OnInit, OnDestroy, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, TranslateModule, RouterLink],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home implements OnInit, OnDestroy {

  slides = [
    {
      img: '/assets/img/carousel/slide1.jpg',
      titulo: 'Impulsa proyectos liderados por mujeres',
      desc: 'Cada donacion es un paso hacia un mundo mas igualitario'
    },
    {
      img: '/assets/img/carousel/slide2.jpg',
      titulo: 'Crea tu campana en minutos',
      desc: 'Comparte tu historia y conecta con personas que quieren ayudarte'
    },
    {
      img: '/assets/img/carousel/slide3.jpg',
      titulo: 'Juntas somos mas fuertes',
      desc: 'Miles de mujeres ya han hecho realidad sus proyectos'
    }
  ];

  indiceActual = 0;
  private intervalo: any;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    // Solo arrancamos el autoplay en el navegador, no en SSR
    if (isPlatformBrowser(this.platformId)) {
      this.iniciarAutoplay();
    }
  }

  ngOnDestroy(): void {
    this.detenerAutoplay();
  }

  siguiente(): void {
    this.indiceActual = (this.indiceActual + 1) % this.slides.length;
    this.reiniciarAutoplay();
  }

  anterior(): void {
    this.indiceActual = (this.indiceActual - 1 + this.slides.length) % this.slides.length;
    this.reiniciarAutoplay();
  }

  irA(index: number): void {
    this.indiceActual = index;
    this.reiniciarAutoplay();
  }

  private iniciarAutoplay(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.intervalo = setInterval(() => this.siguiente(), 5000);
    }
  }

  private detenerAutoplay(): void {
    if (this.intervalo) clearInterval(this.intervalo);
  }

  private reiniciarAutoplay(): void {
    this.detenerAutoplay();
    this.iniciarAutoplay();
  }

  stats = [
    { key: 'HOME.STAT_1_NUMBER', label: 'HOME.STAT_1_LABEL' },
    { key: 'HOME.STAT_2_NUMBER', label: 'HOME.STAT_2_LABEL' },
    { key: 'HOME.STAT_3_NUMBER', label: 'HOME.STAT_3_LABEL' },
  ];

  steps = [
    { icon: '✏️', title: 'HOME.STEP_1_TITLE', desc: 'HOME.STEP_1_DESC' },
    { icon: '📢', title: 'HOME.STEP_2_TITLE', desc: 'HOME.STEP_2_DESC' },
    { icon: '💜', title: 'HOME.STEP_3_TITLE', desc: 'HOME.STEP_3_DESC' },
  ];

  values = [
    { icon: '🤝', title: 'HOME.VALUE_1_TITLE', desc: 'HOME.VALUE_1_DESC' },
    { icon: '🔒', title: 'HOME.VALUE_2_TITLE', desc: 'HOME.VALUE_2_DESC' },
    { icon: '🌍', title: 'HOME.VALUE_3_TITLE', desc: 'HOME.VALUE_3_DESC' },
    { icon: '💡', title: 'HOME.VALUE_4_TITLE', desc: 'HOME.VALUE_4_DESC' },
  ];
}