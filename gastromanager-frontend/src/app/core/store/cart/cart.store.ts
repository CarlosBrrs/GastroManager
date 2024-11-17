import {patchState, signalStore, withMethods, withState} from "@ngrx/signals";
import {OrderItem} from "./cart.model";

type CartState = {
  customerNotes: string;
  orderItems: OrderItem[];
};

const initialState: CartState = {
  customerNotes: 'notes',
  orderItems: [{productUuid: "111", quantity: 1}, {productUuid: "222", quantity: 2}],
};

//TODO: this will be used to manage the different orders after place them
export const CartStore = signalStore(
  {providedIn: "root"},
  withState(initialState),
  withMethods((store) => ({
   /* addOrderItem(newOrderItem: OrderItem) {
      patchState(store, {
        orderItems: []
      });
    },
    addCustomerNote(customerNotes: string) {}
*/
    // , newMethod() {} //duplicate this to add new methods
  }))
);
