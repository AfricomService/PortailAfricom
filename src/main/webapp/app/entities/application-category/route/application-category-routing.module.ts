import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApplicationCategoryComponent } from '../list/application-category.component';
import { ApplicationCategoryDetailComponent } from '../detail/application-category-detail.component';
import { ApplicationCategoryUpdateComponent } from '../update/application-category-update.component';
import { ApplicationCategoryRoutingResolveService } from './application-category-routing-resolve.service';

const applicationCategoryRoute: Routes = [
  {
    path: '',
    component: ApplicationCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApplicationCategoryDetailComponent,
    resolve: {
      applicationCategory: ApplicationCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApplicationCategoryUpdateComponent,
    resolve: {
      applicationCategory: ApplicationCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApplicationCategoryUpdateComponent,
    resolve: {
      applicationCategory: ApplicationCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(applicationCategoryRoute)],
  exports: [RouterModule],
})
export class ApplicationCategoryRoutingModule {}
