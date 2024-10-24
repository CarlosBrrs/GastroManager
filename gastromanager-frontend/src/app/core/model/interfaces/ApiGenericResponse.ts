export interface ApiGenericResponse<T> {
  timestamp: string,
  flag: boolean,
  message: string,
  data: T
}
