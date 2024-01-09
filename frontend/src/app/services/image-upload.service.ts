import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService {
  private base = environment.API_BASE_URL;
  private imageUrl = this.base + "/api/image";

  constructor(private http: HttpClient) { }

  upload(formData: FormData): Observable<HttpEvent<any>> {
    const req = new HttpRequest('POST', this.imageUrl, formData, {
      reportProgress: true,
      responseType: 'json',
    });

    return this.http.request(req);
  }

  getImagesInfo(): Observable<any> {
    return this.http.get(`${this.imageUrl}`+'s');
  }
}
