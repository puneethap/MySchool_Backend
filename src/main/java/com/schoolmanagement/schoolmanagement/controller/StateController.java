package com.schoolmanagement.schoolmanagement.controller;

import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.model.ApiResponse;
import com.schoolmanagement.schoolmanagement.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/states")
public class StateController {

    @Autowired
    StateService stateService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> uploadStates(MultipartFile statesFile) throws Exception {
        return ok(new ApiResponse<>(stateService.uploadStates(statesFile)));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getStatesByCountryId(@RequestParam("countryId") Long countryId) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(stateService.getStatesByCountryId(countryId)));
    }
}
