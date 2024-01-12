import {Component, OnInit} from '@angular/core';
import {AsyncPipe, NgForOf} from "@angular/common";
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
    NgForOf
  ],
  templateUrl: './image-list.component.html',
  styleUrl: './image-list.component.css'
})
export class ImageListComponent implements OnInit {

  imageInfos?: Observable<ImageInfo[]>;

  constructor(private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.loadImagesInfo();
  }

  loadImagesInfo(): void {
    this.imageInfos = this.imageService.getImagesInfo();
  }
}
