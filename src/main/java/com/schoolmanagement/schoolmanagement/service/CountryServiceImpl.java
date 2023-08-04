package com.schoolmanagement.schoolmanagement.service;

import com.schoolmanagement.schoolmanagement.entity.Country;
import com.schoolmanagement.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.schoolmanagement.repository.CountryRepository;
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
public class CountryServiceImpl implements CountryService {

    @Autowired
    CountryRepository countryRepository;

    @Override
    public String uploadCountries(MultipartFile countriesFile) throws Exception {
        if (!isFileTypeValid(EXCEL, countriesFile)) {
            throw new BadRequestException(WRONG_FILE_TYPE);
        }

        List<String> errors = errorsInFile(countriesFile);

        if (errors.size() > 0) {
            throw new BadRequestException(ERRORS_IN_FILE, errors);
        }

        List<Country> countries = dataToCountryEntityMapper(countriesFile);
        countryRepository.saveAll(countries);

        return UPLOAD_SUCCESSFUL;
    }

    @Override
    public Country getCountryByName(String countryName) throws ResourceNotFoundException {
        Optional<Country> country = Optional.ofNullable(countryRepository.findByName(countryName));
        if (!country.isPresent()) {
            throw new ResourceNotFoundException(COUNTRY_NOT_FOUND + " : " + countryName);
        }
        return country.get();
    }

    @Override
    public List<Country> getCountries() throws ResourceNotFoundException {
        Optional<List<Country>> countries = Optional.ofNullable(countryRepository.findAll());
        if (!countries.isPresent()) {
            throw new ResourceNotFoundException(NO_COUNTRIES_FOUND);
        }

        return countries.get();
    }

    @Override
    public Country getCountryById(Long id) throws ResourceNotFoundException {
        Optional<Country> country = countryRepository.findById(id);
        if (!country.isPresent()) {
            throw new ResourceNotFoundException(COUNTRY_NOT_FOUND + " : " + id);
        }

        return country.get();
    }

    private List<Country> dataToCountryEntityMapper(MultipartFile countriesFile) throws Exception {

        List<Country> countries = new ArrayList<>();
        try {

            InputStream inputStream = countriesFile.getInputStream();

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell countryCell = row.getCell(0);

                // Ignore the first row
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Ignore empty rows

                if (countryCell == null) {
                    continue;
                }

                countries.add(new Country(countryCell.getStringCellValue()));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception(exception.getMessage());
        }
        return countries;
    }

    private List<String> errorsInFile(MultipartFile countriesFile) {
        List<String> errors = new ArrayList();

        try {
            InputStream inputStream = countriesFile.getInputStream();

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            // check if file is empty
            if (sheet.getFirstRowNum() == sheet.getLastRowNum()) {
                errors.add(INPUT_FILE + " : " + FILE_IS_EMPTY);

                return errors;
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return errors;
    }

}
