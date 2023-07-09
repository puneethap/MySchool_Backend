package com.schoolmanagement.schoolmanagement.filter;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdmissionEnquiryFilterCriteria {
    private String searchText;
    private String admissionSeekingStandard;
    private LocalDate enquiredDateAfter;
    private LocalDate enquiredDateBefore;
}
