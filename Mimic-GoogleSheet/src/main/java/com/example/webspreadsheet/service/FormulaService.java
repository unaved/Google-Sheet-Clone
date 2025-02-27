package com.example.webspreadsheet.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

@Service
public class FormulaService {

    public double calculateSum(List<String> values) {
        return parseNumericValues(values)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public double calculateAverage(List<String> values) {
        return parseNumericValues(values)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public double calculateMax(List<String> values) {
        return parseNumericValues(values)
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);
    }

    public double calculateMin(List<String> values) {
        return parseNumericValues(values)
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0.0);
    }

    public long calculateCount(List<String> values) {
        return parseNumericValues(values).count();
    }

    private java.util.stream.Stream<Double> parseNumericValues(List<String> values) {
        return values.stream()
                .filter(this::isNumeric)
                .map(Double::parseDouble);
    }

    private boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String evaluateFormula(String formula, List<String> values) {
        if (formula == null || formula.trim().isEmpty()) {
            return "";
        }

        try {
            String upperFormula = formula.toUpperCase().trim();
            if (upperFormula.startsWith("=")) {
                String function = upperFormula.substring(1);
                switch (function) {
                    case "SUM()":
                        return String.valueOf(calculateSum(values));
                    case "AVERAGE()":
                        return String.valueOf(calculateAverage(values));
                    case "MAX()":
                        return String.valueOf(calculateMax(values));
                    case "MIN()":
                        return String.valueOf(calculateMin(values));
                    case "COUNT()":
                        return String.valueOf(calculateCount(values));
                    default:
                        return "#ERROR!";
                }
            }
            return formula;
        } catch (Exception e) {
            return "#ERROR!";
        }
    }
} 