import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IApplicationCategory, ApplicationCategory } from '../application-category.model';
import { ApplicationCategoryService } from '../service/application-category.service';

@Component({
  selector: 'jhi-application-category-update',
  templateUrl: './application-category-update.component.html',
})
export class ApplicationCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    nom: [null, [Validators.required]],
    description: [],
    ordreAffichage: [],
  });

  constructor(
    protected applicationCategoryService: ApplicationCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationCategory }) => {
      this.updateForm(applicationCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationCategory = this.createFromForm();
    if (applicationCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationCategoryService.update(applicationCategory));
    } else {
      this.subscribeToSaveResponse(this.applicationCategoryService.create(applicationCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationCategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(applicationCategory: IApplicationCategory): void {
    this.editForm.patchValue({
      id: applicationCategory.id,
      code: applicationCategory.code,
      nom: applicationCategory.nom,
      description: applicationCategory.description,
      ordreAffichage: applicationCategory.ordreAffichage,
    });
  }

  protected createFromForm(): IApplicationCategory {
    return {
      ...new ApplicationCategory(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      ordreAffichage: this.editForm.get(['ordreAffichage'])!.value,
    };
  }
}
