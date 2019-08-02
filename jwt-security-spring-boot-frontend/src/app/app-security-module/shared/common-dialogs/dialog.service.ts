import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material';
import { ConfirmationDialogComponent, ConfirmationDialogData } from './confirmation-dialog/confirmation-dialog.component';
import { AlertDialogData, AlertDialogComponent } from './alert-dialog/alert-dialog.component';
import { LoginDialogComponent } from '../../login/login-dialog/login-dialog.component';

@Injectable({
    providedIn: 'root'
})
export class DialogService {

    constructor(
        private dialog: MatDialog
    ) { }

    openConfirmDialog(confirmationDialogData: ConfirmationDialogData): MatDialogRef<ConfirmationDialogComponent> {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = confirmationDialogData;
        return this.dialog.open(ConfirmationDialogComponent, dialogConfig);
    }

    openAlertDialog(alertDialogData: AlertDialogData): MatDialogRef<AlertDialogComponent> {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = alertDialogData;
        return this.dialog.open(AlertDialogComponent, dialogConfig);
    }

    openLoginDialog(): MatDialogRef<LoginDialogComponent> {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        // dialogConfig.data = alertDialogData;
        return this.dialog.open(LoginDialogComponent, dialogConfig);
    }
}
