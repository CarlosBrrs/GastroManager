package com.kaiho.gastromanager.infrastructure.common.exceptionhandler;

import com.kaiho.gastromanager.domain.common.exception.EntityDoesNotExist;
import com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.kaiho.gastromanager.infrastructure.common.model.ApiGenericResponse.buildErrorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiGenericResponse<Object>> handleAllExceptions(Exception ex) {
        ApiGenericResponse<Object> response = buildErrorResponse(ex.getMessage() + ". Consider using a custom exception handler");
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }
}
