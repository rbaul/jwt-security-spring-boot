import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParticleComponent } from './particle.component';

describe('ParticleComponent', () => {
  let component: ParticleComponent;
  let fixture: ComponentFixture<ParticleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParticleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
