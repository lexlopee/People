import { Routes } from '@angular/router';
import { Categoria } from './component/categoria/categoria';

export const routes: Routes = [
  { path: '', redirectTo: 'categoria', pathMatch: 'full' },
  { path: 'categoria', component: Categoria }
];
