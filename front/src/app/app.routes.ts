import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Categoria } from './component/categoria/categoria';
import { CategoriaDetalle } from './component/categoria-detalle/categoria-detalle';
import { Login } from './login/login';
import { Register } from './register/register';
import { Legal } from './legal/legal';

export const routes: Routes = [
  { path: '',                      component: Home },
  { path: 'categoria',             component: Categoria },
  { path: 'categoria/:slug',       component: CategoriaDetalle },
  { path: 'login',                 component: Login },
  { path: 'register',              component: Register },
  { path: 'legal',                 component: Legal },        // ← sin :slug
  { path: 'legal/:slug',           component: Legal },        // ← mantener por compatibilidad
  { path: '**',                    redirectTo: '' }
];