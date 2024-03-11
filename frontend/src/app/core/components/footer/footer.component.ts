import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [],
  template: `
    <footer class="mt-3 text-center fw-lighter">
      <p>created by
        <a href="https://github.com/xalpol12"
           target="_blank"
           rel="noopener noreferrer"
           style="color: inherit; text-decoration: none"
           class="text-decoration-underline">
          xalpol12 <i class="bi-github"></i>
        </a>
      </p>
    </footer>
  `,
  styles: []
})
export class FooterComponent {
}
