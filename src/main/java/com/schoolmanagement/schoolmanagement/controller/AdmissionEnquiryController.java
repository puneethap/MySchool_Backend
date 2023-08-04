package com.schoolmanagement.schoolmanagement.controller;

import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryRequestBody;
import com.schoolmanagement.schoolmanagement.model.ApiResponse;
import com.schoolmanagement.schoolmanagement.service.AdmissionEnquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admission-enquiry")
@Validated
public class AdmissionEnquiryController {

    @Autowired
    AdmissionEnquiryService admissionEnquiryService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createAdmissionEnquiry(@Valid @RequestBody AdmissionEnquiryRequestBody admissionEnquiry) throws Exception {
        return new ResponseEntity(new ApiResponse(admissionEnquiryService.createAdmissionEnquiry(admissionEnquiry)), CREATED);
    }
}
