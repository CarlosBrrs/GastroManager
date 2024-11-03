import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserResponseDto} from "../../core/model/interfaces/UserResponseDto";
import {UserService} from "../../core/services/users/user.service";
import {Subject, takeUntil} from "rxjs";
import {Router} from "@angular/router";
import {AvatarModule} from "primeng/avatar";
import {CurrencyPipe, DatePipe, TitleCasePipe} from "@angular/common";
import {Button} from "primeng/button";
import {CardModule} from "primeng/card";
import {HomeService} from "../../core/services/home/home.service";
import {TableModule} from "primeng/table";
import {QuickActionsComponent} from "./quick-actions/quick-actions.component";
import {QuickAction} from "../../core/model/interfaces/QuickAction";
import {KeyMetricsComponent} from "./key-metrics/key-metrics.component";
import {MetricsService} from "../../core/services/metrics/metrics.service";

export interface Metric {
  label: string; // Nombre de la métrica, como "Revenue", "Orders Today", etc.
  value: string | number; // Valor de la métrica, como "$12,500", 250, etc.
  icon?: string; // Nombre del icono para representar la métrica (ej. "pi pi-chart-line").
  trend?: 'up' | 'down' | 'neutral'; // Tendencia para mostrar el cambio (positivo, negativo o neutro).
  tooltip?: string; // Información adicional que aparecería al pasar el cursor sobre la métrica.
  color?: string; // Color para diferenciar métricas (ej. "text-success", "text-danger").
}

export interface RecentActivity {
  type: 'order' | 'reservation' | 'inventory' | 'staff' | 'review';
  description: string;
  timestamp: Date;
  status?: string;
  icon?: string;
}

@Component({
  selector: 'gm-home',
  standalone: true,
  imports: [
    AvatarModule,
    TitleCasePipe,
    Button,
    CardModule,
    TableModule,
    CurrencyPipe,
    QuickActionsComponent,
    DatePipe,
    KeyMetricsComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit, OnDestroy {

  userInfo!: UserResponseDto;
  actions: QuickAction[] = [];
  actionsByRole = {
    owner: [
      {label: 'Manage Restaurants', icon: 'pi pi-building', action: () => this.manageRestaurants()},
      {label: 'View Reports', icon: 'pi pi-chart-line', action: () => this.viewReports()},
      {label: 'User Management', icon: 'pi pi-users', action: () => this.manageUsers()},
      {label: 'Manage Inventory', icon: 'pi pi-box', action: () => this.manageInventory()}
    ],
    // manager: [
    //   { label: 'View Daily Sales', icon: 'pi pi-dollar', action: () => this.viewSales() },
    //   { label: 'Manage Staff', icon: 'pi pi-users', action: () => this.manageStaff() },
    //   { label: 'Inventory Orders', icon: 'pi pi-shopping-cart', action: () => this.orderInventory() },
    //   { label: 'Customer Feedback', icon: 'pi pi-comment', action: () => this.viewFeedback() }
    // ],
    // chef: [
    //   { label: 'View Menu', icon: 'pi pi-book', action: () => this.viewMenu() },
    //   { label: 'Manage Recipes', icon: 'pi pi-file', action: () => this.manageRecipes() },
    //   { label: 'Track Inventory', icon: 'pi pi-box', action: () => this.trackInventory() },
    //   { label: 'Specials Management', icon: 'pi pi-star', action: () => this.manageSpecials() }
    // ],
    // waiter: [
    //   { label: 'View Orders', icon: 'pi pi-list', action: () => this.viewOrders() },
    //   { label: 'Take New Orders', icon: 'pi pi-plus', action: () => this.takeOrder() },
    //   { label: 'Update Order Status', icon: 'pi pi-check', action: () => this.updateOrderStatus() },
    //   { label: 'Customer Notes', icon: 'pi pi-pencil', action: () => this.viewCustomerNotes() }
    // ],
    // cashier: [
    //   { label: 'Process Payments', icon: 'pi pi-credit-card', action: () => this.processPayments() },
    //   { label: 'View Daily Transactions', icon: 'pi pi-file', action: () => this.viewTransactions() },
    //   { label: 'Issue Refunds', icon: 'pi pi-undo', action: () => this.issueRefunds() },
    //   { label: 'Financial Reports', icon: 'pi pi-chart-bar', action: () => this.viewFinancialReports() }
    // ]
  };
  currentDate: Date = new Date();
  private destroy$ = new Subject();
  summaryMetrics: Metric[] = [];

  constructor(protected router: Router, private userService: UserService, private homeService: HomeService, private metricsService: MetricsService) {

  }

  onQuickAction(event: any) {
    event.action()
  }

  ngOnInit(): void {
// TODO: Dont call the endpoint always, instead save the user info in the service and call the signal

    this.userService.loadUserInfo().pipe(takeUntil(this.destroy$)).subscribe({
      next: (result) => {
        this.userInfo = result.data;
        this.updateTime();
        this.loadMetrics();
// Load actions based on the user's role

        this.actions = this.actionsByRole['owner'];
      },
      error: (response) => {
        console.log(response.error)
      },
      complete: () => {
      }
    });


  }

  ngOnDestroy(): void {
    this.destroy$.next(this.userService.loadUserInfo());  // trigger the unsubscribe
    this.destroy$.complete();
  }

  private updateTime(): void {
    // Update current time every second
    setInterval(() => {
      this.currentDate = new Date();
    }, 1000);
  }

  private manageRestaurants() {
    window.alert('manage rests')
  }

  private viewReports() {
    window.alert('view reports')
  }

  private manageUsers() {
    window.alert('manage users')

  }

  private manageInventory() {
    window.alert('manage inventory')

  }

  private loadMetrics() {
    const userRole = this.userInfo.roles[0].name;
    this.summaryMetrics = this.metricsService.getSummaryMetrics(userRole);
  }
}

