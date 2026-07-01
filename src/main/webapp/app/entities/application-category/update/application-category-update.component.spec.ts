jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ApplicationCategoryService } from '../service/application-category.service';
import { IApplicationCategory, ApplicationCategory } from '../application-category.model';

import { ApplicationCategoryUpdateComponent } from './application-category-update.component';

describe('ApplicationCategory Management Update Component', () => {
  let comp: ApplicationCategoryUpdateComponent;
  let fixture: ComponentFixture<ApplicationCategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let applicationCategoryService: ApplicationCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApplicationCategoryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ApplicationCategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationCategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    applicationCategoryService = TestBed.inject(ApplicationCategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const applicationCategory: IApplicationCategory = { id: 456 };

      activatedRoute.data = of({ applicationCategory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(applicationCategory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationCategory>>();
      const applicationCategory = { id: 123 };
      jest.spyOn(applicationCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationCategory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(applicationCategoryService.update).toHaveBeenCalledWith(applicationCategory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationCategory>>();
      const applicationCategory = new ApplicationCategory();
      jest.spyOn(applicationCategoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: applicationCategory }));
      saveSubject.complete();

      // THEN
      expect(applicationCategoryService.create).toHaveBeenCalledWith(applicationCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ApplicationCategory>>();
      const applicationCategory = { id: 123 };
      jest.spyOn(applicationCategoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ applicationCategory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(applicationCategoryService.update).toHaveBeenCalledWith(applicationCategory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
