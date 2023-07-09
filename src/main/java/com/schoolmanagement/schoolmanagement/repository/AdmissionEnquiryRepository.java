package com.schoolmanagement.schoolmanagement.repository;

import com.schoolmanagement.schoolmanagement.entity.AdmissionEnquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionEnquiryRepository extends JpaRepository<AdmissionEnquiry, Long>, JpaSpecificationExecutor<AdmissionEnquiry> {
}
