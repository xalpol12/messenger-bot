import {Component, inject, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {ImageEntryComponent} from "./image-entry/image-entry.component";
import {Observable} from "rxjs";
import {ImageInfo} from "../../../models/image.model";
import {ImageService} from "../../../services/image.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-image-list',
  standalone: true,
  imports: [
    AsyncPipe,
    ImageEntryComponent,
    NgForOf,
    NgClass,
    NgIf
  ],
  templateUrl: './image-list.component.html',
  styleUrl: './image-list.component.css'
})
export class ImageListComponent implements OnInit {

  imageInfos?: Observable<ImageInfo[]>;
  selectedImageInfo: any;

  modalService = inject(NgbModal);


  constructor(private imageService: ImageService) { }

  ngOnInit(): void {
    this.loadImagesInfo();
  }

  loadImagesInfo(): void {
    this.imageInfos = this.imageService.getInfos();
  }

  deleteAllImages(): void {
    this.imageService.deleteAll()
      .subscribe({
        next: data => {
          console.log('Deleted all images successfully');
        },
        error: err => {
          console.log('There was an error!', err);
        }
      });
  }

  toggleDetails(imageInfo: any) {
    this.selectedImageInfo === imageInfo
      ? this.selectedImageInfo = null
      : this.selectedImageInfo = imageInfo;
  }

  open(content: TemplateRef<any>) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-confirm-deletion'});
  }
}
