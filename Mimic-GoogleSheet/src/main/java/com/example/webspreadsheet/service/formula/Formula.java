package com.example.webspreadsheet.service.formula;

import lombok.Data;

@Data
public class Formula {
    private String expression;
    private FormulaType type;
    private String[] arguments;
    
    public enum FormulaType {
        MATH_FUNCTION,
        DATA_FUNCTION,
        ARITHMETIC
    }
} 