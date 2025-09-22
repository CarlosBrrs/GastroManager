import { ChangeDetectionStrategy, Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

// Interfaces simples para datos de reportes
interface SimpleReport {
  id: string;
  name: string;
  date: string;
  status: string;
}

@Component({
  selector: 'gm-reports-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports-page.component.html',
  styleUrl: './reports-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReportsPageComponent {

  // Datos hardcodeados simples
  readonly totalSales = signal<number>(45280);
  readonly totalOrders = signal<number>(234);
  readonly avgOrderValue = signal<number>(193.25);

  readonly recentReports = signal<SimpleReport[]>([
    { id: 'RPT-001', name: 'Ventas Diarias', date: '2025-01-07', status: 'Completado' },
    { id: 'RPT-002', name: 'Inventario', date: '2025-01-06', status: 'Completado' },
    { id: 'RPT-003', name: 'Productos', date: '2025-01-05', status: 'Procesando' }
  ]);

  // Métodos simples
  onGenerateReport(): void {
    console.log('Generando nuevo reporte...');
  }

  onViewReport(reportId: string): void {
    console.log('Viendo reporte:', reportId);
  }
}
