import { Routes } from '@angular/router';
import {SchedulerComponent} from "./features/components/scheduler/scheduler.component";
import {FileUploadComponent} from "./features/components/file-upload/file-upload.component";
import {HomePageComponent} from "./core/components/home-page/home-page.component";
import {PageNotFoundComponent} from "./core/components/page-not-found/page-not-found.component";

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponent,
    title: 'Home'
  },
  {
    path: 'images',
    component: FileUploadComponent,
    title: 'File Upload',
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
