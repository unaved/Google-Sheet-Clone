package com.example.webspreadsheet.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DataQualityServiceTest {

    @Autowired
    private DataQualityService dataQualityService;

    @Test
    void testTrimFunction() {
        assertEquals("test", dataQualityService.trim("  test  "));
    }

    @Test
    void testUpperFunction() {
        assertEquals("TEST", dataQualityService.upper("test"));
    }

    // Add more tests for other functions
} 