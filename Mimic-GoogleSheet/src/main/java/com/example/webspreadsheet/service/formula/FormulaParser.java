package com.example.webspreadsheet.service.formula;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FormulaParser {
    private static final Pattern CELL_REFERENCE = Pattern.compile("([A-Z]+)([0-9]+)");
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("([A-Z_]+)\\((.*?)\\)");

    public Formula parse(String input) {
        if (input == null || !input.startsWith("=")) {
            return null;
        }

        String expression = input.substring(1).trim();
        Formula formula = new Formula();
        formula.setExpression(expression);

        // Check if it's a function
        Matcher functionMatcher = FUNCTION_PATTERN.matcher(expression);
        if (functionMatcher.matches()) {
            String functionName = functionMatcher.group(1);
            String argumentString = functionMatcher.group(2);
            
            // Parse arguments
            String[] arguments = parseArguments(argumentString);
            formula.setArguments(arguments);

            // Determine function type
            if (isMathFunction(functionName)) {
                formula.setType(Formula.FormulaType.MATH_FUNCTION);
            } else if (isDataFunction(functionName)) {
                formula.setType(Formula.FormulaType.DATA_FUNCTION);
            }
        } else {
            formula.setType(Formula.FormulaType.ARITHMETIC);
        }

        return formula;
    }

    private String[] parseArguments(String argumentString) {
        List<String> arguments = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenthesesCount = 0;
        
        for (char c : argumentString.toCharArray()) {
            if (c == '(') {
                parenthesesCount++;
                current.append(c);
            } else if (c == ')') {
                parenthesesCount--;
                current.append(c);
            } else if (c == ',' && parenthesesCount == 0) {
                arguments.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            arguments.add(current.toString().trim());
        }
        
        return arguments.toArray(new String[0]);
    }

    private boolean isMathFunction(String name) {
        return name.matches("SUM|AVERAGE|MAX|MIN|COUNT");
    }

    private boolean isDataFunction(String name) {
        return name.matches("TRIM|UPPER|LOWER|REMOVE_DUPLICATES");
    }
} 