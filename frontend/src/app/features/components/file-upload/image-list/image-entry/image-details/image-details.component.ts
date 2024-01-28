import {Component, Input} from '@angular/core';
import {ImageInfo} from "../../../../../models/image.model";
import {DatePipe} from "@angular/common";
import {SizeFormatPipe} from "../../../../../../shared/pipes/size-format.pipe";
import {ThumbnailComponent} from "../../../thumbnail/thumbnail.component";

@Component({
  selector: 'app-image-details',
  standalone: true,
    imports: [
        DatePipe,
        SizeFormatPipe,
        ThumbnailComponent
    ],
  templateUrl: './image-details.component.html',
  styleUrl: './image-details.component.css'
})
export class ImageDetailsComponent {
  @Input() imageEntry: ImageInfo | undefined;
}
