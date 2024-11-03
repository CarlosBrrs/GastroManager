import {Component, Input, OnInit} from '@angular/core';
import {Metric} from "../home.component";
import {CardModule} from "primeng/card";
import {NgClass} from "@angular/common";

@Component({
  selector: 'gm-key-metrics',
  standalone: true,
  imports: [
    CardModule,
    NgClass
  ],
  templateUrl: './key-metrics.component.html',
  styleUrl: './key-metrics.component.scss'
})
export class KeyMetricsComponent implements OnInit {
  @Input() metrics: Metric[] = [];

  ngOnInit(): void {

  }

  getTrendSymbol(trend: "up" | "down" | "neutral" | undefined) {
    switch (trend) {
      case 'up':
        return '↑';
      case 'down':
        return '↓';
      case 'neutral':
        return '→';
      default:
        return '';
    }
  }
}
