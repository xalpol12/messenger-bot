import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from "@angular/router";
import {NgbCollapse} from "@ng-bootstrap/ng-bootstrap";
import {MediaMatcher} from "@angular/cdk/layout";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink,
    RouterOutlet,
    NgbCollapse,
    NgIf,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  isMenuCollapsed = true;
  isNavbarHidden: boolean;

  private mediaMatcher: MediaQueryList;

  constructor(private cdr: ChangeDetectorRef,
              private mediaMatcherService: MediaMatcher) {
    this.mediaMatcher = this.mediaMatcherService.matchMedia('(max-width: 991px)');
    this.isNavbarHidden = this.mediaMatcher.matches;

    this.mediaMatcher.addEventListener('change', (event) => {
      this.isNavbarHidden = event.matches;
      this.cdr.detectChanges();
    })
  }
}
