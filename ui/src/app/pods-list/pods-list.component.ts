import { Component, OnInit, OnDestroy } from '@angular/core';
import { interval, Subscription } from 'rxjs';
import { Pod, PodService } from '../pod.service';
import { FormsModule } from '@angular/forms';
import { NgClass, NgFor, NgIf } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { DescribePodModalComponent } from '../describe-pod-modal/describe-pod-modal.component';

@Component({
  selector: 'pods-list',
  templateUrl: './pods-list.component.html',
  styleUrls: ['./pods-list.component.css'],
  imports: [FormsModule, NgFor, NgClass, NgIf, MatTableModule]

})
export class PodsListComponent implements OnInit, OnDestroy {
  pods: Pod[] = [];
  loading = true;
  error = '';
  filterText = '';

  selectedPods: Map<string, Pod> = new Map();

  private refreshSubscription: Subscription | undefined;

  constructor(private podService: PodService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadPods();
    this.refreshSubscription = interval(5000).subscribe(() => this.loadPods());
  }

  ngOnDestroy(): void {
    this.refreshSubscription?.unsubscribe();
  }

  get filteredPods(): Pod[] {
    return this.pods.filter(pod =>
      pod.name.toLowerCase().includes(this.filterText.toLowerCase())
    );
  }

  isPodSelected(p: Pod): boolean {
    return this.selectedPods.has(p.name);
  }

  togglePodSelection(pod: Pod): void {
    if (this.selectedPods.has(pod.name)) {
      this.selectedPods.delete(pod.name);
    } else {
      this.selectedPods.set(pod.name, pod);
    }
  }

  describePod(pod: Pod): void {
    this.podService.desscribePod(pod).subscribe({
      next: (data) => {
        const json = JSON.stringify(data, null, 2);
        this.dialog.open(DescribePodModalComponent, {
          data: { podName: pod.name, json },
          width: '95%',
          height: '80%'
        });
      },
      error: (err) => {
        console.error('Error describing pod:', err);
        this.error = 'Failed to describe pod.';
      }
    });
  }

  loadPods(): void {
    this.loading = true;
    this.podService.getPods().subscribe({
      next: (data) => {
        this.pods = data;
        this.loading = false;
        this.error = '';
      },
      error: (err) => {
        console.error('Error fetching pods:', err);
        this.error = 'Failed to load pods.';
        this.loading = false;
      }
    });
  }


  restartSelectedPods(): void {
    if (this.selectedPods.size > 0) {
      const podsArray = Array.from(this.selectedPods.values());
      podsArray.forEach((pod, idx) => {
        const isLast = idx === podsArray.length - 1;
        this.podService.restartPod(pod, isLast ? () => this.loadPods() : undefined);
      });
      this.selectedPods.clear();
    }
  }
  restartPod(pod: Pod): void {
    this.podService.restartPod(pod, () => this.loadPods());
    this.selectedPods.clear();
  }

  showSuccessToast(message: string): void {
    // e.g., use MatSnackBar from Angular Material
    //this.snackBar.open(message, 'Dismiss', { duration: 3000 });
  }
}
