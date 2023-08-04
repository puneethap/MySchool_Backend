package com.schoolmanagement.schoolmanagement.model;

import com.schoolmanagement.schoolmanagement.entity.*;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static com.schoolmanagement.schoolmanagement.constant.Messages.*;


@Data
@Builder
public class AdmissionEnquiryRequestBody {

    @NotNull(message = "Student name " + IS_NULL)
    @NotBlank(message = "Student name " + IS_BLANK)
    private String studentName;

    @NotNull(message = "Admission seeking standard " + IS_NULL)
    @NotBlank(message = "Admission seeking standard " + IS_BLANK)
    private String admissionSeekingStandard;

    @NotNull(message = "Parent name " + IS_NULL)
    @NotBlank(message = "Parent name " + IS_BLANK)
    private String parentName;

    @NotNull(message = "Phone number " + IS_NULL)
    @NotBlank(message = "Phone number " + IS_BLANK)
    @Size(min = 10, max = 10, message = INVALID_PHONE_NUMBER)
    private String phoneNumber;

    @NotNull(message = EMAIL_IS_NULL)
    @NotBlank(message = EMAIL_IS_BLANK)
    @Email(message = INVALID_EMAIL)
    private String emailId;

    @NotNull(message = "Address " + IS_NULL)
    @NotBlank(message = "Address " + IS_BLANK)
    private String addressText;

    @NotNull(message = "City " + IS_NULL)
    @NotBlank(message = "City " + IS_BLANK)
    private String city;

    @NotNull(message = "PIN Code " + IS_NULL)
    @NotBlank(message = "PIN Code " + IS_BLANK)
    @Size(min = 6, max = 6, message = INVALID_PIN_CODE)
    private String pinCode;

    @NotNull(message = "District Id " + IS_NULL)
    private Long districtId;

    @NotNull(message = "State Id" + IS_NULL)
    private Long stateId;

    @NotNull(message = "Country Id " + IS_NULL)
    private Long countryId;

    public AdmissionEnquiry toAdmissionEnquiryEntity() {
        AdmissionEnquiry admissionEnquiry = AdmissionEnquiry
                .builder()
                .studentName(this.studentName)
                .admissionSeekingStandard(this.admissionSeekingStandard)
                .parentName(this.parentName)
                .phoneNumber(Long.parseLong(this.phoneNumber))
                .emailId(this.emailId)
                .enquiredDate(LocalDate.now())
                .address(Address
                        .builder()
                        .addressText(this.addressText)
                        .city(this.city)
                        .pinCode(Long.parseLong(this.pinCode))
                        .district(District.builder().id(this.districtId).build())
                        .state(State.builder().id(this.stateId).build())
                        .country(Country.builder().id(this.countryId).build())
                        .build()
                )
                .build();

        return admissionEnquiry;
    }
}
