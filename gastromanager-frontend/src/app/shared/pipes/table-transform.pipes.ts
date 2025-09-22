import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'orderItems',
  standalone: true
})
export class OrderItemsPipe implements PipeTransform {
  transform(orderItems: any[]): string {
    if (!orderItems || orderItems.length === 0) return 'No items';

    return orderItems
      .map(item => `${item.productName} - ${item.quantity}x$${item.unitPrice.toLocaleString('es-CO')} - $${item.subtotal.toLocaleString('es-CO')}`)
      .join('<br>'); // Usar <br> en lugar de comas para saltos de línea HTML
  }
}

@Pipe({
  name: 'currency',
  standalone: true
})
export class CustomCurrencyPipe implements PipeTransform {
  transform(value: number): string {
    return `$${value.toLocaleString('es-CO')}`;
  }
}

@Pipe({
  name: 'orderStatus',
  standalone: true
})
export class OrderStatusPipe implements PipeTransform {
  transform(status: string): string {
    if (!status) return '';

    // Mapeo para PaymentStatus
    const paymentStatusMap: Record<string, string> = {
      'UNPAID': 'Sin pagar',
      'PARTIALLY_PAID': 'Parcialmente pagado',
      'FULLY_PAID': 'Completamente pagado',
      'REFUNDED': 'Reembolsado'
    };

    if (paymentStatusMap[status]) {
      return paymentStatusMap[status];
    }

    // Mapeo para OperationalStatus
    const operationalStatusMap: Record<string, string> = {
      'AWAITING_PAYMENT': 'Esperando pago',
      'PENDING': 'Pendiente',
      'PREPARING': 'En preparación',
      'READY': 'Listo',
      'SERVED': 'Servido',
      'COMPLETED': 'Completado',
      'CANCELLED': 'Cancelado'
    };

    if (operationalStatusMap[status]) {
      return operationalStatusMap[status];
    }

    // Fallback: devolver el valor tal cual (o formateado mínimamente)
    // Reemplazar guiones bajos por espacios y capitalizar
    return status.replace(/_/g, ' ').toLowerCase().replace(/(^|\s)\S/g, (t) => t.toUpperCase());
  }
}
