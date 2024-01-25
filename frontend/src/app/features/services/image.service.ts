import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpParams, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
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

  getInfo(id: string): Observable<any> {
    const url = `${this.imageUrl}/${id}/details`;

    return this.http.get(url);
  }

  getThumbnail(id: string, dims: {width: number, height: number}):
    Observable<any> {
    const url = `${this.imageUrl}/${id}/thumbnail`;

    const options = {
      params: new HttpParams().set('width', dims.width).set('height', dims.height),
      responseType: 'blob' as 'json',
    };

    return this.http.get(url, options);
  }

  getInfos(): Observable<any> {
    return this.http.get(`${this.imageUrl}s`);
  }

  deleteSelected(imageIds: string[]) {
    const requestPayload = {
      imageIds: imageIds
    }
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
    };

    return this.http.put(`${this.imageUrl}s/batch`, requestPayload, options);
  }

  deleteAll(): Observable<any> {
    return this.http.delete(`${this.imageUrl}s`);
  }
}
