import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { SecurityService } from './security.service';

@Directive({
  selector: '[hasPrivilege]'
})
export class HasPrivilegeDirective {
  @Input() set hasPrivilege(privileges: String[]) {
    this.viewContainer.clear();
    if (this.securityService.hasPrivilege(privileges)) {
      // Add template to DOM
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      // Remove template from DOM
      this.viewContainer.clear();
    }
  }

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private securityService: SecurityService) { }

}
