package com.schoolmanagement.schoolmanagement.controller;

import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.model.ApiResponse;
import com.schoolmanagement.schoolmanagement.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/districts")
public class DistrictController {

    @Autowired
    DistrictService districtService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> uploadDistricts(MultipartFile districtsFile) throws Exception {
        return ok(new ApiResponse<>(districtService.uploadDistricts(districtsFile)));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getDistrictsByStateId(@RequestParam("stateId") Long stateId) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(districtService.getDistrictsByStateId(stateId)));
    }
}
