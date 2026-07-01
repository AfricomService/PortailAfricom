import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicationCategory } from '../application-category.model';
import { ApplicationCategoryService } from '../service/application-category.service';

@Component({
  templateUrl: './application-category-delete-dialog.component.html',
})
export class ApplicationCategoryDeleteDialogComponent {
  applicationCategory?: IApplicationCategory;

  constructor(protected applicationCategoryService: ApplicationCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.applicationCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
