package com.kaiho.gastromanager.application.submenu.mapper;

import com.kaiho.gastromanager.application.submenu.dto.request.SubmenuRequestDto;
import com.kaiho.gastromanager.application.submenu.dto.request.SubmenuUpdateRequestDto;
import com.kaiho.gastromanager.application.submenu.dto.response.SubmenuDetailResponseDto;
import com.kaiho.gastromanager.application.submenu.dto.response.SubmenuSummaryResponseDto;
import com.kaiho.gastromanager.domain.menu.model.Menu;
import com.kaiho.gastromanager.domain.submenu.domain.Submenu;
import org.springframework.stereotype.Component;

@Component
public class SubmenuMapper {

    public Submenu toDomain(SubmenuRequestDto submenuRequestDto) {
        if (submenuRequestDto == null) {
            return null;
        }
        return Submenu.builder()
                      .name(submenuRequestDto.name())
                      .description(submenuRequestDto.description())
                      .menu(Menu.builder().uuid(submenuRequestDto.menuUuid()).build())
                      .isEnabled(true)
                      .build();
    }

    public SubmenuSummaryResponseDto toResponseSummary(Submenu submenu) {
        if (submenu == null) {
            return null;
        }
        return SubmenuSummaryResponseDto.builder()
                                        .uuid(submenu.getUuid())
                                        .name(submenu.getName())
                                        .description(submenu.getDescription())
                                        .menuName(submenu.getMenu().getName())
                                        .isEnabled(submenu.isEnabled())
                                        .build();
    }

    public SubmenuDetailResponseDto toResponseDetail(Submenu submenuById) {
        if (submenuById == null) {
            return null;
        }
        return SubmenuDetailResponseDto.builder()
                                       .uuid(submenuById.getUuid())
                                       .name(submenuById.getName())
                                       .description(submenuById.getDescription())
                                       .menuUuid(submenuById.getMenu().getUuid())
                                       .menuName(submenuById.getMenu().getName())
                                       .isEnabled(submenuById.isEnabled())
                                       .createdBy(submenuById.getCreatedBy())
                                       .createdDate(submenuById.getCreatedDate())
                                       .updatedBy(submenuById.getUpdatedBy())
                                       .updatedDate(submenuById.getUpdatedDate())
                                       .build();
    }

    public Submenu toDomain(SubmenuUpdateRequestDto submenuRequestDto) {
        if (submenuRequestDto == null) {
            return null;
        }
        return Submenu.builder()
                      .name(submenuRequestDto.name())
                      .description(submenuRequestDto.description())
                      .build();
    }
}
