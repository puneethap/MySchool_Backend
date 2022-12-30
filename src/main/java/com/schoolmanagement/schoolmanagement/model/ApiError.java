package com.schoolmanagement.schoolmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<?> details;

    public ApiError(final String message) {
        this.message = message;
    }

    public ApiError(final HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
