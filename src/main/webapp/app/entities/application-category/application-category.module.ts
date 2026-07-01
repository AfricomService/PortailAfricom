import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApplicationCategoryComponent } from './list/application-category.component';
import { ApplicationCategoryDetailComponent } from './detail/application-category-detail.component';
import { ApplicationCategoryUpdateComponent } from './update/application-category-update.component';
import { ApplicationCategoryDeleteDialogComponent } from './delete/application-category-delete-dialog.component';
import { ApplicationCategoryRoutingModule } from './route/application-category-routing.module';

@NgModule({
  imports: [SharedModule, ApplicationCategoryRoutingModule],
  declarations: [
    ApplicationCategoryComponent,
    ApplicationCategoryDetailComponent,
    ApplicationCategoryUpdateComponent,
    ApplicationCategoryDeleteDialogComponent,
  ],
  entryComponents: [ApplicationCategoryDeleteDialogComponent],
})
export class ApplicationCategoryModule {}
