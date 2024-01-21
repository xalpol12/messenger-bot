import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AsyncPipe, NgIf} from "@angular/common";
import {ImageService} from "../../../services/image.service";
import {ImageInfo} from "../../../models/image.model";
import {async, map, Observable} from "rxjs";
import {ImageDetailsComponent} from "../image-list/image-entry/image-details/image-details.component";

@Component({
  selector: 'app-single-image-details',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    ImageDetailsComponent
  ],
  templateUrl: './single-image-details.component.html',
  styleUrl: './single-image-details.component.css'
})
export class SingleImageDetailsComponent implements OnInit {
  id?: string;
  image?: ImageInfo;
  thumbnail?: Observable<string>;

  constructor(private route: ActivatedRoute,
              private imageService: ImageService) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    })
    this.loadInfo();
    this.loadThumbnail();
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

  loadThumbnail() {
    if (this.id) {
      this.thumbnail = this.imageService.getThumbnail(this.id, {width: 200, height: 200}).pipe(
        map(response => {
          const blob = new Blob([response], { type: 'image/jpeg' });
          return URL.createObjectURL(blob);
        })
      )
    }
  }
}
