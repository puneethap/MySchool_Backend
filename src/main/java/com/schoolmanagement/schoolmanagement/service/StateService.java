package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.State;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StateService {

    String uploadStates(MultipartFile countriesFile) throws Exception;

    State getStateByName(String stringCellValue) throws ResourceNotFoundException;

    List<State> getStatesByCountryId(Long countryId) throws ResourceNotFoundException;
}
