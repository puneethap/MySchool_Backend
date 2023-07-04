package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.*;
import com.schoolmanagement.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryRequestBody;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryResponseBody;
import com.schoolmanagement.schoolmanagement.repository.AdmissionEnquiryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.schoolmanagement.schoolmanagement.constant.Messages.WRONG_DISTRICT_STATE_COMBINATION;
import static com.schoolmanagement.schoolmanagement.constant.Messages.WRONG_STATE_COUNTRY_COMBINATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdmissionEnquiryServiceImplTest {

    @Autowired
    AdmissionEnquiryService admissionEnquiryService;

    @MockBean
    AdmissionEnquiryRepository admissionEnquiryRepository;

    @MockBean
    DistrictService districtService;

    @MockBean
    StateService stateService;

    @MockBean
    CountryService countryService;

    AdmissionEnquiryRequestBody admissionEnquiryRequestBody;
    AdmissionEnquiry admissionEnquiryEntity;

    Country country;
    State state;
    District district;

    Long incorrectStateId = 2L;
    Long incorrectCountryId = 2L;

    @BeforeEach
    void setUp() {
        country = new Country(1L, "India");
        state = new State(1L, "Karnataka", country);
        district = new District(1L, "Bagalkot", state);
        admissionEnquiryRequestBody = AdmissionEnquiryRequestBody
                .builder()
                .studentName("Vikas")
                .admissionSeekingStandard("LKG")
                .parentName("Shivappa")
                .phoneNumber("9999999999")
                .emailId("vikas@email.com")
                .addressText("#4, 1st cross, 2nd main")
                .city("Bilagi")
                .pinCode("999999")
                .districtId(district.getId())
                .stateId(state.getId())
                .countryId(country.getId())
                .build();

        admissionEnquiryEntity = AdmissionEnquiry
                .builder()
                .id(1L)
                .studentName(admissionEnquiryRequestBody.getStudentName())
                .admissionSeekingStandard(admissionEnquiryRequestBody.getAdmissionSeekingStandard())
                .parentName(admissionEnquiryRequestBody.getParentName())
                .phoneNumber(Long.parseLong(admissionEnquiryRequestBody.getPhoneNumber()))
                .emailId(admissionEnquiryRequestBody.getEmailId())
                .address(Address
                        .builder()
                        .addressText(admissionEnquiryRequestBody.getAddressText())
                        .city(admissionEnquiryRequestBody.getCity())
                        .pinCode(Long.parseLong(admissionEnquiryRequestBody.getPinCode()))
                        .district(District.builder().id(district.getId()).build())
                        .state(State.builder().id(state.getId()).build())
                        .country(Country.builder().id(country.getId()).build())
                        .build()
                )
                .build();
    }

    @Test
    void createAdmissionEnquiry() throws ResourceNotFoundException, BadRequestException {
        when(districtService.getStateIdByDistrictId(admissionEnquiryRequestBody.getDistrictId())).thenReturn(admissionEnquiryRequestBody.getStateId());
        when(stateService.getCountryIdByStateId(admissionEnquiryRequestBody.getStateId())).thenReturn(admissionEnquiryRequestBody.getCountryId());
        when(admissionEnquiryRepository.save(any())).thenReturn(admissionEnquiryEntity);
        when(districtService.getDistrictById(admissionEnquiryEntity.getAddress().getDistrict().getId())).thenReturn(district);
        when(stateService.getStateById(admissionEnquiryEntity.getAddress().getState().getId())).thenReturn(state);
        when(countryService.getCountryById(admissionEnquiryEntity.getAddress().getCountry().getId())).thenReturn(country);

        AdmissionEnquiryResponseBody response = admissionEnquiryService.createAdmissionEnquiry(admissionEnquiryRequestBody);

        assertEquals(response.getId(), admissionEnquiryEntity.getId());
        assertEquals(response.getStudentName(), admissionEnquiryEntity.getStudentName());
        assertEquals(response.getAdmissionSeekingStandard(), admissionEnquiryEntity.getAdmissionSeekingStandard());
        assertEquals(response.getParentName(), admissionEnquiryEntity.getParentName());
        assertEquals(response.getPhoneNumber(), admissionEnquiryEntity.getPhoneNumber().toString());
        assertEquals(response.getEmailId(), admissionEnquiryEntity.getEmailId());
        assertEquals(response.getAddressText(), admissionEnquiryEntity.getAddress().getAddressText());
        assertEquals(response.getCity(), admissionEnquiryEntity.getAddress().getCity());
        assertEquals(response.getPinCode(), admissionEnquiryEntity.getAddress().getPinCode().toString());
        assertEquals(response.getDistrict().getId(), admissionEnquiryEntity.getAddress().getDistrict().getId());
        assertEquals(response.getState().getId(), admissionEnquiryEntity.getAddress().getState().getId());
        assertEquals(response.getCountry().getId(), admissionEnquiryEntity.getAddress().getCountry().getId());
    }

    @Test
    void createAdmissionEnquiry_wrong_district_state_combination() throws ResourceNotFoundException {
        when(districtService.getStateIdByDistrictId(admissionEnquiryRequestBody.getDistrictId())).thenReturn(incorrectStateId);
        when(stateService.getCountryIdByStateId(admissionEnquiryRequestBody.getStateId())).thenReturn(admissionEnquiryRequestBody.getCountryId());
        when(admissionEnquiryRepository.save(any())).thenReturn(admissionEnquiryEntity);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> admissionEnquiryService.createAdmissionEnquiry(admissionEnquiryRequestBody));
        assertEquals(WRONG_DISTRICT_STATE_COMBINATION, exception.getMessage());
    }

    @Test
    void createAdmissionEnquiry_wrong_state_country_combination() throws ResourceNotFoundException {
        when(districtService.getStateIdByDistrictId(admissionEnquiryRequestBody.getDistrictId())).thenReturn(admissionEnquiryRequestBody.getStateId());
        when(stateService.getCountryIdByStateId(admissionEnquiryRequestBody.getStateId())).thenReturn(incorrectCountryId);
        when(admissionEnquiryRepository.save(any())).thenReturn(admissionEnquiryEntity);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> admissionEnquiryService.createAdmissionEnquiry(admissionEnquiryRequestBody));
        assertEquals(WRONG_STATE_COUNTRY_COMBINATION, exception.getMessage());
    }
}