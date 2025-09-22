import {Pageable} from "./pageable.interface";
import {RequestSort} from "./request-sort.interface";

export interface Page<T> {
  content: T[]
  pageable: Pageable
  last: boolean
  totalElements: number
  totalPages: number
  first: boolean
  size: number
  number: number
  sort: RequestSort
  numberOfElements: number
  empty: boolean
}
