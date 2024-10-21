package com.kaiho.gastromanager.infrastructure.common.exceptionhandler;

import com.kaiho.gastromanager.domain.common.exception.EntityAlreadyExistsException;
import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExistException;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildErrorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(EntityDoesNotExistException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleEntityDoesNotExistException(EntityDoesNotExistException ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleEntityDoesNotExistException(EntityAlreadyExistsException ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleBadCredentialsException(BadCredentialsException ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    /*extends DataAccessException*/
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleSqlException(DataIntegrityViolationException ex) {
        ApiGenericResponse<Object> response = buildErrorResponse("Data integrity violation. " + ex.getCause().getCause().getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleAllExceptions(InsufficientAuthenticationException ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage() + ". You are not allowed to perform this action");
        return new ResponseEntity<>(response, FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleAllExceptions(Exception ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(Arrays.toString(ex.getStackTrace()) + ". Consider using a custom exception handler");
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }
}
