package com.kaiho.gastromanager.application.submenu.handler;

import com.kaiho.gastromanager.application.submenu.dto.request.SubmenuRequestDto;
import com.kaiho.gastromanager.application.submenu.dto.request.SubmenuUpdateRequestDto;
import com.kaiho.gastromanager.application.submenu.dto.response.SubmenuDetailResponseDto;
import com.kaiho.gastromanager.application.submenu.dto.response.SubmenuSummaryResponseDto;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria.SubmenuSearchCriteria;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface SubmenuHandler {
    ApiGenericResponse<UUID> createSubmenu(SubmenuRequestDto submenuRequestDto);

    ApiGenericResponse<Page<SubmenuSummaryResponseDto>> getAllSubmenus(SubmenuSearchCriteria criteria);

    ApiGenericResponse<SubmenuDetailResponseDto> getSubmenuById(UUID submenuUuid);

    ApiGenericResponse<SubmenuDetailResponseDto> updateSubmenu(UUID submenuUuid, SubmenuUpdateRequestDto submenuRequestDto);
}
