import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';

function passwordsMatch(c: AbstractControl): ValidationErrors | null {
  const p = c.get('password'), r = c.get('confirmPassword');
  return p && r && p.value !== r.value ? { passwordsMismatch: true } : null;
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  registerForm: FormGroup;
  showPassword = false;
  showConfirm  = false;
  errorMessage = '';
  loading      = false;

  constructor(private fb: FormBuilder, private router: Router, private authService: AuthService) {
    this.registerForm = this.fb.group({
      nombre:          ['', [Validators.required, Validators.minLength(2)]],
      email:           ['', [Validators.required, Validators.email]],
      rol:             ['donante', Validators.required],
      password:        ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: passwordsMatch });
  }

  togglePassword() { this.showPassword = !this.showPassword; }
  toggleConfirm()  { this.showConfirm  = !this.showConfirm; }

  onSubmit() {
    if (this.registerForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.authService.registro({
        nombre:      this.registerForm.value.nombre,
        email:       this.registerForm.value.email,
        rol:         this.registerForm.value.rol,
        contrasenia: this.registerForm.value.password
      }).subscribe({
        next: () => { this.loading = false; this.router.navigate(['/login']); },
        error: (err) => {
          this.loading = false;
          this.errorMessage = err.status === 409
            ? 'Este email ya está registrado'
            : 'Error al registrar. Inténtalo de nuevo.';
        }
      });
    }
  }
}