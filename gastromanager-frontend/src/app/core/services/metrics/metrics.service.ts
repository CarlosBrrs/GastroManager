import {Injectable} from '@angular/core';
import {Metric} from "../../../pages/home/home.component";

@Injectable({
  providedIn: 'root'
})
export class MetricsService {

  constructor() {
  }

  getSummaryMetrics(role: string): Metric[] {
    switch (role) {
      case 'ROLE_OWNER':
        return [
          {
            label: 'Revenue',
            value: '$12,500',
            icon: 'pi pi-chart-line',
            trend: 'up',
            tooltip: 'Total revenue for this month',
            color: 'text-success'
          },
          {
            label: 'Orders Today',
            value: 250,
            icon: 'pi pi-shopping-cart',
            trend: 'neutral',
            tooltip: 'Number of orders processed today',
            color: 'text-info'
          },
          {
            label: 'Active Staff',
            value: 15,
            icon: 'pi pi-users',
            trend: 'down',
            tooltip: 'Total active staff on shift',
            color: 'text-warning'
          },
          {
            label: 'Profit Margin',
            value: '25%',
            icon: 'pi pi-percentage',
            trend: 'up',
            tooltip: 'Monthly profit margin',
            color: 'text-success'
          },
          {
            label: 'Customer Feedback',
            value: '4.5/5',
            icon: 'pi pi-star',
            trend: 'up',
            tooltip: 'Average customer feedback rating',
            color: 'text-success'
          },
        ];

      case 'ROLE_MANAGER':
        return [
          {
            label: 'Orders Today',
            value: 150,
            icon: 'pi pi-shopping-cart',
            trend: 'up',
            tooltip: 'Total orders processed today',
            color: 'text-success'
          },
          {
            label: 'Staff on Duty',
            value: 10,
            icon: 'pi pi-users',
            trend: 'neutral',
            tooltip: 'Number of staff currently working',
            color: 'text-info'
          },
          {
            label: 'Pending Orders',
            value: 20,
            icon: 'pi pi-clock',
            trend: 'down',
            tooltip: 'Orders pending delivery',
            color: 'text-warning'
          },
          {
            label: 'Customer Complaints',
            value: 5,
            icon: 'pi pi-exclamation-triangle',
            trend: 'up',
            tooltip: 'Total customer complaints received',
            color: 'text-danger'
          },
          {
            label: 'Inventory Status',
            value: 'Healthy',
            icon: 'pi pi-box',
            trend: 'neutral',
            tooltip: 'Overall inventory status',
            color: 'text-info'
          },
        ];

      case 'ROLE_WAITER':
        return [
          {
            label: 'Tables Served',
            value: 10,
            icon: 'pi pi-table',
            trend: 'up',
            tooltip: 'Number of tables served today',
            color: 'text-success'
          },
          {
            label: 'Orders Taken',
            value: 30,
            icon: 'pi pi-shopping-cart',
            trend: 'neutral',
            tooltip: 'Total orders taken today',
            color: 'text-info'
          },
          {
            label: 'Customer Satisfaction',
            value: '4.8/5',
            icon: 'pi pi-star',
            trend: 'up',
            tooltip: 'Average customer satisfaction rating',
            color: 'text-success'
          },
          {
            label: 'Tips Earned',
            value: '$200',
            icon: 'pi pi-money-bill',
            trend: 'up',
            tooltip: 'Total tips earned today',
            color: 'text-success'
          },
        ];

      case 'ROLE_CASHIER':
        return [
          {
            label: 'Sales Today',
            value: '$8,000',
            icon: 'pi pi-dollar',
            trend: 'up',
            tooltip: 'Total sales for today',
            color: 'text-success'
          },
          {
            label: 'Transactions Completed',
            value: 150,
            icon: 'pi pi-check',
            trend: 'neutral',
            tooltip: 'Total transactions completed today',
            color: 'text-info'
          },
          {
            label: 'Pending Payments',
            value: '$1,500',
            icon: 'pi pi-clock',
            trend: 'down',
            tooltip: 'Payments pending confirmation',
            color: 'text-warning'
          },
          {
            label: 'Refunds Processed',
            value: 2,
            icon: 'pi pi-times',
            trend: 'up',
            tooltip: 'Total refunds processed today',
            color: 'text-danger'
          },
        ];

      case 'ROLE_CHEF':
        return [
          {
            label: 'Meals Prepared',
            value: 100,
            icon: 'pi pi-cutlery',
            trend: 'up',
            tooltip: 'Total meals prepared today',
            color: 'text-success'
          },
          {
            label: 'Food Waste',
            value: '5%',
            icon: 'pi pi-trash',
            trend: 'down',
            tooltip: 'Percentage of food waste today',
            color: 'text-danger'
          },
          {
            label: 'Kitchen Staff',
            value: 5,
            icon: 'pi pi-users',
            trend: 'neutral',
            tooltip: 'Total staff in the kitchen',
            color: 'text-info'
          },
          {
            label: 'Inventory Status',
            value: 'Good',
            icon: 'pi pi-box',
            trend: 'neutral',
            tooltip: 'Current inventory status in the kitchen',
            color: 'text-info'
          },
        ];

      case 'ROLE_KITCHEN_STAFF':
        return [
          {
            label: 'Meals Assisted',
            value: 90,
            icon: 'pi pi-cutlery',
            trend: 'up',
            tooltip: 'Total meals assisted today',
            color: 'text-success'
          },
          {
            label: 'Prep Time',
            value: '1h 30m',
            icon: 'pi pi-clock',
            trend: 'neutral',
            tooltip: 'Average prep time per meal',
            color: 'text-info'
          },
          {
            label: 'Cleanliness Score',
            value: '9/10',
            icon: 'pi pi-check-circle',
            trend: 'up',
            tooltip: 'Current kitchen cleanliness score',
            color: 'text-success'
          },
          {
            label: 'Staffing Level',
            value: 4,
            icon: 'pi pi-users',
            trend: 'neutral',
            tooltip: 'Number of staff currently working',
            color: 'text-info'
          },
        ];

      default:
        return [];
    }
  }
}
