package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.Country;
import com.schoolmanagement.schoolmanagement.entity.State;
import com.schoolmanagement.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.StateRepository;
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
public class StateServiceImpl implements StateService {

    @Autowired
    StateRepository stateRepository;

    @Autowired
    CountryService countryService;

    @Override
    public String uploadStates(MultipartFile statesFile) throws BadRequestException, ResourceNotFoundException {
        if (!isFileTypeValid(EXCEL, statesFile)) {
            throw new BadRequestException(WRONG_FILE_TYPE);
        }

        List<String> errors = errorsInFile(statesFile);

        if (errors.size() > 0) {
            throw new BadRequestException(ERRORS_IN_FILE, errors);
        }

        List<State> states = dataToStateEntityMapper(statesFile);
        stateRepository.saveAll(states);

        return UPLOAD_SUCCESSFUL;
    }

    @Override
    public State getStateByName(String stateName) throws ResourceNotFoundException {
        Optional<State> state = Optional.ofNullable(stateRepository.findByName(stateName));
        if (!state.isPresent()) {
            throw new ResourceNotFoundException(STATE_IS_NOT_PRESENT + " with name : " + stateName);
        }
        return state.get();
    }

    @Override
    public List<State> getStatesByCountryId(Long countryId) throws ResourceNotFoundException {
        List<State> states = stateRepository.findByCountryId(countryId);
        if (states.isEmpty()) {
            throw new ResourceNotFoundException(NO_STATES_EXIST + " for country Id : " + countryId);
        }

        return states;
    }

    private List<String> errorsInFile(MultipartFile statesFile) {
        List<String> errors = new ArrayList();

        try {
            InputStream inputStream = statesFile.getInputStream();

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
                Cell stateCell = row.getCell(0);
                Cell countryCell = row.getCell(1);

                // Ignore the first row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Ignore empty rows
                if (stateCell == null && countryCell == null) {
                    continue;
                }

                if (stateCell == null && countryCell != null) {
                    errors.add("Row " + (row.getRowNum() + 1) + " : " + STATE_IS_BLANK);
                }

                if (stateCell != null && countryCell == null) {
                    errors.add("Row " + (row.getRowNum() + 1) + " : " + COUNTRY_IS_BLANK);
                }

            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return errors;
    }

    private List<State> dataToStateEntityMapper(MultipartFile statesFile) throws BadRequestException, ResourceNotFoundException {

        List<State> states = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try {
            InputStream inputStream = statesFile.getInputStream();

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell stateCell = row.getCell(0);
                Cell countryCell = row.getCell(1);

                // Ignore the first row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Ignore empty rows
                if (stateCell == null && countryCell == null) {
                    continue;
                }

                Country country;
                try {
                    country = countryService.getCountryByName(countryCell.getStringCellValue());
                } catch (ResourceNotFoundException exception) {
                    errors.add("Row " + (row.getRowNum() + 1) + " : " + INVALID_COUNTRY);
                    continue;
                }

                states.add(new State(stateCell.getStringCellValue(), country));
            }
            if (errors.size() > 0) {
                throw new BadRequestException(ERRORS_IN_FILE, errors);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return states;
    }

}
