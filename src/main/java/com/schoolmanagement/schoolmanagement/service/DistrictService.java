package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.District;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DistrictService {

    String uploadDistricts(MultipartFile countriesFile) throws Exception;

    List<District> getDistrictsByStateId(Long stateId) throws ResourceNotFoundException;
}
