package com.kaiho.gastromanager.infrastructure.common.exceptionhandler;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExist;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildErrorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(EntityDoesNotExist.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleEntityDoesNotExistException(EntityDoesNotExist ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }
}
