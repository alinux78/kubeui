import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PodsListComponent } from './pods-list.component';

describe('PodsListComponent', () => {
  let component: PodsListComponent;
  let fixture: ComponentFixture<PodsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PodsListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PodsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
