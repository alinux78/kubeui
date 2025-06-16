import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';

import 'prismjs/components/prism-json';
@Component({
  selector: 'describe-pod-modal',
  templateUrl: './describe-pod-modal.component.html',
  styleUrls: ['./describe-pod-modal.component.css'],
  imports: [MatDialogModule],
})
export class DescribePodModalComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { podName: string; json: string }
  ) {}
}
