import {Component, Input, OnInit} from '@angular/core';
import {async, map, Observable} from "rxjs";
import {ImageService} from "../../../services/image.service";
import {AsyncPipe, NgIf} from "@angular/common";

@Component({
  selector: 'app-thumbnail',
  standalone: true,
  imports: [
    AsyncPipe,
    NgIf
  ],
  templateUrl: './thumbnail.component.html',
  styleUrl: './thumbnail.component.css'
})
export class ThumbnailComponent implements OnInit {

  @Input() imageId: string | undefined;
  thumbnail?: Observable<string>;

  constructor(private imageService: ImageService) {}

  ngOnInit(): void {
    this.loadThumbnail();
  }

  loadThumbnail() {
    if (this.imageId) {
      this.thumbnail = this.imageService.getThumbnail(this.imageId, {width: 200, height: 200}).pipe(
        map(response => {
          const blob = new Blob([response], { type: 'image/jpeg' });
          return URL.createObjectURL(blob);
        })
      )
    }
  }

}
