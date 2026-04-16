import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-legal',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslateModule],
  templateUrl: './legal.html',
  styleUrls: ['./legal.css']
})
export class Legal {

  termsSections = [
    { titleKey: 'LEGAL.TERMS_S1_TITLE', bodyKey: 'LEGAL.TERMS_S1_BODY' },
    { titleKey: 'LEGAL.TERMS_S2_TITLE', bodyKey: 'LEGAL.TERMS_S2_BODY' },
    { titleKey: 'LEGAL.TERMS_S3_TITLE', bodyKey: 'LEGAL.TERMS_S3_BODY' },
    { titleKey: 'LEGAL.TERMS_S4_TITLE', bodyKey: 'LEGAL.TERMS_S4_BODY' },
    { titleKey: 'LEGAL.TERMS_S5_TITLE', bodyKey: 'LEGAL.TERMS_S5_BODY' },
    { titleKey: 'LEGAL.TERMS_S6_TITLE', bodyKey: 'LEGAL.TERMS_S6_BODY' },
    { titleKey: 'LEGAL.TERMS_S7_TITLE', bodyKey: 'LEGAL.TERMS_S7_BODY' },
    { titleKey: 'LEGAL.TERMS_S8_TITLE', bodyKey: 'LEGAL.TERMS_S8_BODY' },
    { titleKey: 'LEGAL.TERMS_S9_TITLE', bodyKey: 'LEGAL.TERMS_S9_BODY' },
  ];

  privacySections = [
    { titleKey: 'LEGAL.PRIVACY_S1_TITLE', bodyKey: 'LEGAL.PRIVACY_S1_BODY' },
    { titleKey: 'LEGAL.PRIVACY_S2_TITLE', bodyKey: 'LEGAL.PRIVACY_S2_BODY' },
    { titleKey: 'LEGAL.PRIVACY_S3_TITLE', bodyKey: 'LEGAL.PRIVACY_S3_BODY' },
    { titleKey: 'LEGAL.PRIVACY_S4_TITLE', bodyKey: 'LEGAL.PRIVACY_S4_BODY' },
    { titleKey: 'LEGAL.PRIVACY_S5_TITLE', bodyKey: 'LEGAL.PRIVACY_S5_BODY' },
    { titleKey: 'LEGAL.PRIVACY_S6_TITLE', bodyKey: 'LEGAL.PRIVACY_S6_BODY' },
    { titleKey: 'LEGAL.PRIVACY_S7_TITLE', bodyKey: 'LEGAL.PRIVACY_S7_BODY' },
    { titleKey: 'LEGAL.PRIVACY_S8_TITLE', bodyKey: 'LEGAL.PRIVACY_S8_BODY' },
  ];

  cookiesSections = [
    { titleKey: 'LEGAL.COOKIES_S1_TITLE', bodyKey: 'LEGAL.COOKIES_S1_BODY' },
    { titleKey: 'LEGAL.COOKIES_S2_TITLE', bodyKey: 'LEGAL.COOKIES_S2_BODY' },
    { titleKey: 'LEGAL.COOKIES_S3_TITLE', bodyKey: 'LEGAL.COOKIES_S3_BODY' },
    { titleKey: 'LEGAL.COOKIES_S4_TITLE', bodyKey: 'LEGAL.COOKIES_S4_BODY' },
    { titleKey: 'LEGAL.COOKIES_S5_TITLE', bodyKey: 'LEGAL.COOKIES_S5_BODY' },
  ];

  transparencySections = [
    { titleKey: 'LEGAL.TRANSPARENCY_S1_TITLE', bodyKey: 'LEGAL.TRANSPARENCY_S1_BODY' },
    { titleKey: 'LEGAL.TRANSPARENCY_S2_TITLE', bodyKey: 'LEGAL.TRANSPARENCY_S2_BODY' },
    { titleKey: 'LEGAL.TRANSPARENCY_S3_TITLE', bodyKey: 'LEGAL.TRANSPARENCY_S3_BODY' },
    { titleKey: 'LEGAL.TRANSPARENCY_S4_TITLE', bodyKey: 'LEGAL.TRANSPARENCY_S4_BODY' },
  ];

  aboutSections = [
    { titleKey: 'LEGAL.ABOUT_S1_TITLE', bodyKey: 'LEGAL.ABOUT_S1_BODY' },
    { titleKey: 'LEGAL.ABOUT_S2_TITLE', bodyKey: 'LEGAL.ABOUT_S2_BODY' },
    { titleKey: 'LEGAL.ABOUT_S3_TITLE', bodyKey: 'LEGAL.ABOUT_S3_BODY' },
  ];
}