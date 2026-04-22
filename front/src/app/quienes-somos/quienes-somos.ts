import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-quienes-somos',
  standalone: true,
  imports: [CommonModule, RouterLink, TranslateModule],
  templateUrl: './quienes-somos.html',
  styleUrls: ['./quienes-somos.css']
})
export class QuienesSomos {
  values = [
    { icon: '🤝', titleKey: 'ABOUT.VALUE_1_TITLE', descKey: 'ABOUT.VALUE_1_DESC' },
    { icon: '🔒', titleKey: 'ABOUT.VALUE_2_TITLE', descKey: 'ABOUT.VALUE_2_DESC' },
    { icon: '🌍', titleKey: 'ABOUT.VALUE_3_TITLE', descKey: 'ABOUT.VALUE_3_DESC' },
    { icon: '💡', titleKey: 'ABOUT.VALUE_4_TITLE', descKey: 'ABOUT.VALUE_4_DESC' },
  ];
}