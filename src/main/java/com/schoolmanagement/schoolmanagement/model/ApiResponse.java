package com.schoolmanagement.schoolmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private List<ApiError> errors;
    private T data;

    public ApiResponse(final T data) {
        this();
        this.data = data;
    }

    public ApiResponse(final ApiError error) {
        this();
        this.addError(error);
    }

    public void addError(final ApiError error) {
        this.errors.add(error);
    }
}
