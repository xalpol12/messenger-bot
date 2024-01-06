import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Image, ImageInfo} from "../models/image.model";

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService {
  private baseUrl = "http://localhost:8080/api/image";

  constructor(private http: HttpClient) { }

  upload(image: Image): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('file', image.body);

    const req = new HttpRequest('POST', `${this.baseUrl}`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getImagesInfo(): Observable<any> {
    console.log("Called getImages");
    return this.http.get(`${this.baseUrl}`+'s');
  }
}
