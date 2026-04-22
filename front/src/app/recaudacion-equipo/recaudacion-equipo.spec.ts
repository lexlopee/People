import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecaudacionEquipo } from './recaudacion-equipo';

describe('RecaudacionEquipo', () => {
  let component: RecaudacionEquipo;
  let fixture: ComponentFixture<RecaudacionEquipo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecaudacionEquipo],
    }).compileComponents();

    fixture = TestBed.createComponent(RecaudacionEquipo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
