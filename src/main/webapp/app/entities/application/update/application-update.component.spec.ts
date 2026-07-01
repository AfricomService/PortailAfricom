jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApplicationService } from '../service/application.service';
import { IApplication, Application } from '../application.model';
import { IApplicationCategory } from 'app/entities/application-category/application-category.model';
import { ApplicationCategoryService } from 'app/entities/application-category/service/application-category.service';

import { ApplicationUpdateComponent } from './application-update.component';

describe('Application Management Update Component', () => {
  let comp: ApplicationUpdateComponent;
  let fixture: ComponentFixture<ApplicationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicationService: ApplicationService;
  let applicationCategoryService: ApplicationCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApplicationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ApplicationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicationService = TestBed.inject(ApplicationService);
    applicationCategoryService = TestBed.inject(ApplicationCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationCategory query and add missing value', () => {
      const application: IApplication = { id: 456 };
      const categorie: IApplicationCategory = { id: 24629 };
      application.categorie = categorie;

      const applicationCategoryCollection: IApplicationCategory[] = [{ id: 54683 }];
      jest.spyOn(applicationCategoryService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationCategoryCollection })));
      const additionalApplicationCategories = [categorie];
      const expectedCollection: IApplicationCategory[] = [...additionalApplicationCategories, ...applicationCategoryCollection];
      jest.spyOn(applicationCategoryService, 'addApplicationCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(applicationCategoryService.query).toHaveBeenCalled();
      expect(applicationCategoryService.addApplicationCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        applicationCategoryCollection,
        ...additionalApplicationCategories
      );
      expect(comp.applicationCategoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const application: IApplication = { id: 456 };
      const categorie: IApplicationCategory = { id: 10734 };
      application.categorie = categorie;

      activatedRoute.data = of({ application });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(application));
      expect(comp.applicationCategoriesSharedCollection).toContain(categorie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Application>>();
      const application = { id: 123 };
      jest.spyOn(applicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ application });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: application }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicationService.update).toHaveBeenCalledWith(application);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Application>>();
      const application = new Application();
      jest.spyOn(applicationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ application });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: application }));
      saveSubject.complete();

      // THEN
      expect(applicationService.create).toHaveBeenCalledWith(application);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Application>>();
      const application = { id: 123 };
      jest.spyOn(applicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ application });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicationService.update).toHaveBeenCalledWith(application);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApplicationCategoryById', () => {
      it('Should return tracked ApplicationCategory primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
