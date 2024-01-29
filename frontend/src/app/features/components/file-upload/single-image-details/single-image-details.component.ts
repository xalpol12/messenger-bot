import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AsyncPipe, Location, NgForOf, NgIf} from "@angular/common";
import {ImageService} from "../../../services/image.service";
import {ImageInfo} from "../../../models/image.model";
import {ImageDetailsComponent} from "../image-list/image-entry/image-details/image-details.component";
import {ThumbnailComponent} from "../thumbnail/thumbnail.component";
import {UrlSeparatorPipe} from "../../../../shared/pipes/uri-separator.pipe";

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
}
