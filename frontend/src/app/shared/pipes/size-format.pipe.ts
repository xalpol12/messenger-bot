import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sizeFormat',
  standalone: true
})
export class SizeFormatPipe implements PipeTransform {

 static units = ['B', 'KB', 'MB', 'GB', 'TB'];

  transform(value: number | undefined): string {
    if (value === null || value === undefined || isNaN(value)) return '';

    let size = value;
    let unitIndex = 0;

    while (size >= 1024) {
      size /= 1024;
      unitIndex++;
    }
    return `${size.toFixed(2)} ${(SizeFormatPipe.units)[unitIndex]}`;
  }
}
