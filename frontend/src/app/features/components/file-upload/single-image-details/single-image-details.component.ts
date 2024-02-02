import {Component, inject, OnInit, TemplateRef} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AsyncPipe, Location, NgForOf, NgIf, NgStyle} from "@angular/common";
import {ImageService} from "../../../services/image.service";
import {ImageInfo} from "../../../models/image.model";
import {ImageDetailsComponent} from "../image-list/image-entry/image-details/image-details.component";
import {ThumbnailComponent} from "../thumbnail/thumbnail.component";
import {UrlSeparatorPipe} from "../../../../shared/pipes/uri-separator.pipe";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ImageFormDetailsService} from "../../../services/image-form-details.service";
import {FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-single-image-details',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    ImageDetailsComponent,
    ThumbnailComponent,
    UrlSeparatorPipe,
    NgForOf,
    FormsModule,
    ReactiveFormsModule,
    NgStyle
  ],
  templateUrl: './single-image-details.component.html',
  styleUrl: './single-image-details.component.css'
})
export class SingleImageDetailsComponent implements OnInit {
  id?: string;
  image?: ImageInfo;
  modalService = inject(NgbModal);
  isEditModeActive: boolean = false;
  imageDetailsForm!: FormGroup;

  constructor(private route: ActivatedRoute,
              private location: Location,
              private imageService: ImageService,
              private imageFormService: ImageFormDetailsService) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    })
    this.imageDetailsForm = this.imageFormService.createImageDetailsForm();
    this.loadInfo();
  }

  loadInfo() {
    if (this.id) {
      this.imageService.getInfo(this.id).subscribe({
        next: response => {
          this.image = response;
        },
        error: err => {
          console.log(err);
        }
      });
    }
  }

  deleteImage() {
    if (this.id) {
      this.imageService.deleteSingle(this.id).subscribe({
        next: () => {
          this.goBack();
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
  }

  goBack() {
    this.location.back();
  }

  openModal(content: TemplateRef<any>) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-confirm-deletion'});
  }

  toggleEditMode(state: boolean) {
    this.isEditModeActive = state;
  }

  saveEditDetails() {
    if (this.imageDetailsForm.valid) {
      console.log(this.imageDetailsForm.valid);
      // switch http methods depending on provided update values (image, details, image and details)
      this.patchDetails();
    }
  }

  patchDetails() {
    if (this.id) {
      this.imageService.patchDetails(this.id, JSON.stringify(this.imageDetailsForm.value))
        .subscribe({
          next: () => {
            console.log("image service patch details called");
            this.toggleEditMode(false);
            this.loadInfo();
          },
          error: err => {
            console.log(err);
          }
        })
    }
  }

  get isImageNameValid(): boolean {
    const imageNameControl = this.imageDetailsForm.get('name');
    if (imageNameControl === null || imageNameControl.value === "") {
      return false;
    } else return imageNameControl?.hasError('maxlength') && imageNameControl?.touched;
  }

  get isImageUrlValid(): boolean {
    const imageUrlControl = this.imageDetailsForm.get('customUri');
    if (imageUrlControl === null || imageUrlControl.value === "") {
      return false;
    } else return imageUrlControl.hasError('uriValidation') && imageUrlControl.touched;
  }
}
