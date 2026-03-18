import { Component } from '@angular/core';
import { TranslateService, TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [TranslateModule],
  templateUrl: './header.html',
  styleUrls: ['./header.css']
})
export class Header {

  currentLang = 'es';

  constructor(private translate: TranslateService) {
    this.translate.setDefaultLang('es');
    this.translate.use('es');
  }

  changeLang(lang: string) {
    this.currentLang = lang;
    this.translate.use(lang);
  }
}
