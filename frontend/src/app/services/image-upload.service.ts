import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Image} from "../components/file-upload/file-upload.component";

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService {
  private baseUrl = "localhost:8080/api/image";

  constructor(private http: HttpClient) { }

  upload(image: Image): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('body', image.body);

    const req = new HttpRequest('POST', `${this.baseUrl}`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getImages(): Observable<any> {
    return this.http.get(`${this.baseUrl}`+'s');
  }
}
