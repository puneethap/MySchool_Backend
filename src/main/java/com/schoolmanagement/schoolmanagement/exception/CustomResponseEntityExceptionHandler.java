package com.schoolmanagement.schoolmanagement.exception;

import com.schoolmanagement.schoolmanagement.model.ApiError;
import com.schoolmanagement.schoolmanagement.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, resourceNotFoundException.getMessage());
        ApiResponse apiResponse = new ApiResponse<>(apiError);
        return ResponseEntity.status(apiError.getStatus()).body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> genericException(Exception exception) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        ApiResponse apiResponse = new ApiResponse<>(apiError);
        return ResponseEntity.status(apiError.getStatus()).body(apiResponse);
    }
}
