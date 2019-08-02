import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NavItem } from '../models/nav-item';
import { SecurityService } from '../security/security.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  activeLink = '';
  @Input() title: string;
  @Input() navList: NavItem[];

  loggedUser: string;

  constructor(
    private securityService: SecurityService,
    private router: Router
  ) { }

  ngOnInit() {
    this.securityService.loggedUserIn.subscribe(username => {
      this.loggedUser = username;
    });
    this.navList = this.navList.filter(item => this.securityService.hasPrivilege(item.privileges));
  }

  handleClick(selectedItem) {
    this.activeLink = selectedItem.linkTitle;
  }

  logout() {
    this.securityService.logout();
    this.router.navigate(['/login']);
  }
}
