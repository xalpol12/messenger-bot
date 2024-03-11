import { Routes } from '@angular/router';
import {SchedulerComponent} from "./features/components/scheduler/scheduler.component";
import {ImageUploadComponent} from "./features/components/file-upload/image-upload.component";
import {HomePageComponent} from "./core/components/home-page/home-page.component";
import {PageNotFoundComponent} from "./core/components/page-not-found/page-not-found.component";
import {
  SingleImageDetailsComponent
} from "./features/components/file-upload/single-image-details/single-image-details.component";

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponent,
    title: 'Home'
  },
  {
    path: 'images',
    component: ImageUploadComponent,
    title: 'Image Upload',
  },
  {
    path: 'image/details/:id',
    component: SingleImageDetailsComponent,
    title: 'Image details'
  },
  {
    path: 'scheduler',
    component: SchedulerComponent,
    title: 'Scheduler'
  },
  {
    path: '**',
    component: PageNotFoundComponent,
    title: 'Page not found!'
  },
];
