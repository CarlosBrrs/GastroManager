import {ChangeDetectionStrategy, Component, computed, inject, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CashRegisterCardComponent} from './cash-register-card/cash-register-card.component';
import {CashRegisterStore} from "../../../../../core/store/cash-register/cash-register.store";
import {CashRegister} from "../../../domain/models/cash-register.interface";
import {CashRegisterSessionStore} from "../../../../../core/store/cash-register/cash-register-session.store";

@Component({
  selector: 'gm-management-cash-registers-page',
  standalone: true,
  imports: [CommonModule, CashRegisterCardComponent],
  templateUrl: './management-cash-registers-page.component.html',
  styleUrl: './management-cash-registers-page.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ManagementCashRegistersPageComponent {

  // Signal para almacenar el resumen de sesión actual
  currentSessionSummary = signal<any>(null);
  // Signal para almacenar qué caja tiene el resumen cargado
  sessionSummaryForCashRegister = signal<string | null>(null);
  // Computed properties para las estadísticas - con tipos explícitos para evitar errores
  activeCashRegisters = computed(() =>
    this.cashRegisters().filter((cr: CashRegister) => cr.status === 'ACTIVE').length
  );
  closedCashRegisters = computed(() =>
    this.cashRegisters().filter((cr: CashRegister) => cr.status === 'CLOSED').length
  );
  totalInitialAmount = computed(() =>
    this.cashRegisters()
      .filter((cr: CashRegister) => cr.currentSession)
      .reduce((total: number, cr: CashRegister) => total + (cr.currentSession?.initialAmount || 0), 0)
  );
  totalCashRegisters = computed(() => this.cashRegisters().length);
  private readonly cashRegisterStore = inject(CashRegisterStore);
  // Computed properties basadas en el store - con tipos explícitos
  cashRegisters = computed(() => this.cashRegisterStore.cashRegisters());
  loading = computed(() => this.cashRegisterStore.loading());
  error = computed(() => this.cashRegisterStore.error());
  private readonly cashRegisterSessionStore = inject(CashRegisterSessionStore);
  // Computed properties para el session store
  sessionLoading = computed(() => this.cashRegisterSessionStore.loading());
  sessionError = computed(() => this.cashRegisterSessionStore.error());
  currentSession = computed(() => this.cashRegisterSessionStore.currentSession());

  onOpenCashRegister(data: { uuid: string, initialAmount: number, notes: string }) {

    console.log('🟢 Recibiendo datos para abrir caja registradora:');
    console.log('📄 UUID:', data.uuid);
    console.log('💰 Monto inicial:', data.initialAmount);
    console.log('📝 Notas:', data.notes);

    // Llamar al store para abrir la sesión
    this.cashRegisterSessionStore.openSession({
      cashRegisterUuid: data.uuid,
      initialAmount: data.initialAmount,
      notes: data.notes
    }).subscribe();
  }

  onCloseCashRegister(data: { uuid: string, finalAmount: number, notes: string }) {
    console.log('🔒 Recibiendo datos para cerrar caja registradora:');
    console.log('📄 UUID:', data.uuid);
    console.log('💰 Monto final:', data.finalAmount);
    console.log('📝 Notas:', data.notes);

    // Necesitamos el UUID de la sesión, no de la caja registradora
    // Por ahora usaremos el sessionSummary para obtener el sessionUuid
    const sessionUuid = this.currentSessionSummary()?.sessionUuid;

    if (sessionUuid) {
      // Llamar al store para cerrar la sesión
      this.cashRegisterSessionStore.closeSession({
        sessionUuid: sessionUuid,
        finalAmount: data.finalAmount,
        notes: data.notes
      });

      // Limpiar el resumen de sesión después de cerrar
      this.currentSessionSummary.set(null);
      this.sessionSummaryForCashRegister.set(null);
    } else {
      console.error('❌ No se pudo obtener el UUID de la sesión para cerrar');
      // Aquí podrías mostrar un mensaje de error al usuario
    }
  }

  onViewCashRegisterDetails(cashRegisterUuid: string) {
    console.log('Ver detalles de caja registradora:', cashRegisterUuid);
    // TODO: Navegar a página de detalles o abrir modal
  }

  onViewTransactions(cashRegisterUuid: string) {
    console.log('Ver transacciones de caja registradora:', cashRegisterUuid);
    // TODO: Navegar a página de transacciones o abrir modal
  }

  onCreateCashRegister() {
    console.log('Crear nueva caja registradora');
    // TODO: Implementar modal para crear nueva caja registradora
  }

  // Método para limpiar errores de sesión
  clearSessionError() {
    this.cashRegisterSessionStore.clearError();
  }

  getCashRegisterStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'badge bg-success';
      case 'CLOSED':
        return 'badge bg-secondary';
      case 'MAINTENANCE':
        return 'badge bg-warning';
      default:
        return 'badge bg-secondary';
    }
  }

  getCashRegisterStatusText(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'Activa';
      case 'CLOSED':
        return 'Cerrada';
      case 'MAINTENANCE':
        return 'Mantenimiento';
      default:
        return 'Desconocido';
    }
  }

  // Método actualizado para manejar la solicitud de resumen de sesión sin UUID
  onRequestSessionSummary(cashRegisterUuid: string) {
    console.log('🔍 Solicitando resumen de sesión para caja:', cashRegisterUuid);

    // Limpiar resumen anterior si es de otra caja
    if (this.sessionSummaryForCashRegister() !== cashRegisterUuid) {
      this.currentSessionSummary.set(null);
      this.sessionSummaryForCashRegister.set(cashRegisterUuid);
    }

    // Llamar al store para obtener el resumen de sesión (sin UUID - identificado por usuario)
    this.cashRegisterSessionStore.getSessionSummary()
      .subscribe({
        next: (sessionSummary) => {
          console.log('✅ Resumen de sesión obtenido del store:', sessionSummary);
          this.currentSessionSummary.set(sessionSummary);
        },
        error: (error) => {
          console.error('❌ Error al obtener resumen de sesión del store:', error);
          // El error ya se maneja en el store, aquí solo logueamos
        }
      });
  }

  // Computed para obtener el resumen de sesión de una caja específica
  getSessionSummaryForCashRegister(cashRegisterUuid: string) {
    return this.sessionSummaryForCashRegister() === cashRegisterUuid
      ? this.currentSessionSummary()
      : null;
  }
}
