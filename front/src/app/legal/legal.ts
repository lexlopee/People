import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

export type LegalSection = 'terms' | 'privacy' | 'cookies' | 'transparency' | 'about';

interface LegalPage {
  titleKey: string;
  updatedKey?: string;
  introKey: string;
  sections: { titleKey: string; bodyKey: string }[];
}

@Component({
  selector: 'app-legal',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslateModule],
  templateUrl: './legal.html',
  styleUrls: ['./legal.css']
})
export class Legal implements OnInit {

  page: LegalSection = 'terms';

  readonly pages: Record<LegalSection, LegalPage> = {
    terms: {
      titleKey: 'LEGAL.TERMS_TITLE',
      updatedKey: 'LEGAL.TERMS_UPDATED',
      introKey: 'LEGAL.TERMS_INTRO',
      sections: [
        { titleKey: 'LEGAL.TERMS_S1_TITLE', bodyKey: 'LEGAL.TERMS_S1_BODY' },
        { titleKey: 'LEGAL.TERMS_S2_TITLE', bodyKey: 'LEGAL.TERMS_S2_BODY' },
        { titleKey: 'LEGAL.TERMS_S3_TITLE', bodyKey: 'LEGAL.TERMS_S3_BODY' },
        { titleKey: 'LEGAL.TERMS_S4_TITLE', bodyKey: 'LEGAL.TERMS_S4_BODY' },
        { titleKey: 'LEGAL.TERMS_S5_TITLE', bodyKey: 'LEGAL.TERMS_S5_BODY' },
        { titleKey: 'LEGAL.TERMS_S6_TITLE', bodyKey: 'LEGAL.TERMS_S6_BODY' },
        { titleKey: 'LEGAL.TERMS_S7_TITLE', bodyKey: 'LEGAL.TERMS_S7_BODY' },
        { titleKey: 'LEGAL.TERMS_S8_TITLE', bodyKey: 'LEGAL.TERMS_S8_BODY' },
        { titleKey: 'LEGAL.TERMS_S9_TITLE', bodyKey: 'LEGAL.TERMS_S9_BODY' },
      ]
    },
    privacy: {
      titleKey: 'LEGAL.PRIVACY_TITLE',
      updatedKey: 'LEGAL.PRIVACY_UPDATED',
      introKey: 'LEGAL.PRIVACY_INTRO',
      sections: [
        { titleKey: 'LEGAL.PRIVACY_S1_TITLE', bodyKey: 'LEGAL.PRIVACY_S1_BODY' },
        { titleKey: 'LEGAL.PRIVACY_S2_TITLE', bodyKey: 'LEGAL.PRIVACY_S2_BODY' },
        { titleKey: 'LEGAL.PRIVACY_S3_TITLE', bodyKey: 'LEGAL.PRIVACY_S3_BODY' },
        { titleKey: 'LEGAL.PRIVACY_S4_TITLE', bodyKey: 'LEGAL.PRIVACY_S4_BODY' },
        { titleKey: 'LEGAL.PRIVACY_S5_TITLE', bodyKey: 'LEGAL.PRIVACY_S5_BODY' },
        { titleKey: 'LEGAL.PRIVACY_S6_TITLE', bodyKey: 'LEGAL.PRIVACY_S6_BODY' },
        { titleKey: 'LEGAL.PRIVACY_S7_TITLE', bodyKey: 'LEGAL.PRIVACY_S7_BODY' },
        { titleKey: 'LEGAL.PRIVACY_S8_TITLE', bodyKey: 'LEGAL.PRIVACY_S8_BODY' },
      ]
    },
    cookies: {
      titleKey: 'LEGAL.COOKIES_TITLE',
      updatedKey: 'LEGAL.COOKIES_UPDATED',
      introKey: 'LEGAL.COOKIES_INTRO',
      sections: [
        { titleKey: 'LEGAL.COOKIES_S1_TITLE', bodyKey: 'LEGAL.COOKIES_S1_BODY' },
        { titleKey: 'LEGAL.COOKIES_S2_TITLE', bodyKey: 'LEGAL.COOKIES_S2_BODY' },
        { titleKey: 'LEGAL.COOKIES_S3_TITLE', bodyKey: 'LEGAL.COOKIES_S3_BODY' },
        { titleKey: 'LEGAL.COOKIES_S4_TITLE', bodyKey: 'LEGAL.COOKIES_S4_BODY' },
        { titleKey: 'LEGAL.COOKIES_S5_TITLE', bodyKey: 'LEGAL.COOKIES_S5_BODY' },
      ]
    },
    transparency: {
      titleKey: 'LEGAL.TRANSPARENCY_TITLE',
      introKey: 'LEGAL.TRANSPARENCY_INTRO',
      sections: [
        { titleKey: 'LEGAL.TRANSPARENCY_S1_TITLE', bodyKey: 'LEGAL.TRANSPARENCY_S1_BODY' },
        { titleKey: 'LEGAL.TRANSPARENCY_S2_TITLE', bodyKey: 'LEGAL.TRANSPARENCY_S2_BODY' },
        { titleKey: 'LEGAL.TRANSPARENCY_S3_TITLE', bodyKey: 'LEGAL.TRANSPARENCY_S3_BODY' },
        { titleKey: 'LEGAL.TRANSPARENCY_S4_TITLE', bodyKey: 'LEGAL.TRANSPARENCY_S4_BODY' },
      ]
    },
    about: {
      titleKey: 'LEGAL.ABOUT_TITLE',
      introKey: 'LEGAL.ABOUT_INTRO',
      sections: [
        { titleKey: 'LEGAL.ABOUT_S1_TITLE', bodyKey: 'LEGAL.ABOUT_S1_BODY' },
        { titleKey: 'LEGAL.ABOUT_S2_TITLE', bodyKey: 'LEGAL.ABOUT_S2_BODY' },
        { titleKey: 'LEGAL.ABOUT_S3_TITLE', bodyKey: 'LEGAL.ABOUT_S3_BODY' },
      ]
    }
  };

  get currentPage(): LegalPage {
    return this.pages[this.page];
  }

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const slug = params.get('slug') as LegalSection;
      if (slug && this.pages[slug]) {
        this.page = slug;
      }
    });
  }
}