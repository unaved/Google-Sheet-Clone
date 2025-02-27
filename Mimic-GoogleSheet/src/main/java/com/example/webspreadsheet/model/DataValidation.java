package com.example.webspreadsheet.model;

import lombok.Data;

@Data
public class DataValidation {
    private ValidationType type;
    private String[] criteria;
    private String errorMessage;
    
    public enum ValidationType {
        NUMBER,
        TEXT,
        DATE,
        LIST,
        CUSTOM,
        RANGE,
        LENGTH,
        REGEX
    }
} 