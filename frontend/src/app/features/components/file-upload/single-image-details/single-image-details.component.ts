import {Component, inject, OnInit, TemplateRef} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AsyncPipe, Location, NgForOf, NgIf} from "@angular/common";
import {ImageService} from "../../../services/image.service";
import {ImageInfo} from "../../../models/image.model";
import {ImageDetailsComponent} from "../image-list/image-entry/image-details/image-details.component";
import {ThumbnailComponent} from "../thumbnail/thumbnail.component";
import {UrlSeparatorPipe} from "../../../../shared/pipes/uri-separator.pipe";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-single-image-details',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    ImageDetailsComponent,
    ThumbnailComponent,
    UrlSeparatorPipe,
    NgForOf
  ],
  templateUrl: './single-image-details.component.html',
  styleUrl: './single-image-details.component.css'
})
export class SingleImageDetailsComponent implements OnInit {
  id?: string;
  image?: ImageInfo;
  modalService = inject(NgbModal);
  isEditModeActive: boolean = false;

  constructor(private route: ActivatedRoute,
              private location: Location,
              private imageService: ImageService) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    })
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
    this.toggleEditMode(false);
  }
}
