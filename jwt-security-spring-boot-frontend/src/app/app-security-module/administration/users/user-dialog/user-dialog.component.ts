import { Component, OnInit, Inject } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { RoleApiService } from 'src/app/app-security-module/services/role-api.service';
import { UserResponseDto, UserRequestDto } from 'src/app/app-security-module/models/user';
import { RoleResponseDto } from 'src/app/app-security-module/models/role';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.scss']
})
export class UserDialogComponent implements OnInit {

  form: FormGroup;

  roles: RoleResponseDto[];

  constructor(
      private fb: FormBuilder,
      private roleApiService: RoleApiService,
      private dialogRef: MatDialogRef<UserDialogComponent>,
      @Inject(MAT_DIALOG_DATA) data: UserResponseDto) {
        this.roleApiService.getAllRoles().subscribe(rolesResponse => {
          this.roles = rolesResponse;
          const selectedRoles = data.roles ?
           this.roles.filter(role => data.roles.map(r => r.id).indexOf(role.id) > -1) : [];

           this.form.patchValue({
             roles: selectedRoles
           });
        });

        this.form = this.fb.group({
          firstName: [data.firstName, [Validators.required]],
          lastName: [data.lastName, [Validators.required]],
          username: [data.username, [Validators.required]],
          password: [undefined, [Validators.required]],
          roles: [[], [Validators.required, Validators.min(1)]]
        });
  }

  ngOnInit() {

  }

  save() {
    const userRequest: UserRequestDto = {
      firstName: this.form.value.firstName,
      lastName: this.form.value.lastName,
      username: this.form.value.username,
      password: this.form.value.password,
      roleIds: this.form.value.roles.map(role => role.id),
    };
    this.dialogRef.close(userRequest);
  }

}
