import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApplicationCategory, getApplicationCategoryIdentifier } from '../application-category.model';

export type EntityResponseType = HttpResponse<IApplicationCategory>;
export type EntityArrayResponseType = HttpResponse<IApplicationCategory[]>;

@Injectable({ providedIn: 'root' })
export class ApplicationCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/application-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(applicationCategory: IApplicationCategory): Observable<EntityResponseType> {
    return this.http.post<IApplicationCategory>(this.resourceUrl, applicationCategory, { observe: 'response' });
  }

  update(applicationCategory: IApplicationCategory): Observable<EntityResponseType> {
    return this.http.put<IApplicationCategory>(
      `${this.resourceUrl}/${getApplicationCategoryIdentifier(applicationCategory) as number}`,
      applicationCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(applicationCategory: IApplicationCategory): Observable<EntityResponseType> {
    return this.http.patch<IApplicationCategory>(
      `${this.resourceUrl}/${getApplicationCategoryIdentifier(applicationCategory) as number}`,
      applicationCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApplicationCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApplicationCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApplicationCategoryToCollectionIfMissing(
    applicationCategoryCollection: IApplicationCategory[],
    ...applicationCategoriesToCheck: (IApplicationCategory | null | undefined)[]
  ): IApplicationCategory[] {
    const applicationCategories: IApplicationCategory[] = applicationCategoriesToCheck.filter(isPresent);
    if (applicationCategories.length > 0) {
      const applicationCategoryCollectionIdentifiers = applicationCategoryCollection.map(
        applicationCategoryItem => getApplicationCategoryIdentifier(applicationCategoryItem)!
      );
      const applicationCategoriesToAdd = applicationCategories.filter(applicationCategoryItem => {
        const applicationCategoryIdentifier = getApplicationCategoryIdentifier(applicationCategoryItem);
        if (applicationCategoryIdentifier == null || applicationCategoryCollectionIdentifiers.includes(applicationCategoryIdentifier)) {
          return false;
        }
        applicationCategoryCollectionIdentifiers.push(applicationCategoryIdentifier);
        return true;
      });
      return [...applicationCategoriesToAdd, ...applicationCategoryCollection];
    }
    return applicationCategoryCollection;
  }
}
