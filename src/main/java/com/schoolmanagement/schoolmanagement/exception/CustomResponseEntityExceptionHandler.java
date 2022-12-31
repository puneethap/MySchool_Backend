package com.schoolmanagement.schoolmanagement.exception;

import com.schoolmanagement.schoolmanagement.model.ApiError;
import com.schoolmanagement.schoolmanagement.model.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        List<Map> errorDetails = new ArrayList<>();
        errorDetails.add(errors);

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation Errors", errorDetails);
        ApiResponse apiResponse = new ApiResponse<>(apiError);
        return ResponseEntity.status(apiError.getStatus()).body(apiResponse);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = ex.getParameterName() + " is missing";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage);
        ApiResponse apiResponse = new ApiResponse<>(apiError);
        return ResponseEntity.status(apiError.getStatus()).body(apiResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> constraintViolationException(ConstraintViolationException exception) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage());
        ApiResponse apiResponse = new ApiResponse<>(apiError);
        return ResponseEntity.status(apiError.getStatus()).body(apiResponse);
    }
}
