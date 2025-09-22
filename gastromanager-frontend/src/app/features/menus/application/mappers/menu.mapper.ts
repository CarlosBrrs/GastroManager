import {MenuResponseDto} from "../../domain/models/menu-response-dto.interface";
import {Menu} from "../../domain/models/menu.interface";
import {MenuRequestDto} from "../../domain/models/menu-request-dto.interface";
import {MenuDetailResponseDto} from "../../domain/models/menu-detail-response-dto.interface";

export function mapToMenuSummary(dto: MenuResponseDto): Menu {
  return {
    uuid: dto.uuid,
    name: dto.name,
    description: dto.description,
    isEnabled: dto.isEnabled,
  };
}

export function mapToMenuRequestDto(menu: Menu): MenuRequestDto {
  return {
    name: menu.name,
    description: menu.description,
  }
}

export function mapToMenuDetail(menuResponseDto: MenuDetailResponseDto): Menu {
  return {
    uuid: menuResponseDto.uuid,
    name: menuResponseDto.name,
    description: menuResponseDto.description,
    isEnabled: menuResponseDto.isEnabled,
    createdBy: menuResponseDto.createdBy,
    createdDate: new Date(menuResponseDto.createdDate),
    updatedBy: menuResponseDto.updatedBy,
    updatedDate: new Date(menuResponseDto.updatedDate),
  }
}

export function mapToEditMenuRequestDto(menu: Menu): MenuRequestDto {
  return {
    name: menu.name,
    description: menu.description,
  }
}


