import {OrderCreateRequestDto, PaymentType} from "../../domain/models/order-create-request-dto.interface";
import {OrderSummaryResponseDto} from "../../domain/models/order-summary-response-dto.interface";
import {OrderDetailResponseDto} from "../../domain/models/order-detail-response-dto.interface";
import {Order} from "../../domain/models/order.interface";
import {OrderItemRequestDto} from "../../../../services/models/order-item-request-dto";

export function mapToOrder(dto: OrderSummaryResponseDto): Order {
  return {
    uuid: dto.uuid,
    code: dto.code,
    customerNotes: '',
    tableNumber: '',
    customerName: '',
    paymentType: '',
    totalAmount: dto.totalAmount,
    totalPaid: dto.totalPaid,
    remainingToPay: dto.remainingToPay,
    paymentStatus: dto.paymentStatus,
    status: dto.operationalStatus, // Mapear operationalStatus al campo status
    orderItems: dto.orderItems.map(item => ({
      uuid: item.uuid,
      productUuid: item.productUuid,
      productName: item.productName,
      quantity: item.quantity,
      unitPrice: item.unitPrice,
      subtotal: item.subtotal,
      customerNotes: item.customerNotes
    }))
  };
}

export function mapToOrderCreateRequestDto(order: Order): OrderCreateRequestDto {
  return {
    orderItems: order.orderItems.map(item => ({
        productUuid: item.productUuid,
        quantity: item.quantity,
        customerNotes: item.customerNotes || '',
      } as OrderItemRequestDto)
    ),
    customerNotes: order.customerNotes,
    tableNumber: order.tableNumber,
    paymentType: order.paymentType as PaymentType,
    customerName: order.customerName
  };
}

export function mapOrderDetailToOrder(dto: OrderDetailResponseDto): Order {
  return {
    uuid: dto.uuid,
    code: dto.code,
    customerName: dto.customerName,
    customerNotes: dto.customerNotes, // Ahora viene directamente del objeto principal
    tableNumber: dto.tableNumber,
    paymentType: '', // No viene en la respuesta, usar valor por defecto
    totalAmount: dto.totalAmount,
    totalPaid: dto.totalPaid, // Usar el valor real del DTO
    remainingToPay: dto.remainingToPay, // Usar el valor real del DTO
    paymentStatus: dto.paymentStatus, // Usar el estado real del DTO
    status: dto.operationalStatus, // Mapear el operationalStatus al status
    orderItems: dto.orderItems.map(item => ({
      uuid: item.uuid,
      productUuid: item.productUuid,
      productName: item.productName,
      quantity: item.quantity,
      unitPrice: item.unitPrice,
      subtotal: item.subtotal,
      customerNotes: item.customerNotes // Notas específicas por producto
    }))
  };
}
