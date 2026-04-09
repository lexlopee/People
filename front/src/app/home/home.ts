import { Component } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, TranslateModule, RouterLink],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home {
  stats = [
    { key: 'HOME.STAT_1_NUMBER', label: 'HOME.STAT_1_LABEL' },
    { key: 'HOME.STAT_2_NUMBER', label: 'HOME.STAT_2_LABEL' },
    { key: 'HOME.STAT_3_NUMBER', label: 'HOME.STAT_3_LABEL' },
  ];

  steps = [
    { icon: '✏️', title: 'HOME.STEP_1_TITLE', desc: 'HOME.STEP_1_DESC' },
    { icon: '📢', title: 'HOME.STEP_2_TITLE', desc: 'HOME.STEP_2_DESC' },
    { icon: '💜', title: 'HOME.STEP_3_TITLE', desc: 'HOME.STEP_3_DESC' },
  ];

  values = [
    { icon: '🤝', title: 'HOME.VALUE_1_TITLE', desc: 'HOME.VALUE_1_DESC' },
    { icon: '🔒', title: 'HOME.VALUE_2_TITLE', desc: 'HOME.VALUE_2_DESC' },
    { icon: '🌍', title: 'HOME.VALUE_3_TITLE', desc: 'HOME.VALUE_3_DESC' },
    { icon: '💡', title: 'HOME.VALUE_4_TITLE', desc: 'HOME.VALUE_4_DESC' },
  ];
}