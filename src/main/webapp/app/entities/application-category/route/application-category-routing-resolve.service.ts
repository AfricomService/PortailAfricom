import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApplicationCategory, ApplicationCategory } from '../application-category.model';
import { ApplicationCategoryService } from '../service/application-category.service';

@Injectable({ providedIn: 'root' })
export class ApplicationCategoryRoutingResolveService implements Resolve<IApplicationCategory> {
  constructor(protected service: ApplicationCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApplicationCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((applicationCategory: HttpResponse<ApplicationCategory>) => {
          if (applicationCategory.body) {
            return of(applicationCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApplicationCategory());
  }
}
