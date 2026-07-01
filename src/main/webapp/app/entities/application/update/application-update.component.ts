import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IApplication, Application } from '../application.model';
import { ApplicationService } from '../service/application.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationCategory } from 'app/entities/application-category/application-category.model';
import { ApplicationCategoryService } from 'app/entities/application-category/service/application-category.service';
import { EtatApplication } from 'app/entities/enumerations/etat-application.model';
import { TypeAcces } from 'app/entities/enumerations/type-acces.model';

@Component({
  selector: 'jhi-application-update',
  templateUrl: './application-update.component.html',
})
export class ApplicationUpdateComponent implements OnInit {
  isSaving = false;
  etatApplicationValues = Object.keys(EtatApplication);
  typeAccesValues = Object.keys(TypeAcces);

  applicationCategoriesSharedCollection: IApplicationCategory[] = [];

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    nom: [null, [Validators.required]],
    description: [],
    url: [],
    icon: [],
    iconContentType: [],
    couleur: [],
    ordreAffichage: [],
    etat: [null, [Validators.required]],
    typeAcces: [null, [Validators.required]],
    dateCreation: [],
    dateActivation: [],
    dateDesactivation: [],
    categorie: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected applicationService: ApplicationService,
    protected applicationCategoryService: ApplicationCategoryService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ application }) => {
      if (application.id === undefined) {
        const today = dayjs().startOf('day');
        application.dateCreation = today;
        application.dateActivation = today;
        application.dateDesactivation = today;
      }

      this.updateForm(application);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('portailApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const application = this.createFromForm();
    if (application.id !== undefined) {
      this.subscribeToSaveResponse(this.applicationService.update(application));
    } else {
      this.subscribeToSaveResponse(this.applicationService.create(application));
    }
  }

  trackApplicationCategoryById(index: number, item: IApplicationCategory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplication>>): void {
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

  protected updateForm(application: IApplication): void {
    this.editForm.patchValue({
      id: application.id,
      code: application.code,
      nom: application.nom,
      description: application.description,
      url: application.url,
      icon: application.icon,
      iconContentType: application.iconContentType,
      couleur: application.couleur,
      ordreAffichage: application.ordreAffichage,
      etat: application.etat,
      typeAcces: application.typeAcces,
      dateCreation: application.dateCreation ? application.dateCreation.format(DATE_TIME_FORMAT) : null,
      dateActivation: application.dateActivation ? application.dateActivation.format(DATE_TIME_FORMAT) : null,
      dateDesactivation: application.dateDesactivation ? application.dateDesactivation.format(DATE_TIME_FORMAT) : null,
      categorie: application.categorie,
    });

    this.applicationCategoriesSharedCollection = this.applicationCategoryService.addApplicationCategoryToCollectionIfMissing(
      this.applicationCategoriesSharedCollection,
      application.categorie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationCategoryService
      .query()
      .pipe(map((res: HttpResponse<IApplicationCategory[]>) => res.body ?? []))
      .pipe(
        map((applicationCategories: IApplicationCategory[]) =>
          this.applicationCategoryService.addApplicationCategoryToCollectionIfMissing(
            applicationCategories,
            this.editForm.get('categorie')!.value
          )
        )
      )
      .subscribe((applicationCategories: IApplicationCategory[]) => (this.applicationCategoriesSharedCollection = applicationCategories));
  }

  protected createFromForm(): IApplication {
    return {
      ...new Application(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      url: this.editForm.get(['url'])!.value,
      iconContentType: this.editForm.get(['iconContentType'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      couleur: this.editForm.get(['couleur'])!.value,
      ordreAffichage: this.editForm.get(['ordreAffichage'])!.value,
      etat: this.editForm.get(['etat'])!.value,
      typeAcces: this.editForm.get(['typeAcces'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value
        ? dayjs(this.editForm.get(['dateCreation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateActivation: this.editForm.get(['dateActivation'])!.value
        ? dayjs(this.editForm.get(['dateActivation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateDesactivation: this.editForm.get(['dateDesactivation'])!.value
        ? dayjs(this.editForm.get(['dateDesactivation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      categorie: this.editForm.get(['categorie'])!.value,
    };
  }
}
