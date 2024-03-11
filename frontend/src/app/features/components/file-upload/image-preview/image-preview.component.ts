import {Component, Input} from '@angular/core';
import {ImageReaderService} from "../../../../shared/services/image-reader.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-image-preview',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './image-preview.component.html',
  styleUrl: './image-preview.component.css'
})
export class ImagePreviewComponent {
  private _selectedFile: File | undefined;
  selectedFilePreview: string | undefined;

  constructor(private imageReader: ImageReaderService) { }

  @Input()
  set selectedFile(value: File | undefined) {
    if (value !== undefined) {
      this._selectedFile = value;
      this.loadPreview();
    }
    if (this._selectedFile !== undefined && value === undefined) {
      this.removePreview();
    }
  }

  loadPreview() {
    if (this._selectedFile != undefined) {
    this.imageReader.readImage(this._selectedFile)
      .subscribe({
        next: (imageAsString) => {
          this.selectedFilePreview = imageAsString;
        },
        error: (error) => {
          console.error('Error reading image:', error);
        },
      });
    }
  }

  removePreview() {
    this.selectedFilePreview = undefined;
  }
}
