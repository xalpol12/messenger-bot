import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService {
  private baseUrl = "http://localhost:8080/api/image";

  constructor(private http: HttpClient) { }

  upload(formData: FormData): Observable<HttpEvent<any>> {
    const req = new HttpRequest('POST', this.baseUrl, formData, {
      reportProgress: true,
      responseType: 'json',
    });

    return this.http.request(req);
  }

  getImagesInfo(): Observable<any> {
    return this.http.get(`${this.baseUrl}`+'s');
  }
}
