package com.schoolmanagement.schoolmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private ApiError error;
    private T data;

    public ApiResponse(final T data) {
        this();
        this.data = data;
    }

    public ApiResponse(final ApiError error) {
        this();
        this.error = error;
    }
}
