import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DescribePodModalComponent } from './describe-pod-modal.component';

describe('DescribePodModalComponent', () => {
  let component: DescribePodModalComponent;
  let fixture: ComponentFixture<DescribePodModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DescribePodModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DescribePodModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
