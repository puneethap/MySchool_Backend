package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.AdmissionEnquiry;
import com.schoolmanagement.schoolmanagement.entity.Country;
import com.schoolmanagement.schoolmanagement.entity.District;
import com.schoolmanagement.schoolmanagement.entity.State;
import com.schoolmanagement.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryRequestBody;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryResponseBody;
import com.schoolmanagement.schoolmanagement.repository.AdmissionEnquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.schoolmanagement.schoolmanagement.constant.Messages.WRONG_DISTRICT_STATE_COMBINATION;
import static com.schoolmanagement.schoolmanagement.constant.Messages.WRONG_STATE_COUNTRY_COMBINATION;

@Service
public class AdmissionEnquiryServiceImpl implements AdmissionEnquiryService {

    @Autowired
    AdmissionEnquiryRepository admissionEnquiryRepository;

    @Autowired
    DistrictService districtService;

    @Autowired
    StateService stateService;

    @Autowired
    CountryService countryService;

    @Transactional
    @Override
    public AdmissionEnquiryResponseBody createAdmissionEnquiry(AdmissionEnquiryRequestBody admissionEnquiry) throws BadRequestException, ResourceNotFoundException {
        validateFields(admissionEnquiry);
        AdmissionEnquiry admissionEnquiryEntity = admissionEnquiryRepository.save(admissionEnquiry.toAdmissionEnquiryEntity());

        District district = districtService.getDistrictById(admissionEnquiryEntity.getAddress().getDistrict().getId());
        State state = stateService.getStateById(admissionEnquiryEntity.getAddress().getState().getId());
        Country country = countryService.getCountryById(admissionEnquiryEntity.getAddress().getCountry().getId());

        admissionEnquiryEntity.getAddress().getDistrict().setName(district.getName());
        admissionEnquiryEntity.getAddress().getDistrict().setState(state);

        admissionEnquiryEntity.getAddress().getState().setName(state.getName());
        admissionEnquiryEntity.getAddress().getState().setCountry(country);

        admissionEnquiryEntity.getAddress().getCountry().setName(country.getName());

        AdmissionEnquiryResponseBody responseBody = new AdmissionEnquiryResponseBody();

        return responseBody.entityToAdmissionEnquiryResponse(admissionEnquiryEntity);
    }

    private void validateFields(AdmissionEnquiryRequestBody admissionEnquiry) throws BadRequestException, ResourceNotFoundException {
        Long stateId = districtService.getStateIdByDistrictId(admissionEnquiry.getDistrictId());
        if (!stateId.equals(admissionEnquiry.getStateId()))
            throw new BadRequestException(WRONG_DISTRICT_STATE_COMBINATION);

        Long countryId = stateService.getCountryIdByStateId(admissionEnquiry.getStateId());
        if (!countryId.equals(admissionEnquiry.getCountryId()))
            throw new BadRequestException(WRONG_STATE_COUNTRY_COMBINATION);
    }
}
