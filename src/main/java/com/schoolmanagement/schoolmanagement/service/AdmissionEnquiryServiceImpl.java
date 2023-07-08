package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.constant.enums.AdmissionEnquiryFilterCriteria;
import com.schoolmanagement.schoolmanagement.constant.enums.SortOrder;
import com.schoolmanagement.schoolmanagement.entity.AdmissionEnquiry;
import com.schoolmanagement.schoolmanagement.entity.Country;
import com.schoolmanagement.schoolmanagement.entity.District;
import com.schoolmanagement.schoolmanagement.entity.State;
import com.schoolmanagement.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryFilterCriteriaInput;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryRequestBody;
import com.schoolmanagement.schoolmanagement.model.AdmissionEnquiryResponseBody;
import com.schoolmanagement.schoolmanagement.repository.AdmissionEnquiryRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.schoolmanagement.schoolmanagement.constant.Messages.*;

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
        validateCreateRequestBody(admissionEnquiry);
        AdmissionEnquiry admissionEnquiryEntity = admissionEnquiryRepository.save(admissionEnquiry.toAdmissionEnquiryEntity());
        AdmissionEnquiryResponseBody admissionEnquiryResponseBody = new AdmissionEnquiryResponseBody();

        District districtProxy = getDistrictById(admissionEnquiryEntity.getAddress().getDistrict().getId());
        District district = (District) Hibernate.unproxy(districtProxy);

        State stateProxy = getStateById(admissionEnquiryEntity.getAddress().getState().getId());
        State state = (State) Hibernate.unproxy(stateProxy);

        Country countryProxy = getCountryById(admissionEnquiryEntity.getAddress().getCountry().getId());
        Country country = (Country) Hibernate.unproxy(countryProxy);

        admissionEnquiryEntity.getAddress().setDistrict(district);
        admissionEnquiryEntity.getAddress().setState(state);
        admissionEnquiryEntity.getAddress().setCountry(country);

        return admissionEnquiryResponseBody.entityToAdmissionEnquiryResponse(admissionEnquiryEntity);
    }

    @Override
    public Page<AdmissionEnquiryResponseBody> getAdmissionEnquiries(Integer pageNumber, Integer pageSize, String sortBy, SortOrder sortOrder, AdmissionEnquiryFilterCriteria filterBy, AdmissionEnquiryFilterCriteriaInput filterCriteria) throws BadRequestException {

        Boolean filterRequired = filterBy != null ? true : false;

        Page<AdmissionEnquiry> entityResponse = null;
        Pageable pageableRequest = null;

        switch (sortOrder) {
            case ASC:
                pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
                break;

            case DESC:
                pageableRequest = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
                break;
        }


        if (filterRequired) {
            validateFilterCriteria(filterBy, filterCriteria);

            switch (filterBy) {
                case ADMISSION_SEEKING_STANDARD:
                    entityResponse = admissionEnquiryRepository.findByAdmissionSeekingStandard(filterCriteria.getAdmissionSeekingStandard(), pageableRequest);
                    break;

                case ENQUIRED_DATE:
                    entityResponse = admissionEnquiryRepository.findByEnquiredDateBetween(filterCriteria.getStartDate(), filterCriteria.getEndDate(), pageableRequest);
                    break;

                case SEARCH_TEXT:
                    entityResponse = admissionEnquiryRepository.findByStudentNameContaining(filterCriteria.getSearchText(), pageableRequest);
            }
        } else {
            entityResponse = admissionEnquiryRepository.findAll(pageableRequest);
        }


        return entityResponse.map(entity -> {
            try {
                AdmissionEnquiryResponseBody admissionEnquiryResponseBody = new AdmissionEnquiryResponseBody();

                District districtProxy = getDistrictById(entity.getAddress().getDistrict().getId());
                District district = (District) Hibernate.unproxy(districtProxy);

                State stateProxy = getStateById(entity.getAddress().getState().getId());
                State state = (State) Hibernate.unproxy(stateProxy);

                Country countryProxy = getCountryById(entity.getAddress().getCountry().getId());
                Country country = (Country) Hibernate.unproxy(countryProxy);

                entity.getAddress().setDistrict(district);
                entity.getAddress().setState(state);
                entity.getAddress().setCountry(country);

                return admissionEnquiryResponseBody.entityToAdmissionEnquiryResponse(entity);

            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public AdmissionEnquiryResponseBody getAdmissionEnquiryDetailsById(Long enquiryId) throws ResourceNotFoundException {
        Optional<AdmissionEnquiry> admissionEnquiryEntity = admissionEnquiryRepository.findById(enquiryId);

        if (!admissionEnquiryEntity.isPresent()) {
            throw new ResourceNotFoundException(RECORD_NOT_FOUND);
        }
        AdmissionEnquiry admissionEnquiry = admissionEnquiryEntity.get();

        AdmissionEnquiryResponseBody admissionEnquiryResponseBody = new AdmissionEnquiryResponseBody();

        District districtProxy = getDistrictById(admissionEnquiry.getAddress().getDistrict().getId());
        District district = (District) Hibernate.unproxy(districtProxy);

        State stateProxy = getStateById(admissionEnquiry.getAddress().getState().getId());
        State state = (State) Hibernate.unproxy(stateProxy);

        Country countryProxy = getCountryById(admissionEnquiry.getAddress().getCountry().getId());
        Country country = (Country) Hibernate.unproxy(countryProxy);

        admissionEnquiry.getAddress().setDistrict(district);
        admissionEnquiry.getAddress().setState(state);
        admissionEnquiry.getAddress().setCountry(country);


        return admissionEnquiryResponseBody.entityToAdmissionEnquiryResponse(admissionEnquiry);
    }

    private void validateCreateRequestBody(AdmissionEnquiryRequestBody admissionEnquiry) throws BadRequestException, ResourceNotFoundException {
        Long stateId = districtService.getStateIdByDistrictId(admissionEnquiry.getDistrictId());
        if (!stateId.equals(admissionEnquiry.getStateId()))
            throw new BadRequestException(WRONG_DISTRICT_STATE_COMBINATION);

        Long countryId = stateService.getCountryIdByStateId(admissionEnquiry.getStateId());
        if (!countryId.equals(admissionEnquiry.getCountryId()))
            throw new BadRequestException(WRONG_STATE_COUNTRY_COMBINATION);
    }

    private Country getCountryById(Long id) throws ResourceNotFoundException {
        return countryService.getCountryById(id);
    }

    private State getStateById(Long id) throws ResourceNotFoundException {
        return stateService.getStateById(id);
    }

    private District getDistrictById(Long id) throws ResourceNotFoundException {
        return districtService.getDistrictById(id);
    }

    private void validateFilterCriteria(AdmissionEnquiryFilterCriteria filterBy, AdmissionEnquiryFilterCriteriaInput filterCriteria) throws BadRequestException {
        switch (filterBy) {
            case ADMISSION_SEEKING_STANDARD: {
                if (filterCriteria.getAdmissionSeekingStandard() == null)
                    throw new BadRequestException("Admission Seeking Standard is missing from request body");
                break;
            }

            case ENQUIRED_DATE: {
                if (filterCriteria.getStartDate() == null)
                    throw new BadRequestException("Start date is missing in the request body");

                if (filterCriteria.getEndDate() == null)
                    throw new BadRequestException("End date is missing in the request body");

                break;
            }

            case SEARCH_TEXT: {
                if (filterCriteria.getSearchText() == null)
                    throw new BadRequestException("Search Text is missing in request body");
                break;
            }
        }
    }
}
