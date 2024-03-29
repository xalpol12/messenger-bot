import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
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
  @Input() isItemSelectionActive: boolean = false;
  @Output() selectionChanged = new EventEmitter<{ itemId: string, isChecked: boolean }>();
  @ViewChild('checkboxRef', {static: false}) checkboxRef!: ElementRef;

  handleDivClick(event: any) {
    if (this.isItemSelectionActive && event.target !== this.checkboxRef.nativeElement) {
      console.log("div click");
      this.checkboxRef.nativeElement.checked = !this.checkboxRef.nativeElement.checked;
      this.onSelectionChanged();
    }
  }

  onSelectionChanged() {
    if (this.imageEntry?.imageId) {
      this.selectionChanged.emit({ itemId: this.imageEntry?.imageId, isChecked: this.checkboxRef.nativeElement.checked });
    }
  }
}
