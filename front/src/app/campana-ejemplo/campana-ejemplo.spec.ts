import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CampanaEjemplo } from './campana-ejemplo';

describe('CampanaEjemplo', () => {
  let component: CampanaEjemplo;
  let fixture: ComponentFixture<CampanaEjemplo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CampanaEjemplo],
    }).compileComponents();

    fixture = TestBed.createComponent(CampanaEjemplo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have 3 example campaigns', () => {
    expect(component.campanas.length).toBe(3);
  });

  it('should calculate percentage correctly', () => {
    const pct = component.getPorcentaje(component.campanas[0]); // 6240/8000 = 78%
    expect(pct).toBe(78);
  });
});