import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApplication, getApplicationIdentifier } from '../application.model';

export type EntityResponseType = HttpResponse<IApplication>;
export type EntityArrayResponseType = HttpResponse<IApplication[]>;

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/applications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(application: IApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(application);
    return this.http
      .post<IApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(application: IApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(application);
    return this.http
      .put<IApplication>(`${this.resourceUrl}/${getApplicationIdentifier(application) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(application: IApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(application);
    return this.http
      .patch<IApplication>(`${this.resourceUrl}/${getApplicationIdentifier(application) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApplicationToCollectionIfMissing(
    applicationCollection: IApplication[],
    ...applicationsToCheck: (IApplication | null | undefined)[]
  ): IApplication[] {
    const applications: IApplication[] = applicationsToCheck.filter(isPresent);
    if (applications.length > 0) {
      const applicationCollectionIdentifiers = applicationCollection.map(applicationItem => getApplicationIdentifier(applicationItem)!);
      const applicationsToAdd = applications.filter(applicationItem => {
        const applicationIdentifier = getApplicationIdentifier(applicationItem);
        if (applicationIdentifier == null || applicationCollectionIdentifiers.includes(applicationIdentifier)) {
          return false;
        }
        applicationCollectionIdentifiers.push(applicationIdentifier);
        return true;
      });
      return [...applicationsToAdd, ...applicationCollection];
    }
    return applicationCollection;
  }

  protected convertDateFromClient(application: IApplication): IApplication {
    return Object.assign({}, application, {
      dateCreation: application.dateCreation?.isValid() ? application.dateCreation.toJSON() : undefined,
      dateActivation: application.dateActivation?.isValid() ? application.dateActivation.toJSON() : undefined,
      dateDesactivation: application.dateDesactivation?.isValid() ? application.dateDesactivation.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
      res.body.dateActivation = res.body.dateActivation ? dayjs(res.body.dateActivation) : undefined;
      res.body.dateDesactivation = res.body.dateDesactivation ? dayjs(res.body.dateDesactivation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((application: IApplication) => {
        application.dateCreation = application.dateCreation ? dayjs(application.dateCreation) : undefined;
        application.dateActivation = application.dateActivation ? dayjs(application.dateActivation) : undefined;
        application.dateDesactivation = application.dateDesactivation ? dayjs(application.dateDesactivation) : undefined;
      });
    }
    return res;
  }
}
