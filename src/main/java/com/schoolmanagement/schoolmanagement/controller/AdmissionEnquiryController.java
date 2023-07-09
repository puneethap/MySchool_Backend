package com.schoolmanagement.schoolmanagement.controller;

import com.schoolmanagement.schoolmanagement.enums.SortOrder;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.filter.AdmissionEnquiryFilterCriteria;
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
import static org.springframework.http.ResponseEntity.ok;

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

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAdmissionEnquiries(@RequestParam(defaultValue = "0")
                                                             Integer pageNumber,
                                                             @RequestParam(defaultValue = "10")
                                                             Integer pageSize,
                                                             @RequestParam(defaultValue = "enquiredDate")
                                                             String sortBy,
                                                             @RequestParam(defaultValue = "DESC")
                                                             SortOrder sortOrder,
                                                             @RequestBody(required = false)
                                                             AdmissionEnquiryFilterCriteria filterCriteria) {
        return ok(new ApiResponse<>(admissionEnquiryService.getAdmissionEnquiries(pageNumber, pageSize, sortBy, sortOrder, filterCriteria)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAdmissionEnquiryDetailsById(@PathVariable("id") Long enquiryId) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(admissionEnquiryService.getAdmissionEnquiryDetailsById(enquiryId)));
    }
}
