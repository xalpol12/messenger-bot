import {Component, OnDestroy, OnInit} from '@angular/core';
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {ImageEntryComponent} from "./image-entry/image-entry.component";
import {Observable} from "rxjs";
import {ImageInfo} from "../../../models/image.model";
import {ImageService} from "../../../services/image.service";

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

  showConfirmationModal = false;

  constructor(private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.loadImagesInfo();
  }

  loadImagesInfo(): void {
    this.imageInfos = this.imageService.getImagesInfo();
  }

  deleteAllImages(): void {
    this.imageService.deleteAllImages()
      .subscribe({
        next: data => {
          console.log('Deleted all images successfully');
        },
        error: err => {
          console.log('There was an error!', err);
        }
      });
  }

  confirmDelete() {
    this.showConfirmationModal = true;
    console.log('Confirm delete dialog modal: ', this.showConfirmationModal);
  }

  closeConfirmationModal() {
    this.showConfirmationModal = false;
  }
}
