export type Invoice = {
  uuid: string;
  code: string;
  user: string;
  customerNotes: string;
  totalPrice: number;
  amount: number;
  status: string;
  createdBy: string;
  createdDate: string;
  updatedBy: string;
  updatedDate: string;
}
