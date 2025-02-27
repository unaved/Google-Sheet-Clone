package com.example.webspreadsheet.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.springframework.stereotype.Service;

import com.example.webspreadsheet.model.DataValidation;

import lombok.Data;

@Service
public class DataValidationService {
    private final Map<String, DataValidation> cellValidations = new HashMap<>();
    
    public void setValidation(String cellId, DataValidation validation) {
        cellValidations.put(cellId, validation);
    }
    
    public ValidationResult validate(String cellId, String value) {
        DataValidation validation = cellValidations.get(cellId);
        if (validation == null) {
            return new ValidationResult(true, null);
        }
        
        try {
            switch (validation.getType()) {
                case NUMBER:
                    return validateNumber(value, validation.getCriteria());
                case TEXT:
                    return validateText(value, validation.getCriteria());
                case DATE:
                    return validateDate(value, validation.getCriteria());
                case LIST:
                    return validateList(value, validation.getCriteria());
                case RANGE:
                    return validateRange(value, validation.getCriteria());
                case LENGTH:
                    return validateLength(value, validation.getCriteria());
                case REGEX:
                    return validateRegex(value, validation.getCriteria());
                default:
                    return new ValidationResult(true, null);
            }
        } catch (Exception e) {
            return new ValidationResult(false, "Invalid input format");
        }
    }
    
    private ValidationResult validateNumber(String value, String[] criteria) {
        try {
            double number = Double.parseDouble(value);
            if (criteria.length >= 2) {
                double min = Double.parseDouble(criteria[0]);
                double max = Double.parseDouble(criteria[1]);
                if (number < min || number > max) {
                    return new ValidationResult(false, 
                        String.format("Value must be between %s and %s", min, max));
                }
            }
            return new ValidationResult(true, null);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Must be a valid number");
        }
    }
    
    private ValidationResult validateText(String value, String[] criteria) {
        if (criteria.length > 0 && criteria[0].equals("NOT_EMPTY") && value.trim().isEmpty()) {
            return new ValidationResult(false, "Text cannot be empty");
        }
        return new ValidationResult(true, null);
    }
    
    private ValidationResult validateDate(String value, String[] criteria) {
        try {
            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
            if (criteria.length >= 2) {
                LocalDate min = LocalDate.parse(criteria[0]);
                LocalDate max = LocalDate.parse(criteria[1]);
                if (date.isBefore(min) || date.isAfter(max)) {
                    return new ValidationResult(false, 
                        String.format("Date must be between %s and %s", min, max));
                }
            }
            return new ValidationResult(true, null);
        } catch (DateTimeParseException e) {
            return new ValidationResult(false, "Must be a valid date (YYYY-MM-DD)");
        }
    }
    
    private ValidationResult validateList(String value, String[] criteria) {
        if (!Arrays.asList(criteria).contains(value)) {
            return new ValidationResult(false, 
                "Value must be one of: " + String.join(", ", criteria));
        }
        return new ValidationResult(true, null);
    }
    
    private ValidationResult validateRange(String value, String[] criteria) {
        try {
            double number = Double.parseDouble(value);
            double min = Double.parseDouble(criteria[0]);
            double max = Double.parseDouble(criteria[1]);
            if (number < min || number > max) {
                return new ValidationResult(false, 
                    String.format("Value must be between %s and %s", min, max));
            }
            return new ValidationResult(true, null);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Must be a valid number");
        }
    }
    
    private ValidationResult validateLength(String value, String[] criteria) {
        int length = value.length();
        int min = Integer.parseInt(criteria[0]);
        int max = Integer.parseInt(criteria[1]);
        if (length < min || length > max) {
            return new ValidationResult(false, 
                String.format("Text length must be between %d and %d characters", min, max));
        }
        return new ValidationResult(true, null);
    }
    
    private ValidationResult validateRegex(String value, String[] criteria) {
        try {
            Pattern pattern = Pattern.compile(criteria[0]);
            if (!pattern.matcher(value).matches()) {
                return new ValidationResult(false, 
                    criteria.length > 1 ? criteria[1] : "Value does not match the required pattern");
            }
            return new ValidationResult(true, null);
        } catch (PatternSyntaxException e) {
            return new ValidationResult(false, "Invalid regex pattern");
        }
    }
    
    @Data
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;
    }
} 