import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Categoria } from './component/categoria/categoria';
import { Login } from './login/login';
import { Register } from './register/register';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'categoria', component: Categoria },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: '**', redirectTo: '' }
];