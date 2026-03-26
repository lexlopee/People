import { Component } from '@angular/core';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, TranslateModule, RouterLink],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class Header {

  currentLang = 'es';

  constructor(private translate: TranslateService) {}

  changeLang(lang: string) {
    this.currentLang = lang;
    this.translate.use(lang);
  }
}