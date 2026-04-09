import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoriaDetalle } from './categoria-detalle';

describe('CategoriaDetalle', () => {
  let component: CategoriaDetalle;
  let fixture: ComponentFixture<CategoriaDetalle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoriaDetalle],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoriaDetalle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
