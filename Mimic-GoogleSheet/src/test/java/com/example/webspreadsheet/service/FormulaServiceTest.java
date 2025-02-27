package com.example.webspreadsheet.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FormulaServiceTest {

    @Autowired
    private FormulaService formulaService;

    @Test
    void testSumFunction() {
        List<String> values = Arrays.asList("1", "2", "3");
        assertEquals("6", formulaService.evaluateFormula("=SUM(A1:A3)", values));
    }

    @Test
    void testAverageFunction() {
        List<String> values = Arrays.asList("1", "2", "3");
        assertEquals("2.00", formulaService.evaluateFormula("=AVERAGE(A1:A3)", values));
    }

    // Add more tests for other functions
} 