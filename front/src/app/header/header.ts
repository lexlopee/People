import { Component } from '@angular/core';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, TranslateModule],
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
