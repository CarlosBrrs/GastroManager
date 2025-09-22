package com.kaiho.gastromanager.application.submenu.handler;

import com.kaiho.gastromanager.application.submenu.dto.request.SubmenuRequestDto;
import com.kaiho.gastromanager.application.submenu.dto.request.SubmenuUpdateRequestDto;
import com.kaiho.gastromanager.application.submenu.dto.response.SubmenuDetailResponseDto;
import com.kaiho.gastromanager.application.submenu.dto.response.SubmenuSummaryResponseDto;
import com.kaiho.gastromanager.application.submenu.mapper.SubmenuMapper;
import com.kaiho.gastromanager.domain.submenu.api.SubmenuServicePort;
import com.kaiho.gastromanager.domain.submenu.domain.Submenu;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria.SubmenuSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@Service
@RequiredArgsConstructor
public class SubmenuHandlerImpl implements SubmenuHandler {

    private final SubmenuMapper submenuMapper;
    private final SubmenuServicePort submenuServicePort;

    @Override
    public ApiGenericResponse<UUID> createSubmenu(SubmenuRequestDto submenuRequestDto) {
        Submenu submenu = submenuMapper.toDomain(submenuRequestDto);
        UUID createdSubmenu = submenuServicePort.createSubmenu(submenu);
        return buildSuccessResponse("Submenu created successfully", createdSubmenu);
    }

    @Override
    public ApiGenericResponse<Page<SubmenuSummaryResponseDto>> getAllSubmenus(SubmenuSearchCriteria criteria) {
        Page<Submenu> submenuPage = submenuServicePort.getAllSubmenus(criteria);

        Page<SubmenuSummaryResponseDto> menuResponseDtoList = submenuPage.map(submenuMapper::toResponseSummary);

        return buildSuccessResponse("List of submenus retrieved successfully", menuResponseDtoList);
    }

    @Override
    public ApiGenericResponse<SubmenuDetailResponseDto> getSubmenuById(UUID submenuUuid) {
        Submenu submenuById = submenuServicePort.getSubmenuById(submenuUuid);
        SubmenuDetailResponseDto response = submenuMapper.toResponseDetail(submenuById);
        return buildSuccessResponse("Submenu retrieved successfully", response);
    }

    @Override
    public ApiGenericResponse<SubmenuDetailResponseDto> updateSubmenu(UUID submenuUuid, SubmenuUpdateRequestDto submenuRequestDto) {
        Submenu submenu = submenuMapper.toDomain(submenuRequestDto);
        Submenu updateMenu = submenuServicePort.updateSubmenu(submenuUuid, submenu);
        SubmenuDetailResponseDto response = submenuMapper.toResponseDetail(updateMenu);
        return buildSuccessResponse("Submenu updated successfully", response);
    }
}
