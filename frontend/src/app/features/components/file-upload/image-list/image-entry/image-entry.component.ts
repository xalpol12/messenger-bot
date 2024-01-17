import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ImageInfo} from "../../../../models/image.model";
import {NgIf} from "@angular/common";
import {NgbCollapse} from "@ng-bootstrap/ng-bootstrap";
import {ImageDetailsComponent} from "./image-details/image-details.component";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-image-entry',
  standalone: true,
  imports: [
    NgIf,
    NgbCollapse,
    ImageDetailsComponent,
    RouterLink,
    RouterOutlet
  ],
  templateUrl: './image-entry.component.html',
  styleUrl: './image-entry.component.css'
})
export class ImageEntryComponent {
  @Input() imageEntry: ImageInfo | undefined;
  @Input() isExpanded: boolean = false;
}
