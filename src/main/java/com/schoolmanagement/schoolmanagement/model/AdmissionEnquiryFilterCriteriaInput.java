package com.schoolmanagement.schoolmanagement.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdmissionEnquiryFilterCriteriaInput {
    String searchText;
    String admissionSeekingStandard;
    LocalDate startDate;
    LocalDate endDate;
}
