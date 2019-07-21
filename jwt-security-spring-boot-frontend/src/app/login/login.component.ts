import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { SecurityService } from '../security/security.service';
import { User } from '../security/models/user';
import { UserAuth } from '../security/models/user-auth';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;

  errorMessage: string;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private securityService: SecurityService
  ) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() { return this.loginForm.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
        return;
    }

    this.errorMessage = '';

    this.loading = true;
    this.securityService.login(new User(this.f.username.value, this.f.password.value))
      .subscribe(resp => {
        if (this.returnUrl) {
          this.router.navigateByUrl(this.returnUrl);
        }
      },
        (error) => {
          this.errorMessage = 'Invalid Username or Password';
    });
  }


  isFieldInvalid(field: string) {
    return (
      (!this.loginForm.get(field).valid && this.loginForm.get(field).touched) ||
      (this.loginForm.get(field).untouched && !this.submitted)
    );
  }

}
