import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

function passwordsMatch(control: AbstractControl): ValidationErrors | null {
  const password = control.get('password');
  const confirm = control.get('confirmPassword');
  if (password && confirm && password.value !== confirm.value) {
    return { passwordsMismatch: true };
  }
  return null;
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
  showConfirm = false;

  constructor(
    private fb: FormBuilder, 
    private router: Router,
    private http: HttpClient
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      rol: ['DONANTE', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: passwordsMatch });
  }

  togglePassword() { this.showPassword = !this.showPassword; }
  toggleConfirm() { this.showConfirm = !this.showConfirm; }

  onSubmit() {
    if (this.registerForm.valid) {
      // Preparamos el JSON exactamente con los nombres que espera tu DTO de Java
      const userData = {
        nombre: this.registerForm.value.nombre,
        email: this.registerForm.value.email,
        rol: this.registerForm.value.rol.toUpperCase(), // Lo enviamos en mayúsculas
        contrasenia: this.registerForm.value.password // ¡Importante! El backend espera "contrasenia"
      };

      // Llamada a tu endpoint exacto
      this.http.post('http://localhost:8080/api/auth/registro', userData, { responseType: 'text' })
        .subscribe({
          next: (response) => {
            console.log('Registro exitoso', response);
            alert('¡Usuario registrado correctamente!');
            this.router.navigate(['/login']);
          },
          error: (err) => {
            console.error('Error del servidor', err);
            alert('Error: ' + (err.error || 'No se pudo registrar el usuario.'));
          }
        });
    }
  }
}