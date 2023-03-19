package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.Country;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CountryService {
    String uploadCountries(MultipartFile countriesFile) throws Exception;

    Country getCountryByName(String countryName) throws ResourceNotFoundException;

    List<Country> getCountries() throws ResourceNotFoundException;

    Country getCountryById(Long countryId) throws ResourceNotFoundException;
}
