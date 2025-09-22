import {inject, Injectable} from '@angular/core';
import {catchError, Observable} from 'rxjs';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ApiGenericResponse} from "../../../../core/model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";
import {RestaurantsRepository} from "../../domain/ports/restaurants.repository";
import {Restaurant} from "../../domain/models/restaurant.interface";
import {RestaurantDetailResponseDto} from "../../domain/models/restaurant-detail-response-dto.interface";
import {mapToRestaurantDetail} from "../../application/mappers/restaurant.mapper";
import {UserRestaurantResponseDto} from "../../domain/models/user-restaurant-response-dto.interface";
import {environment} from "../../../../../environments/environment.dev";


@Injectable({
  providedIn: 'root'
})
export class RestaurantsAdapter implements RestaurantsRepository {

  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = environment.API_URL;


  getRestaurantDetails(uuid: string): Observable<Restaurant> {

    return this.http.get<ApiGenericResponse<RestaurantDetailResponseDto>>(`${this.baseUrl}/restaurants/${uuid}`,
    ).pipe(
      map((response: ApiGenericResponse<RestaurantDetailResponseDto>) => mapToRestaurantDetail(response.data)),
      catchError((error: HttpErrorResponse) => {
        throw new Error(error.error.message);
      })
    )
  }

  getUserRestaurants(): Observable<UserRestaurantResponseDto[]> {
    console.log("Fetching user restaurants from API...");
    return this.http.get<ApiGenericResponse<UserRestaurantResponseDto[]>>(`${this.baseUrl}/restaurants/my-access`)
      .pipe(
        map((response: ApiGenericResponse<UserRestaurantResponseDto[]>) => response.data),
        catchError((error: HttpErrorResponse) => {
          throw new Error(error.error.message);
        })
      );
  }
}
