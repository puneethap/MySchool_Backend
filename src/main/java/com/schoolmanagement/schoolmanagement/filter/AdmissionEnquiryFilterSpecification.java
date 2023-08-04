package com.schoolmanagement.schoolmanagement.filter;

import com.schoolmanagement.schoolmanagement.entity.AdmissionEnquiry;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AdmissionEnquiryFilterSpecification {
    public static final String SEARCH_TEXT = "studentName";
    public static final String ADMISSION_SEEKING_STANDARD = "admissionSeekingStandard";
    public static final String ENQUIRED_DATE = "enquiredDate";

    public static Specification<AdmissionEnquiry> filterBy(AdmissionEnquiryFilterCriteria admissionEnquiryFilter) {
        return Specification
                .where(hasSearchText(admissionEnquiryFilter.getSearchText()))
                .and(hasAdmissionSeekingStandard(admissionEnquiryFilter.getAdmissionSeekingStandard()))
                .and(hasEnquiredDateAfter(admissionEnquiryFilter.getEnquiredDateAfter()))
                .and(hasEnquiredDateBefore(admissionEnquiryFilter.getEnquiredDateBefore()));
    }

    private static Specification<AdmissionEnquiry> hasSearchText(String searchText) {
        return ((root, query, cb) -> searchText == null || searchText.isEmpty() ? cb.conjunction() : cb.like(root.get(SEARCH_TEXT), "%" + searchText + "%"));
    }

    private static Specification<AdmissionEnquiry> hasAdmissionSeekingStandard(String admissionSeekingStandard) {
        return ((root, query, cb) -> admissionSeekingStandard == null || admissionSeekingStandard.isEmpty() ? cb.conjunction() : cb.equal(root.get(ADMISSION_SEEKING_STANDARD), admissionSeekingStandard));
    }

    private static Specification<AdmissionEnquiry> hasEnquiredDateAfter(LocalDate enquiredDateAfter) {
        return ((root, query, cb) -> enquiredDateAfter == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get(ENQUIRED_DATE), enquiredDateAfter));
    }

    private static Specification<AdmissionEnquiry> hasEnquiredDateBefore(LocalDate enquiredDateBefore) {
        return ((root, query, cb) -> enquiredDateBefore == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get(ENQUIRED_DATE), enquiredDateBefore));
    }
}
