import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {ImageUploadService} from "../../services/image-upload.service";
import {HttpEventType, HttpResponse} from "@angular/common/http";

export interface Image {
  body: File,
}

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css'
})
export class FileUploadComponent implements OnInit {

  selectedImages?: FileList;
  currentFile?: File;
  progress = 0;
  message = '';

  imageInfos?: Observable<any>;

  constructor(private uploadService: ImageUploadService) {
  }

  ngOnInit(): void {
    this.imageInfos = this.uploadService.getImages();
  }

  selectFile(event: any): void {
    this.selectedImages = event.target.files;
  }

  upload(): void {
    this.progress = 0;

    if (this.selectedImages) {
      const file: File | null = this.selectedImages.item(0);

      if (file) {
        this.currentFile = file;

        const image: Image = {body: file};

        this.uploadService.upload(image).subscribe({
          next: (event: any) => {
            if (event.type === HttpEventType.UploadProgress) {
              this.progress = Math.round(100 * event.loaded / event.total);
            } else if (event instanceof HttpResponse) {
              this.message = event.body.message;
              this.imageInfos = this.uploadService.getImages();
            }
          },
          error: (err: any) => {
            console.log(err);
            this.progress = 0;

            if (err.error && err.error.message) {
              this.message = err.error.message;
            } else {
              this.message = 'Could not upload file!';
            }

            this.currentFile = undefined;
          }
        });
      }
    }

    this.selectedImages = undefined;
  }

}
