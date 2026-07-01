import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ApplicationCategoryService } from '../service/application-category.service';

import { ApplicationCategoryComponent } from './application-category.component';

describe('ApplicationCategory Management Component', () => {
  let comp: ApplicationCategoryComponent;
  let fixture: ComponentFixture<ApplicationCategoryComponent>;
  let service: ApplicationCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApplicationCategoryComponent],
    })
      .overrideTemplate(ApplicationCategoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApplicationCategoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApplicationCategoryService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.applicationCategories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
