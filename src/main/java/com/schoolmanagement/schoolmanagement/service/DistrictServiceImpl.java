package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.District;
import com.schoolmanagement.schoolmanagement.entity.State;
import com.schoolmanagement.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.DistrictRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.schoolmanagement.schoolmanagement.constant.FileTypes.EXCEL;
import static com.schoolmanagement.schoolmanagement.constant.Messages.*;
import static com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods.isFileTypeValid;

@Transactional
@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    StateService stateService;

    @Override
    public String uploadDistricts(MultipartFile districtsFile) throws BadRequestException, IOException, ResourceNotFoundException {
        if (!isFileTypeValid(EXCEL, districtsFile)) {
            throw new BadRequestException(WRONG_FILE_TYPE);
        }

        List<String> errors = errorsInFile(districtsFile);

        if (errors.size() > 0) {
            throw new BadRequestException(ERRORS_IN_FILE, errors);
        }

        List<District> districts = dataToDistrictEntityMapper(districtsFile);
        districtRepository.saveAll(districts);

        return UPLOAD_SUCCESSFUL;
    }

    @Override
    public List<District> getDistrictsByStateId(Long stateId) throws ResourceNotFoundException {
        List<District> districts = districtRepository.findByStateId(stateId);
        if (districts.isEmpty()) {
            throw new ResourceNotFoundException(NO_DISTRICTS_EXIST + " for state Id : " + stateId);
        }

        return districts;
    }

    @Override
    public Long getStateIdByDistrictId(Long districtId) throws ResourceNotFoundException {
        Optional<District> district = districtRepository.findById(districtId);
        if (!district.isPresent())
            throw new ResourceNotFoundException(DISTRICT_NOT_FOUND + " with id : " + districtId);

        return district.get().getState().getId();
    }

    @Override
    public District getDistrictById(Long id) throws ResourceNotFoundException {
        Optional<District> district = districtRepository.findById(id);
        if (!district.isPresent()) {
            throw new ResourceNotFoundException(DISTRICT_NOT_FOUND + " with id : " + id);
        }
        return district.get();
    }

    private List<String> errorsInFile(MultipartFile districtsFile) {
        List<String> errors = new ArrayList();

        try {
            InputStream inputStream = districtsFile.getInputStream();

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            // check if file is empty
            if (sheet.getFirstRowNum() == sheet.getLastRowNum()) {
                errors.add(INPUT_FILE + " : " + FILE_IS_EMPTY);

                return errors;
            }

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell districtCell = row.getCell(0);
                Cell stateCell = row.getCell(1);
                //Cell stateCell = row.getCell(1);

                //ignore the first row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // ignore empty rows
                if (districtCell == null && stateCell == null) {
                    continue;
                }

                if (districtCell == null && stateCell != null) {
                    errors.add("Row " + (row.getRowNum() + 1) + " : " + DISTRICT_IS_BLANK);
                }

                if (districtCell != null && stateCell == null) {
                    errors.add("Row " + (row.getRowNum() + 1) + " : " + STATE_IS_BLANK);
                }

            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return errors;
    }

    private List<District> dataToDistrictEntityMapper(MultipartFile districtsFile) throws BadRequestException, ResourceNotFoundException {

        List<District> districts = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try {
            InputStream inputStream = districtsFile.getInputStream();

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell districtCell = row.getCell(0);
                Cell stateCell = row.getCell(1);

                // Ignore the first row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Ignore empty rows
                if (districtCell == null && stateCell == null) {
                    continue;
                }

                State state;
                try {
                    state = stateService.getStateByName(stateCell.getStringCellValue());
                } catch (ResourceNotFoundException exception) {
                    errors.add("Row " + (row.getRowNum() + 1) + " : " + STATE_NOT_FOUND);
                    continue;
                }

                districts.add(new District(districtCell.getStringCellValue(), state));
            }
            if (errors.size() > 0) {
                throw new BadRequestException(ERRORS_IN_FILE, errors);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return districts;
    }

}
