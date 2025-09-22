package com.kaiho.gastromanager.application.menu.handler;

import com.kaiho.gastromanager.application.menu.dto.request.MenuRequestDto;
import com.kaiho.gastromanager.application.menu.dto.request.MenuUpdateRequestDto;
import com.kaiho.gastromanager.application.menu.dto.response.MenuDetailResponseDto;
import com.kaiho.gastromanager.application.menu.dto.response.MenuSummaryResponseDto;
import com.kaiho.gastromanager.application.menu.mapper.MenuMapper;
import com.kaiho.gastromanager.domain.menu.api.MenuServicePort;
import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria.MenuSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Component
@RequiredArgsConstructor
public class MenuHandlerImpl implements MenuHandler {

    private final MenuMapper menuMapper;
    private final MenuServicePort menuServicePort;

    @Override
    public ApiGenericResponse<UUID> createMenu(MenuRequestDto menuRequestDto) {
        Menu menu = menuMapper.toDomain(menuRequestDto);
        UUID createdMenu = menuServicePort.createMenu(menu);
        return buildSuccessResponse("Menu created successfully", createdMenu);
    }

    @Override
    public ApiGenericResponse<Page<MenuSummaryResponseDto>> getAllMenus(MenuSearchCriteria criteria) {
        Page<Menu> menuPage = menuServicePort.getAllMenus(criteria);

        Page<MenuSummaryResponseDto> menuResponseDtoList = menuPage.map(menuMapper::toResponseSummary);

        return buildSuccessResponse("List of menus retrieved successfully", menuResponseDtoList);
    }

    @Override
    public ApiGenericResponse<MenuDetailResponseDto> getMenuById(UUID menuUuid) {
        Menu menuById = menuServicePort.getMenuById(menuUuid);
        MenuDetailResponseDto response = menuMapper.toResponseDetail(menuById);
        return buildSuccessResponse("Menu retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<MenuDetailResponseDto> updateMenu(UUID menuUuid, MenuUpdateRequestDto menuRequestDto) {
        Menu menu = menuMapper.toDomain(menuRequestDto);
        Menu updateMenu = menuServicePort.updateMenu(menuUuid, menu);
        MenuDetailResponseDto response = menuMapper.toResponseDetail(updateMenu);
        return buildSuccessResponse("Menu updated successfully", response);
    }
}
