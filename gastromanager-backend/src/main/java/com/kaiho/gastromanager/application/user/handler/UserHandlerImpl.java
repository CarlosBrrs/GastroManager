package com.kaiho.gastromanager.application.user.handler;

import com.kaiho.gastromanager.application.user.dto.request.UserRequestDto;
import com.kaiho.gastromanager.application.user.dto.response.UserResponseDto;
import com.kaiho.gastromanager.application.user.mapper.UserMapper;
import com.kaiho.gastromanager.domain.restaurant.api.RestaurantServicePort;
import com.kaiho.gastromanager.domain.user.api.RoleServicePort;
import com.kaiho.gastromanager.domain.user.api.UserServicePort;
import com.kaiho.gastromanager.domain.user.model.Role;
import com.kaiho.gastromanager.domain.user.model.User;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildSuccessResponse;

@RequiredArgsConstructor
@Component
public class UserHandlerImpl implements UserHandler {


    private final UserMapper userMapper;
    private final UserServicePort userServicePort;
    private final RoleServicePort roleServicePort;
    private final RestaurantServicePort restaurantServicePort;

    @Override
    public ApiGenericResponse<UserResponseDto> createUser(UserRequestDto userRequestDto) {




        // TODO: CHANGE THIS DOMAIN TO ENCAPSULATE THOSE ATTRIBUTES
        User user = userMapper.toDomain(userRequestDto);
        User createdUser = userServicePort.createUser(user);
        UserResponseDto responseDto = userMapper.toResponse(createdUser);
        return buildSuccessResponse("User creation successful", responseDto);
    }

    @Override
    public ApiGenericResponse<UserResponseDto> getUserByUuid(UUID userUuid) {
        User user = userServicePort.getUserByUuid(userUuid);
        UserResponseDto responseDto = userMapper.toResponse(user);
        return buildSuccessResponse("User info retrieved successfully", responseDto);
    }
}
