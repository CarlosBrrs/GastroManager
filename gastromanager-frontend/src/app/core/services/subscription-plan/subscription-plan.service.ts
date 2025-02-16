import {Injectable} from '@angular/core';
import {BaseHttpService} from "../basehttp/base-http.service";
import {SubscriptionPlan} from "../../store/plan/plan.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SubscriptionPlanService extends BaseHttpService {

  constructor() {
    super();
  }

  getAllSubscriptionPlans(): Observable<SubscriptionPlan[]> {
    return this.handleRequest<SubscriptionPlan[]>("GET", "subscription-plans");

  }

  getSubscriptionPlanByUuid(uuid: string) {
return this.handleRequest<SubscriptionPlan>("GET", "subscription-plans/" + uuid, {headers: {'Accept': 'application/json'}})
  }
}
