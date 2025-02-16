import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {catchError, Observable, throwError} from "rxjs";
import {ApiGenericResponse} from "../../model/interfaces/ApiGenericResponse";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class BaseHttpService {

  apiUrl: string = environment.API_URL;
  constructor() {
  }
  http: HttpClient = inject(HttpClient);

  protected handleRequest<T>(
    method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH',
    endpoint: string,
    body?: any,
    params?: HttpParams
  ): Observable<T> {
    const url = `${this.apiUrl}/${endpoint}`;
    const headers = this.buildHeaders(method, body);
    return this.http.request<ApiGenericResponse<T>>(method, url, {
      body,
      headers,
      params
    }).pipe(
      map(response => this.handleSuccessResponse<T>(response)),
      // catchError(error => this.handleErrorResponse(error))
    );
  }

  private buildHeaders(method: string, body?: any): HttpHeaders {
    let headers = new HttpHeaders({ 'Accept': 'application/json' });

    if (['POST', 'PUT', 'PATCH'].includes(method) && body) {
      headers = headers.set('Content-Type', 'application/json');
    }

    return headers;
  }

  private handleSuccessResponse<T>(response: ApiGenericResponse<T>): T {

    if (!response.flag || !response.data) {
      throw new Error(response.message || 'Operación fallida');
    }
    return response.data;
  }
/*
  private handleErrorResponse(error: HttpErrorResponse): Observable<never> {
    const apiError = error.error as ApiGenericResponse<null>;
    const errorMessage = apiError?.message || error.message;
    return throwError(() => new Error(errorMessage));
  }*/
}
