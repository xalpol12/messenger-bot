import { Routes } from '@angular/router';
import {AppComponent} from "./app.component";
import {SchedulerComponent} from "./components/scheduler/scheduler.component";
import {FileUploadComponent} from "./components/file-upload/file-upload.component";

export const routes: Routes = [
  {
    path: 'images',
    component: FileUploadComponent,
    title: 'File Upload'
  },
  {
    path: 'scheduler',
    component: SchedulerComponent,
    title: 'Scheduler'
  },
];
