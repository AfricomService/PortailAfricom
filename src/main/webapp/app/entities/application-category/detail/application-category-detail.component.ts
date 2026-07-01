import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApplicationCategory } from '../application-category.model';

@Component({
  selector: 'jhi-application-category-detail',
  templateUrl: './application-category-detail.component.html',
})
export class ApplicationCategoryDetailComponent implements OnInit {
  applicationCategory: IApplicationCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationCategory }) => {
      this.applicationCategory = applicationCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
