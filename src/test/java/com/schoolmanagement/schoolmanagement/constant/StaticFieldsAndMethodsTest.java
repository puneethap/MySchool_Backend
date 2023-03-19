package com.schoolmanagement.schoolmanagement.constant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.schoolmanagement.schoolmanagement.constant.FileMimeTypes.EXCEL_TYPE;
import static com.schoolmanagement.schoolmanagement.constant.FileTypes.EXCEL;
import static com.schoolmanagement.schoolmanagement.constant.StaticFieldsAndMethods.isTokenExpired;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StaticFieldsAndMethodsTest {

    private static final int OTP_LENGTH = 4;

    @Test
    void generateOTP() {
        String otp = StaticFieldsAndMethods.generateOTP(OTP_LENGTH);
        assertEquals(OTP_LENGTH, otp.length());
    }

    @Test
    void isTokenExpired_when_token_not_expired() {
        LocalDateTime tokenCreationDateTime = LocalDateTime.now();
        boolean isTokenExpired = isTokenExpired(tokenCreationDateTime);

        assertFalse(isTokenExpired);
    }

    @Test
    void isTokenExpired_when_token_is_expired() {
        LocalDateTime tokenCreationDateTime = LocalDateTime.now().minusMinutes(StaticFieldsAndMethods.EXPIRE_TOKEN_AFTER_MINUTES);
        boolean isTokenExpired = isTokenExpired(tokenCreationDateTime);

        assertTrue(isTokenExpired);
    }

    @Test
    void isFileTypeValid() {
        Map<String, MultipartFile> files = new HashMap<>();

        MultipartFile dummyFile = new MockMultipartFile("dummyFile.dummy",
                "dummyFile.dummy",
                "dummyMimeType",
                "This is dummy file".getBytes(StandardCharsets.UTF_8));

        MultipartFile excelFile = new MockMultipartFile("actualFile.xlsx",
                "actualFile.xlsx",
                EXCEL_TYPE,
                "This is excel file".getBytes(StandardCharsets.UTF_8));

        files.put(EXCEL, excelFile);

        files.forEach(
                (key, value) -> {
                    assertTrue(StaticFieldsAndMethods.isFileTypeValid(key, value));
                    assertFalse(StaticFieldsAndMethods.isFileTypeValid(key, dummyFile));
                });

        assertFalse(StaticFieldsAndMethods.isFileTypeValid("dummy", dummyFile));
    }
}