import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {FileUploadComponent} from "./features/components/file-upload/file-upload.component";
import {BrowserModule} from "@angular/platform-browser";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ImageEntryComponent} from "./features/components/file-upload/image-list/image-entry/image-entry.component";
import {NgOptimizedImage} from "@angular/common";
import {NavbarComponent} from "./core/components/navbar/navbar.component";
import {RouterModule} from "@angular/router";
import {routes} from "./app.routes";
import {ImagePreviewComponent} from "./features/components/file-upload/image-preview/image-preview.component";
import {ImageListComponent} from "./features/components/file-upload/image-list/image-list.component";
import {ImageInputComponent} from "./features/components/file-upload/image-input/image-input.component";

@NgModule({
  declarations: [
    AppComponent,
    FileUploadComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    ImageEntryComponent,
    NgOptimizedImage,
    NavbarComponent,
    RouterModule.forRoot(routes),
    ImagePreviewComponent,
    ImageListComponent,
    ImageInputComponent
  ],
  exports: [RouterModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
