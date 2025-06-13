import { Component } from '@angular/core';
import { PodsListComponent } from './pods-list/pods-list.component';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatMenuModule} from '@angular/material/menu';
import { DeploymentsListComponent } from './deployments-list/deployments-list.component';
import { NgIf } from '@angular/common';
@Component({
  selector: 'app-root',
  imports: [
    PodsListComponent,
    DeploymentsListComponent,

    // Angular common directives
    NgIf,
    // Angular Material modules
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'KVO Debug UI';
  currentView = 'pods';

  changeView(view: string) {
    this.currentView = view;
  }
}
