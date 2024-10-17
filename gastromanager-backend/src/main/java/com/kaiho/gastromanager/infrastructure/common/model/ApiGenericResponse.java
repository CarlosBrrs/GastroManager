package com.kaiho.gastromanager.infrastructure.common.model;

import java.time.Instant;

public record ApiGenericResponse<T>(Instant timestamp, boolean flag, String message, T data) {

    public static <T> ApiGenericResponse<T> buildSuccessResponse(String message, T data) {
        return new ApiGenericResponse<>(Instant.now(), true, message, data);
    }

    public static <T> ApiGenericResponse<T> buildErrorResponse(String message) {
        return new ApiGenericResponse<>(Instant.now(), false, message, null);
    }

}
