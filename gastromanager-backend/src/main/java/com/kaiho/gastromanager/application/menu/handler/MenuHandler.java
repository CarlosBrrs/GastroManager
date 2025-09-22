package com.kaiho.gastromanager.application.menu.handler;

import com.kaiho.gastromanager.application.menu.dto.request.MenuRequestDto;
import com.kaiho.gastromanager.application.menu.dto.request.MenuUpdateRequestDto;
import com.kaiho.gastromanager.application.menu.dto.response.MenuDetailResponseDto;
import com.kaiho.gastromanager.application.menu.dto.response.MenuSummaryResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria.MenuSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MenuHandler {
    ApiGenericResponse<UUID> createMenu(MenuRequestDto menuRequestDto);

    ApiGenericResponse<Page<MenuSummaryResponseDto>> getAllMenus(MenuSearchCriteria criteria);

    ApiGenericResponse<MenuDetailResponseDto> getMenuById(UUID menuUuid);

    ApiGenericResponse<MenuDetailResponseDto> updateMenu(UUID menuUuid, MenuUpdateRequestDto menuRequestDto);
}
