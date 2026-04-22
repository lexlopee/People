import { Component } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [TranslateModule],
  templateUrl: './footer.html',
  styleUrl: './footer.css',
})
export class Footer {
  currentYear = new Date().getFullYear();

  constructor(private router: Router) {}

  goTo(section: string): void {
    if (this.router.url === '/legal' || this.router.url.startsWith('/legal#')) {
      // Ya estamos en /legal → scroll directo
      this.scrollToSection(section);
    } else {
      // Navegar a /legal y esperar a que Angular termine de renderizar
      this.router.navigate(['/legal']).then(() => {
        // Esperamos al próximo evento NavigationEnd para asegurarnos
        // de que el componente Legal ya está en el DOM
        const sub = this.router.events
          .pipe(filter(e => e instanceof NavigationEnd))
          .subscribe(() => {
            sub.unsubscribe();
          });

        // Doble setTimeout: primero Angular renderiza, luego hacemos scroll
        setTimeout(() => {
          this.scrollToSection(section);
        }, 300);
      });
    }
  }

  private scrollToSection(section: string): void {
    const el = document.getElementById(section);
    if (el) {
      el.scrollIntoView({ behavior: 'smooth', block: 'start' });
    } else {
      // Si por algún motivo el DOM no está listo, reintentar
      setTimeout(() => {
        const retryEl = document.getElementById(section);
        if (retryEl) {
          retryEl.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
      }, 300);
    }
  }
}