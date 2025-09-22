package com.kaiho.gastromanager.infrastructure.submenu.input.rest;

import com.kaiho.gastromanager.application.submenu.dto.request.SubmenuRequestDto;
import com.kaiho.gastromanager.application.submenu.dto.request.SubmenuUpdateRequestDto;
import com.kaiho.gastromanager.application.submenu.dto.response.SubmenuDetailResponseDto;
import com.kaiho.gastromanager.application.submenu.dto.response.SubmenuSummaryResponseDto;
import com.kaiho.gastromanager.application.submenu.handler.SubmenuHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.criteria.SubmenuSearchCriteria;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/submenus")
@AllArgsConstructor
public class SubmenuRestController {

    private final SubmenuHandler submenuHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<Page<SubmenuSummaryResponseDto>>> getAllSubmenus(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(required = false) UUID menuUuid,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        SubmenuSearchCriteria criteria = SubmenuSearchCriteria.builder()
                                                              .search(search)
                                                              .sortBy(sort.split(",")[0])
                                                              .sortDirection(sort.split(",")[1])
                                                              .page(page)
                                                              .size(size)
                                                              .menuUuid(menuUuid)
                                                              .build();
        ApiGenericResponse<Page<SubmenuSummaryResponseDto>> handlerResponse = submenuHandler.getAllSubmenus(criteria);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{submenuUuid}")
    public ResponseEntity<ApiGenericResponse<SubmenuDetailResponseDto>> getSubmenuById(@PathVariable UUID submenuUuid) {
        ApiGenericResponse<SubmenuDetailResponseDto> handlerResponse = submenuHandler.getSubmenuById(submenuUuid);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createSubmenu(@RequestBody @Valid SubmenuRequestDto submenuRequestDto) {
        return new ResponseEntity<>(submenuHandler.createSubmenu(submenuRequestDto), CREATED);
    }

    @PutMapping("/{submenuUuid}")
    public ResponseEntity<ApiGenericResponse<SubmenuDetailResponseDto>> updateSubmenu(
            @PathVariable UUID submenuUuid, @RequestBody SubmenuUpdateRequestDto menuRequestDto) {
        ApiGenericResponse<SubmenuDetailResponseDto> handlerResponse = submenuHandler.updateSubmenu(submenuUuid, menuRequestDto);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

}
