import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule, RouterLink],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  loginForm: FormGroup;
  showPassword = false;
  errorMessage = '';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private http: HttpClient
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      const loginData = {
        email: this.loginForm.value.email,
        contrasenia: this.loginForm.value.password  // el backend espera "contrasenia"
      };

      this.http.post<any>('http://localhost:8080/api/auth/login', loginData)
        .subscribe({
          next: (response) => {
            // Guardamos el token y datos del usuario en sessionStorage
            sessionStorage.setItem('token', response.token);
            sessionStorage.setItem('usuario', JSON.stringify({
              id: response.id,
              email: response.email,
              rol: response.rol
            }));
            console.log('Login correcto, rol:', response.rol);
            this.loading = false;
            this.router.navigate(['/']);
          },
          error: (err) => {
            this.loading = false;
            if (err.status === 401) {
              this.errorMessage = 'Email o contraseña incorrectos';
            } else {
              this.errorMessage = 'Error al conectar con el servidor';
            }
            console.error('Error login:', err);
          }
        });
    }
  }
}