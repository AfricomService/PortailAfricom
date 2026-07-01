import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'application-category',
        data: { pageTitle: 'portailApp.applicationCategory.home.title' },
        loadChildren: () => import('./application-category/application-category.module').then(m => m.ApplicationCategoryModule),
      },
      {
        path: 'application',
        data: { pageTitle: 'portailApp.application.home.title' },
        loadChildren: () => import('./application/application.module').then(m => m.ApplicationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
