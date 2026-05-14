import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

export interface UsuarioSesion {
  id: number;
  nombre: string;
  email: string;
  rol: string;   // 'donante' | 'creador' | 'administrador'
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly API      = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'token';
  private readonly USER_KEY  = 'usuario';

  private usuarioSubject = new BehaviorSubject<UsuarioSesion | null>(this.cargarGuardado());
  public usuario$ = this.usuarioSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, contrasenia: string): Observable<any> {
    return this.http.post<any>(`${this.API}/login`, { email, contrasenia }).pipe(
      tap(res => {
        localStorage.setItem(this.TOKEN_KEY, res.token);
        const u: UsuarioSesion = {
          id: res.id,
          nombre: res.nombre,
          email: res.email,
          rol: res.rol
        };
        localStorage.setItem(this.USER_KEY, JSON.stringify(u));
        this.usuarioSubject.next(u);
      })
    );
  }

  registro(datos: { nombre: string; email: string; rol: string; contrasenia: string }): Observable<any> {
    return this.http.post(`${this.API}/registro`, datos, { responseType: 'text' });
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.usuarioSubject.next(null);
    this.router.navigate(['/']);
  }

  get usuario(): UsuarioSesion | null  { return this.usuarioSubject.value; }
  getToken(): string | null            { return localStorage.getItem(this.TOKEN_KEY); }
  estaLogueado(): boolean              { return !!this.getToken(); }
  esAdmin(): boolean                   { return this.usuario?.rol === 'administrador'; }

  private cargarGuardado(): UsuarioSesion | null {
    try {
      const s = localStorage.getItem(this.USER_KEY);
      return s ? JSON.parse(s) : null;
    } catch { return null; }
  }
}