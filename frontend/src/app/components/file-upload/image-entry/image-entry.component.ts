import {Component, Input, OnInit} from '@angular/core';
import {ImageInfo} from "../../../models/image.model";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-image-entry',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './image-entry.component.html',
  styleUrl: './image-entry.component.css'
})
export class ImageEntryComponent {
  @Input() imageEntry: ImageInfo | undefined;
}
