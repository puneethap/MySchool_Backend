package com.schoolmanagement.schoolmanagement.repository;

import com.schoolmanagement.schoolmanagement.entity.AdmissionEnquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AdmissionEnquiryRepository extends JpaRepository<AdmissionEnquiry, Long> {
    Page<AdmissionEnquiry> findByAdmissionSeekingStandard(String admissionSeekingStandard, Pageable pageable);

    Page<AdmissionEnquiry> findByEnquiredDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<AdmissionEnquiry> findByStudentNameContaining(String searchText, Pageable pageable);
}
