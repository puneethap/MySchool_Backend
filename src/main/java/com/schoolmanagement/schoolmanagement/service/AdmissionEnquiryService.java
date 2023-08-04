package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.enums.SortOrder;
import com.schoolmanagement.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.filter.AdmissionEnquiryFilterCriteria;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryRequestBody;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryResponseBody;
import org.springframework.data.domain.Page;

public interface AdmissionEnquiryService {
    AdmissionEnquiryResponseBody createAdmissionEnquiry(AdmissionEnquiryRequestBody admissionEnquiry) throws BadRequestException, ResourceNotFoundException;

    Page<AdmissionEnquiryResponseBody> getAdmissionEnquiries(Integer pageNumber, Integer pageSize, String sortBy, SortOrder sortOrder, AdmissionEnquiryFilterCriteria admissionEnquiryFilter);

    AdmissionEnquiryResponseBody getAdmissionEnquiryDetailsById(Long enquiryId) throws ResourceNotFoundException;
}
