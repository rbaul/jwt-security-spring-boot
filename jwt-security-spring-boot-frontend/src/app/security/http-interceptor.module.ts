import { Injectable, NgModule } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler,
         HttpRequest,
         HttpErrorResponse} from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { SecurityService } from './security.service';
import { Router } from '@angular/router';
import { catchError, map } from 'rxjs/operators';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {

  constructor(
    private router: Router,
    private securityService: SecurityService
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {
    const token = localStorage.getItem('bearerToken');

    if (token) {
      req = req.clone({
           headers: req.headers.set('Authorization', 'Bearer ' + token)
      });
    }

    return next.handle(req).pipe(
      map((event: HttpEvent<any>) => {
        // if (event instanceof HttpResponse) {
            // console.log('event--->>>', event);
            // this.errorDialogService.openDialog(event);
        // }
        return event;
    }),
    catchError((error: HttpErrorResponse) => {
        let data = {};
        data = {
            reason: error && error.error.reason ? error.error.reason : '',
            status: error.status
        };
        if (error.status === 401 || error.status === 403) {
          // this.errorDialogService.openDialog(data);
          this.securityService.logoutProccess();
          this.router.navigateByUrl(`/login`);
        }

        return throwError(error);
    })
    );

  }
}

@NgModule({
  providers: [
    { provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptor,
      multi: true,
      deps: [Router, SecurityService]
    }
  ]
})
export class HttpInterceptorModule { }