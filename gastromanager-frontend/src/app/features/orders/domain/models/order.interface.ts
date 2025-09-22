export interface Order {
  uuid: string;
  code: string;
  customerNotes: string;
  customerName: string;
  notes?: string;
  totalAmount: number;
  totalPaid: number;
  remainingToPay: number;
  paymentStatus: string;
  tableNumber: string;
  paymentType: string; // e.g., 'Cash', 'Card'
  status: string; // e.g., 'Pending', 'Completed', 'Cancelled'
  orderItems: /*OrderItem[];*/ any[]; // Replace with actual OrderItem type
}
