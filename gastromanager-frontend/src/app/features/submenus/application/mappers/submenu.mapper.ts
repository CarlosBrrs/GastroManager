import { SubmenuRequestDto } from "../../domain/models/submenu-request-dto.interface";
import {SubmenuSummaryResponseDto} from "../../domain/models/submenu-summary-response-dto.interface";
import {Submenu} from "../../domain/models/submenu.interface";

export function mapToSubmenu(dto: SubmenuSummaryResponseDto): Submenu {
  return {
    uuid: dto.uuid,
    name: dto.name,
    description: dto.description,
    menu: {
      uuid: dto.menuName, // Assuming menuName is the UUID of the menu
      name: dto.menuName, // Assuming menuName is also used as the name
      description: '', // Placeholder, as this information is not provided in the DTO
      isEnabled: true, // Placeholder, as this information is not provided in the DTO
    },
    createdAt: new Date(), // Placeholder, as this information is not provided in the DTO
    updatedAt: new Date(), // Placeholder, as this information is not provided in the DTO
  };
}

export function mapToSubmenuRequestDto(submenu: Submenu): SubmenuRequestDto {
  return {
    name: submenu.name,
    description: submenu.description,
    menuUuid: submenu.menu.uuid,
  }
}
