import { Component, OnInit, OnDestroy } from '@angular/core';

import { NavItem } from './app-security-module/models/nav-item';
import { SecurityService } from './app-security-module/security/security.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  appTitle = 'JWT Security Demo';

  isLoggedIn$: boolean = false;

  navList: NavItem[] = [
    { linkTitle: 'Administration', link: '/administration', icon: 'people', privileges: ['ROLE_ADMIN_PRIVILEGE'] },
    { linkTitle: 'Activity Log', link: '/activity-logs', icon: 'remove_red_eye', privileges: ['ROLE_ACTIVITY_PRIVILEGE'] },
    { linkTitle: 'Product', link: '/products', icon: 'home' }
  ];

  constructor(
    private securityService: SecurityService
  ) {}

  ngOnInit() {
    this.securityService.isLoggedIn.subscribe( logged => {
      this.isLoggedIn$ = logged;
    });
  }
}
