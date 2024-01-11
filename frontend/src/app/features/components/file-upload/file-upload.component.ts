import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Observable} from "rxjs";
import {ImageUploadService} from "../../services/image-upload.service";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {ImageInfo} from "../../models/image.model";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {ImagePreviewComponent} from "./image-preview/image-preview.component";

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css'
})
export class FileUploadComponent implements OnInit {

  @ViewChild('fileInput') fileInput!: ElementRef;

  selectedFile: File | undefined;
  imageDetailsForm: FormGroup;
  progress = 0;
  message = '';

  imageInfos?: Observable<ImageInfo[]>;

  constructor(private uploadService: ImageUploadService,
              private fb: FormBuilder) {
    this.imageDetailsForm = this.fb.group({
      name: [null, [Validators.required, Validators.maxLength(80)]],
      customUri: [null, [Validators.required, this.uriValidator()]]
    })
  }

  ngOnInit(): void {
    this.loadImagesInfo();
  }

  loadImagesInfo(): void {
    this.imageInfos = this.uploadService.getImagesInfo();
  }

  selectFile(event: any): void {
    this.selectedFile = event.target.files.item(0);
  }

  cancelSelectedFile(): void {
    this.selectedFile = undefined;
    this.fileInput.nativeElement.value = '';
  }

  upload(): void {
    this.progress = 0;

    if (this.imageDetailsForm.valid && this.selectedFile) {
      const formData = new FormData();

      formData.append('file', this.selectedFile);
      formData.append('fileDetails', new Blob([JSON.stringify(this.imageDetailsForm.value)], { type: 'application/json' }));

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

          this.selectedFile = undefined;
        }
      });
    }
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
