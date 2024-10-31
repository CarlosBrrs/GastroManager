import {RoleResponseDto} from "./RoleResponseDto";

export interface UserResponseDto {
  uuid: string
  name: string
  lastname: string
  email: string
  username: string
  roles: RoleResponseDto[]
}
