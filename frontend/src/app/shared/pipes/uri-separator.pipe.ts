import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'uriSeparator',
  standalone: true
})
export class UriSeparatorPipe implements PipeTransform {

  /**
   * Splits given URL into two parts,
   * by default splits on the last "/" character.
   * Returns string array with left and right.
   * @param url URL to split.
   * @param splitAt Instead of splitting the last "/" character
   * splits into left and right before the passed args string.
   * If no <i>splitAt</i> is contained in URL, returns URL and empty string.
   * @example
   * let result = transform("http://localhost:8080/swagger-ui/index.html", "swagger-ui"):
   * @example
   * let defaultSplitResult = transform("http://localhost:8080/api/image/efaef");
   */
  transform(url: string, splitAt?: string): string[] {
    if (splitAt) {
      if (url.includes(splitAt)) {
        return this.splitUrl(url, splitAt);
      } else {
        return [url, ""];
      }
    } else {
      return this.splitUrl(url, "/");
    }
  }

  private splitUrl(url: string, splitAt: string): string[] {
    const lastIndex = url.lastIndexOf(splitAt);
    const leftPart = url.substring(0, lastIndex + 1);
    const rightPart = url.substring(lastIndex + 1);
    return [ leftPart, rightPart ];
  }
}
