import { Component, OnInit, OnDestroy } from '@angular/core';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService, UsuarioSesion } from '../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, TranslateModule, RouterLink],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class Header implements OnInit, OnDestroy {

  currentLang = 'es';
  usuario: UsuarioSesion | null = null;
  private sub!: Subscription;

  constructor(private translate: TranslateService, public authService: AuthService) {}

  ngOnInit(): void {
    this.sub = this.authService.usuario$.subscribe(u => this.usuario = u);
  }

  ngOnDestroy(): void { this.sub?.unsubscribe(); }

  changeLang(lang: string) { this.currentLang = lang; this.translate.use(lang); }
  logout() { this.authService.logout(); }

  get inicial(): string {
    return (this.usuario?.nombre ?? this.usuario?.email ?? '').charAt(0).toUpperCase();
  }

  // Muestra el rol real que viene de la BD: 'donante' o 'creador'
  // con la primera letra en mayúscula
  get rolLabel(): string {
    const r = this.usuario?.rol ?? '';
    return r.charAt(0).toUpperCase() + r.slice(1);
  }
}