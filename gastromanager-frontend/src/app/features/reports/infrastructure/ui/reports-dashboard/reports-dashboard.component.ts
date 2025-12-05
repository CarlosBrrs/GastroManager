import {ChangeDetectionStrategy, Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';

// Interface para tarjetas de reportes
interface ReportCard {
  id: string;
  title: string;
  description: string;
  icon: string;
  route: string;
  color: string;
}

@Component({
  selector: 'gm-reports-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports-dashboard.component.html',
  styleUrl: './reports-dashboard.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReportsDashboardComponent {

  // Tarjetas de diferentes tipos de reportes
  readonly reportCards: ReportCard[] = [
    {
      id: 'sales',
      title: 'Reportes de Ventas',
      description: 'Análisis detallado de ventas, ingresos y tendencias',
      icon: '💰',
      route: '/reports/sales',
      color: 'success'
    },
    {
      id: 'inventory',
      title: 'Reportes de Inventario',
      description: 'Control de stock, productos y materias primas',
      icon: '📦',
      route: '/reports/inventory',
      color: 'warning'
    },
    {
      id: 'orders',
      title: 'Reportes de Órdenes',
      description: 'Estadísticas de órdenes, tiempos y eficiencia',
      icon: '🍽️',
      route: '/reports/orders',
      color: 'info'
    },
    {
      id: 'customers',
      title: 'Reportes de Clientes',
      description: 'Análisis de comportamiento y preferencias',
      icon: '👥',
      route: '/reports/customers',
      color: 'primary'
    },
    {
      id: 'finance',
      title: 'Reportes Financieros',
      description: 'Estados financieros, gastos e ingresos',
      icon: '📊',
      route: '/reports/finance',
      color: 'secondary'
    },
    {
      id: 'performance',
      title: 'Reportes de Desempeño',
      description: 'Métricas de rendimiento y KPIs del restaurante',
      icon: '📈',
      route: '/reports/performance',
      color: 'danger'
    }
  ];

  constructor(private router: Router) {
  }

  // Navegar a un reporte específico
  navigateToReport(reportCard: ReportCard): void {
    console.log(`Navegando a reporte: ${reportCard.title}`);
    this.router.navigate([reportCard.route]);
  }

  // Método para generar reporte rápido
  generateQuickReport(): void {
    console.log('Generando reporte rápido...');
    // TODO: Implementar lógica de reporte rápido
  }
}
