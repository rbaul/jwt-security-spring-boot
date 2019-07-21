import { Component, OnInit } from '@angular/core';
import { SecurityService } from '../security/security.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  activeLink = '';
  title = 'JWT Security Demo';

  loggedUser: string;


  listItems = [
    { linkTitle: 'User Manager', link: '/users', icon: 'people', privileges: ['ROLE_ADMIN_PRIVILEGE'] },
    { linkTitle: 'Product', link: '/products', icon: 'home' }
  ];

  constructor(
    private securityService: SecurityService,
    private router: Router
  ) { }

  ngOnInit() {
    this.securityService.loggedUserIn.subscribe(username => {
      this.loggedUser = username;
    });
    this.listItems = this.listItems.filter(item => this.securityService.hasPrivilege(item.privileges));
  }

  handleClick(selectedItem) {
    this.activeLink = selectedItem.linkTitle;
  }

  logout() {
    this.securityService.logoutProccess();
    this.router.navigate(['/login']);
  }
}
