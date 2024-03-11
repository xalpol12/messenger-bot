import { Injectable } from '@angular/core';
import {Observable, Observer} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ImageReaderService {

  fileReader: FileReader = new FileReader();

  constructor() {}

  readImage(file: File): Observable<string> {
    return new Observable((observer: Observer<string>) => {

      this.fileReader.onload = (e) =>  {
        const dataUrl = e.target?.result as string;
        observer.next(dataUrl);
        observer.complete();
      };

      this.fileReader.onerror = (error) => {
        observer.error(error);
      };

      this.fileReader.readAsDataURL(file);
    })
  }

}
