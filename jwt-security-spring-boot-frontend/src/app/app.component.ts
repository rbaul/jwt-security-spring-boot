import { Component, OnInit, OnDestroy } from '@angular/core';

import { Observable } from 'rxjs';
import { SecurityService } from './security/security.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'jwt-security-frontend';

  isLoggedIn$: boolean = false;

  constructor(
    private securityService: SecurityService
  ) {}

  ngOnInit() {
    this.securityService.isLoggedIn.subscribe( logged => {
      this.isLoggedIn$ = logged;
    });
  }
}
