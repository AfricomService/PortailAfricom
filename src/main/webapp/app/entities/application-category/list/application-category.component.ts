import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApplicationCategory } from '../application-category.model';
import { ApplicationCategoryService } from '../service/application-category.service';
import { ApplicationCategoryDeleteDialogComponent } from '../delete/application-category-delete-dialog.component';

@Component({
  selector: 'jhi-application-category',
  templateUrl: './application-category.component.html',
})
export class ApplicationCategoryComponent implements OnInit {
  applicationCategories?: IApplicationCategory[];
  isLoading = false;

  constructor(protected applicationCategoryService: ApplicationCategoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.applicationCategoryService.query().subscribe(
      (res: HttpResponse<IApplicationCategory[]>) => {
        this.isLoading = false;
        this.applicationCategories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IApplicationCategory): number {
    return item.id!;
  }

  delete(applicationCategory: IApplicationCategory): void {
    const modalRef = this.modalService.open(ApplicationCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.applicationCategory = applicationCategory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
