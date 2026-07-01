import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { EtatApplication } from 'app/entities/enumerations/etat-application.model';
import { TypeAcces } from 'app/entities/enumerations/type-acces.model';
import { IApplication, Application } from '../application.model';

import { ApplicationService } from './application.service';

describe('Application Service', () => {
  let service: ApplicationService;
  let httpMock: HttpTestingController;
  let elemDefault: IApplication;
  let expectedResult: IApplication | IApplication[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApplicationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      code: 'AAAAAAA',
      nom: 'AAAAAAA',
      description: 'AAAAAAA',
      url: 'AAAAAAA',
      iconContentType: 'image/png',
      icon: 'AAAAAAA',
      couleur: 'AAAAAAA',
      ordreAffichage: 0,
      etat: EtatApplication.ACTIF,
      typeAcces: TypeAcces.PUBLIC,
      dateCreation: currentDate,
      dateActivation: currentDate,
      dateDesactivation: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateActivation: currentDate.format(DATE_TIME_FORMAT),
          dateDesactivation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Application', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateActivation: currentDate.format(DATE_TIME_FORMAT),
          dateDesactivation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateActivation: currentDate,
          dateDesactivation: currentDate,
        },
        returnedFromService
      );

      service.create(new Application()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Application', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          nom: 'BBBBBB',
          description: 'BBBBBB',
          url: 'BBBBBB',
          icon: 'BBBBBB',
          couleur: 'BBBBBB',
          ordreAffichage: 1,
          etat: 'BBBBBB',
          typeAcces: 'BBBBBB',
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateActivation: currentDate.format(DATE_TIME_FORMAT),
          dateDesactivation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateActivation: currentDate,
          dateDesactivation: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Application', () => {
      const patchObject = Object.assign(
        {
          couleur: 'BBBBBB',
          typeAcces: 'BBBBBB',
          dateDesactivation: currentDate.format(DATE_TIME_FORMAT),
        },
        new Application()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateActivation: currentDate,
          dateDesactivation: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Application', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          nom: 'BBBBBB',
          description: 'BBBBBB',
          url: 'BBBBBB',
          icon: 'BBBBBB',
          couleur: 'BBBBBB',
          ordreAffichage: 1,
          etat: 'BBBBBB',
          typeAcces: 'BBBBBB',
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateActivation: currentDate.format(DATE_TIME_FORMAT),
          dateDesactivation: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateActivation: currentDate,
          dateDesactivation: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Application', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApplicationToCollectionIfMissing', () => {
      it('should add a Application to an empty array', () => {
        const application: IApplication = { id: 123 };
        expectedResult = service.addApplicationToCollectionIfMissing([], application);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(application);
      });

      it('should not add a Application to an array that contains it', () => {
        const application: IApplication = { id: 123 };
        const applicationCollection: IApplication[] = [
          {
            ...application,
          },
          { id: 456 },
        ];
        expectedResult = service.addApplicationToCollectionIfMissing(applicationCollection, application);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Application to an array that doesn't contain it", () => {
        const application: IApplication = { id: 123 };
        const applicationCollection: IApplication[] = [{ id: 456 }];
        expectedResult = service.addApplicationToCollectionIfMissing(applicationCollection, application);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(application);
      });

      it('should add only unique Application to an array', () => {
        const applicationArray: IApplication[] = [{ id: 123 }, { id: 456 }, { id: 92241 }];
        const applicationCollection: IApplication[] = [{ id: 123 }];
        expectedResult = service.addApplicationToCollectionIfMissing(applicationCollection, ...applicationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const application: IApplication = { id: 123 };
        const application2: IApplication = { id: 456 };
        expectedResult = service.addApplicationToCollectionIfMissing([], application, application2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(application);
        expect(expectedResult).toContain(application2);
      });

      it('should accept null and undefined values', () => {
        const application: IApplication = { id: 123 };
        expectedResult = service.addApplicationToCollectionIfMissing([], null, application, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(application);
      });

      it('should return initial array if no Application is added', () => {
        const applicationCollection: IApplication[] = [{ id: 123 }];
        expectedResult = service.addApplicationToCollectionIfMissing(applicationCollection, undefined, null);
        expect(expectedResult).toEqual(applicationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
