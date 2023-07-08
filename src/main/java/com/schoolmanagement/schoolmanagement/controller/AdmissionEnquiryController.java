package com.schoolmanagement.schoolmanagement.controller;

import com.schoolmanagement.schoolmanagement.constant.enums.AdmissionEnquiryFilterCriteria;
import com.schoolmanagement.schoolmanagement.constant.enums.SortOrder;
import com.schoolmanagement.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryFilterCriteriaInput;
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
    public ResponseEntity<ApiResponse> getAdmissionEnquiries(@RequestParam
                                                             Integer pageNumber,
                                                             @RequestParam
                                                             Integer pageSize,
                                                             @RequestParam(defaultValue = "enquiredDate")
                                                             String sortBy,
                                                             @RequestParam(defaultValue = "DESC")
                                                             SortOrder sortOrder,
                                                             @RequestParam(required = false)
                                                             AdmissionEnquiryFilterCriteria filterBy,
                                                             @RequestBody(required = false)
                                                             AdmissionEnquiryFilterCriteriaInput filterCriteria) throws BadRequestException {
        return ok(new ApiResponse<>(admissionEnquiryService.getAdmissionEnquiries(pageNumber, pageSize, sortBy, sortOrder, filterBy, filterCriteria)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAdmissionEnquiryDetailsById(@PathVariable("id") Long enquiryId) throws ResourceNotFoundException {
        return ok(new ApiResponse<>(admissionEnquiryService.getAdmissionEnquiryDetailsById(enquiryId)));
    }
}
