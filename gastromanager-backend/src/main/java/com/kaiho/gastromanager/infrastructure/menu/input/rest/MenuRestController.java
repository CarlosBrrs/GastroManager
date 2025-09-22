package com.kaiho.gastromanager.infrastructure.menu.input.rest;


import com.kaiho.gastromanager.application.menu.dto.request.MenuRequestDto;
import com.kaiho.gastromanager.application.menu.dto.request.MenuUpdateRequestDto;
import com.kaiho.gastromanager.application.menu.dto.response.MenuDetailResponseDto;
import com.kaiho.gastromanager.application.menu.dto.response.MenuSummaryResponseDto;
import com.kaiho.gastromanager.application.menu.handler.MenuHandler;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import com.kaiho.gastromanager.infrastructure.menu.output.jpa.criteria.MenuSearchCriteria;
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
@RequestMapping("/menus")
@AllArgsConstructor
public class MenuRestController {

    private final MenuHandler menuHandler;

    @GetMapping
    public ResponseEntity<ApiGenericResponse<Page<MenuSummaryResponseDto>>> getAllMenus(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        MenuSearchCriteria criteria = MenuSearchCriteria.builder()
                                                        .search(search)
                                                        .sortBy(sort.split(",")[0])
                                                        .sortDirection(sort.split(",")[1])
                                                        .page(page)
                                                        .size(size)
                                                        .build();
        ApiGenericResponse<Page<MenuSummaryResponseDto>> handlerResponse = menuHandler.getAllMenus(criteria);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @GetMapping("/{menuUuid}")
    public ResponseEntity<ApiGenericResponse<MenuDetailResponseDto>> getMenuById(@PathVariable UUID menuUuid) {
        ApiGenericResponse<MenuDetailResponseDto> handlerResponse = menuHandler.getMenuById(menuUuid);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<UUID>> createMenu(@RequestBody @Valid MenuRequestDto menuRequestDto) {
        return new ResponseEntity<>(menuHandler.createMenu(menuRequestDto), CREATED);
    }

    @PutMapping("/{menuUuid}")
    public ResponseEntity<ApiGenericResponse<MenuDetailResponseDto>> updateMenu(
            @PathVariable UUID menuUuid, @RequestBody MenuUpdateRequestDto menuRequestDto) {
        ApiGenericResponse<MenuDetailResponseDto> handlerResponse = menuHandler.updateMenu(menuUuid, menuRequestDto);
        return new ResponseEntity<>(handlerResponse, HttpStatus.OK);
    }
}
