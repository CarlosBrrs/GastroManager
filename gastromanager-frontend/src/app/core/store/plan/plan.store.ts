import {patchState, signalStore, withComputed, withHooks, withMethods, withState} from "@ngrx/signals";
import {SubscriptionPlan} from "./plan.model";
import {computed, inject} from "@angular/core";
import {SubscriptionPlanService} from "../../services/subscription-plan/subscription-plan.service";
import {StoreEventService} from "../../services/store-event/store-event.service";
import {BaseStore} from "../base-store";

type SubscriptionPlanState = {
  subscriptionPlans: SubscriptionPlan[];
  loading: boolean;
  error: string | null;
  selectedPlan: SubscriptionPlan | null;
};

const initialState: SubscriptionPlanState = {
  subscriptionPlans: [],
  loading: false,
  error: null,
  selectedPlan: null,
};

export const SubscriptionPlanStore = signalStore(
  {providedIn: "root"},
  withState<SubscriptionPlanState>(initialState),
  // Métodos
  withMethods((store, service = inject(SubscriptionPlanService), events = inject(StoreEventService)) => {
    const baseStore = new BaseStore(store, events);
    return {

      loadPlans(): void {
        baseStore.performOperation<SubscriptionPlan[]>(
          service.getAllSubscriptionPlans(),
          (response) => patchState(store, {subscriptionPlans: response}),
          "Planes de suscripción cargados con éxito"
        );
      },

      selectPlan(uuid: string) {
        let plan = store.subscriptionPlans().find(p => p.uuid === uuid);

        if (plan && this.isPlanComplete(plan)) {
          // Si el plan tiene todos los detalles, solo lo seleccionamos
          patchState(store, { selectedPlan: plan });
        } else {
          // Si no tiene todos los detalles, hacemos una nueva llamada a la API
          baseStore.performOperation(
            service.getSubscriptionPlanByUuid(uuid),
            (response) => {
              // Reemplazamos el plan en la lista con el actualizado
              patchState(store, {
                selectedPlan: response,
                subscriptionPlans: store.subscriptionPlans().map(p =>
                  p.uuid === response.uuid ? response : p
                ),
              });
            },
            "Plan cargado con éxito"
          );
        }
      },

      isPlanComplete(plan: SubscriptionPlan): boolean {
        return !!(plan.name && plan.description && plan.features && plan.price);
      }
    };
  }),

  withComputed(({ selectedPlan }) => ({
    selectedPlanDetails: computed(() => selectedPlan())
  })),
  withHooks({
    onInit(store) {
      store.loadPlans();
    },
    onDestroy() {
      console.log("Subscription store destroyed");
    }
  })
);
