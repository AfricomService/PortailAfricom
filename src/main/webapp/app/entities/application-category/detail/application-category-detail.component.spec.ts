import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApplicationCategoryDetailComponent } from './application-category-detail.component';

describe('ApplicationCategory Management Detail Component', () => {
  let comp: ApplicationCategoryDetailComponent;
  let fixture: ComponentFixture<ApplicationCategoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ApplicationCategoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ applicationCategory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ApplicationCategoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ApplicationCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load applicationCategory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.applicationCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
