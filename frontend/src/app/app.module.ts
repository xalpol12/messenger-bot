import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {FileUploadComponent} from "./components/file-upload/file-upload.component";
import {BrowserModule} from "@angular/platform-browser";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ImageEntryComponent} from "./components/file-upload/image-entry/image-entry.component";
import {NgOptimizedImage} from "@angular/common";
import {NavbarComponent} from "./components/navbar/navbar.component";

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
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
