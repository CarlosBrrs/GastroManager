import {PageSort} from "./page-sort.interface";


export interface Pageable {
  pageNumber: number
  pageSize: number
  sort: PageSort
  offset: number
  paged: boolean
  unpaged: boolean
}
