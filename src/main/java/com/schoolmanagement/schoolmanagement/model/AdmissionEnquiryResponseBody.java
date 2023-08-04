package com.schoolmanagement.schoolmanagement.model;

import com.schoolmanagement.schoolmanagement.entity.AdmissionEnquiry;
import com.schoolmanagement.schoolmanagement.entity.Country;
import com.schoolmanagement.schoolmanagement.entity.District;
import com.schoolmanagement.schoolmanagement.entity.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionEnquiryResponseBody {
    private Long id;
    private String studentName;
    private String admissionSeekingStandard;
    private String parentName;
    private String phoneNumber;
    private String emailId;
    private LocalDate enquiredDate;
    private String addressText;
    private String city;
    private String pinCode;
    private District district;
    private State state;
    private Country country;

    public AdmissionEnquiryResponseBody entityToAdmissionEnquiryResponse(AdmissionEnquiry admissionEnquiryEntity) {
        this.id = admissionEnquiryEntity.getId();
        this.studentName = admissionEnquiryEntity.getStudentName();
        this.admissionSeekingStandard = admissionEnquiryEntity.getAdmissionSeekingStandard();
        this.parentName = admissionEnquiryEntity.getParentName();
        this.phoneNumber = admissionEnquiryEntity.getPhoneNumber().toString();
        this.emailId = admissionEnquiryEntity.getEmailId();
        this.enquiredDate = admissionEnquiryEntity.getEnquiredDate();
        this.addressText = admissionEnquiryEntity.getAddress().getAddressText();
        this.city = admissionEnquiryEntity.getAddress().getCity();
        this.pinCode = admissionEnquiryEntity.getAddress().getPinCode().toString();
        this.district = admissionEnquiryEntity.getAddress().getDistrict();
        this.state = admissionEnquiryEntity.getAddress().getState();
        this.country = admissionEnquiryEntity.getAddress().getCountry();

        return this;

    }
}
