import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {ImageUploadService} from "../../services/image-upload.service";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {ImageFormData, ImageInfo} from "../../models/image.model";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css'
})
export class FileUploadComponent implements OnInit {

  selectedImages?: FileList;
  currentFile?: File;
  imageDetailsForm: FormGroup;
  progress = 0;
  message = '';

  imageInfos?: Observable<ImageInfo[]>;

  constructor(private uploadService: ImageUploadService,
              private fb: FormBuilder) {
    this.imageDetailsForm = this.fb.group({
      imageName: [null, [Validators.required, Validators.maxLength(80)]],
      imageUrl: [null, [Validators.required, this.uriValidator()]]
    })
  }

  ngOnInit(): void {
    this.loadImagesInfo();
  }

  loadImagesInfo(): void {
    this.imageInfos = this.uploadService.getImagesInfo();
  }

  selectFile(event: any): void {
    this.selectedImages = event.target.files;
  }

  upload(): void {
    this.progress = 0;

    if (this.selectedImages && this.imageDetailsForm.valid) {
      const file: File | null = this.selectedImages.item(0);

      if (file) {
        this.currentFile = file;
        const formData = new FormData();

        formData.append('file', file);
        formData.append('fileDetails', new Blob([JSON.stringify(this.imageDetailsForm.value)], { type: 'application/json'}));

        this.uploadService.upload(formData).subscribe({
          next: (event: any) => {
            if (event.type === HttpEventType.UploadProgress) {
              this.progress = Math.round(100 * event.loaded / event.total);
            } else if (event instanceof HttpResponse) {
              this.message = event.body.message;
              this.imageInfos = this.uploadService.getImagesInfo();
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

  uriValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const isValid = /^([a-z0-9]+-)*[a-z0-9]+$/.test(control.value);
      return isValid ? null : { uriValidation: true };
    }
  }

  get isImageNameValid(): boolean {
    const imageNameControl = this.imageDetailsForm.get('imageName');
    if (imageNameControl === null || imageNameControl.value === "") {
      return false;
    } else return imageNameControl?.hasError('maxlength') && imageNameControl?.touched;
  }

  get isImageUrlValid(): boolean {
    const imageUrlControl = this.imageDetailsForm.get('imageUrl');
    if (imageUrlControl === null || imageUrlControl.value === "") {
      return false;
    } else return imageUrlControl.hasError('uriValidation') && imageUrlControl.touched;
  }
}
