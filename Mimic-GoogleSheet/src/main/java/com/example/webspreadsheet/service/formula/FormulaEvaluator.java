package com.example.webspreadsheet.service.formula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.webspreadsheet.service.FormulaService;
import com.example.webspreadsheet.service.DataQualityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FormulaEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(FormulaEvaluator.class);

    @Autowired
    private FormulaParser parser;
    
    @Autowired
    private FormulaService formulaService;
    
    @Autowired
    private DataQualityService dataQualityService;

    private static final Pattern CELL_REFERENCE = Pattern.compile("([A-Z]+)([0-9]+)");

    public String evaluate(String input, Map<String, String> cellValues) {
        try {
            Formula formula = parser.parse(input);
            if (formula == null) {
                return input;
            }

            switch (formula.getType()) {
                case MATH_FUNCTION:
                    return evaluateMathFunction(formula, cellValues);
                case DATA_FUNCTION:
                    return evaluateDataFunction(formula, cellValues);
                case ARITHMETIC:
                    return evaluateArithmetic(formula.getExpression(), cellValues);
                default:
                    return "#ERROR!";
            }
        } catch (Exception e) {
            return handleError(e);
        }
    }

    private String evaluateMathFunction(Formula formula, Map<String, String> cellValues) {
        List<String> values = resolveArguments(formula.getArguments(), cellValues);
        String functionName = formula.getExpression().split("\\(")[0];

        switch (functionName) {
            case "SUM":
                return String.valueOf(formulaService.calculateSum(values));
            case "AVERAGE":
                return String.valueOf(formulaService.calculateAverage(values));
            case "MAX":
                return String.valueOf(formulaService.calculateMax(values));
            case "MIN":
                return String.valueOf(formulaService.calculateMin(values));
            case "COUNT":
                return String.valueOf(formulaService.calculateCount(values));
            default:
                return "#ERROR!";
        }
    }

    private String evaluateDataFunction(Formula formula, Map<String, String> cellValues) {
        List<String> values = resolveArguments(formula.getArguments(), cellValues);
        String functionName = formula.getExpression().split("\\(")[0];
        String value = values.isEmpty() ? "" : values.get(0);

        switch (functionName) {
            case "TRIM":
                return dataQualityService.trim(value);
            case "UPPER":
                return dataQualityService.upper(value);
            case "LOWER":
                return dataQualityService.lower(value);
            case "REMOVE_DUPLICATES":
                return String.join(",", dataQualityService.removeDuplicates(values));
            default:
                return "#ERROR!";
        }
    }

    private String evaluateArithmetic(String expression, Map<String, String> cellValues) {
        try {
            // Replace cell references with their values
            String processedExpression = expression;
            Matcher matcher = CELL_REFERENCE.matcher(expression);
            
            while (matcher.find()) {
                String cellRef = matcher.group();
                String cellValue = cellValues.getOrDefault(cellRef, "0");
                // Verify the cell value is numeric
                try {
                    Double.parseDouble(cellValue);
                } catch (NumberFormatException e) {
                    return "#VALUE!";
                }
                processedExpression = processedExpression.replace(cellRef, cellValue);
            }
            
            // Evaluate the processed expression
            double result = evaluateExpression(processedExpression);
            
            // Format the result
            if (result == (long) result) {
                return String.format("%d", (long) result);
            } else {
                return String.format("%.2f", result);
            }
        } catch (Exception e) {
            return "#ERROR!";
        }
    }

    private List<String> resolveArguments(String[] arguments, Map<String, String> cellValues) {
        List<String> resolved = new ArrayList<>();
        for (String arg : arguments) {
            if (isCellReference(arg)) {
                resolved.add(cellValues.getOrDefault(arg, ""));
            } else if (isCellRange(arg)) {
                resolved.addAll(resolveCellRange(arg, cellValues));
            } else {
                resolved.add(arg);
            }
        }
        return resolved;
    }

    private boolean isCellReference(String value) {
        return CELL_REFERENCE.matcher(value).matches();
    }

    private boolean isCellRange(String value) {
        return value.contains(":");
    }

    private List<String> resolveCellRange(String range, Map<String, String> cellValues) {
        String[] parts = range.split(":");
        if (parts.length != 2) return Collections.emptyList();

        Matcher m1 = CELL_REFERENCE.matcher(parts[0]);
        Matcher m2 = CELL_REFERENCE.matcher(parts[1]);
        
        if (!m1.matches() || !m2.matches()) return Collections.emptyList();

        List<String> values = new ArrayList<>();
        int startCol = columnToNumber(m1.group(1));
        int endCol = columnToNumber(m2.group(1));
        int startRow = Integer.parseInt(m1.group(2));
        int endRow = Integer.parseInt(m2.group(2));

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                String cellRef = numberToColumn(col) + row;
                values.add(cellValues.getOrDefault(cellRef, ""));
            }
        }

        return values;
    }

    private int columnToNumber(String column) {
        int result = 0;
        for (char c : column.toCharArray()) {
            result = result * 26 + (c - 'A' + 1);
        }
        return result;
    }

    private String numberToColumn(int number) {
        StringBuilder result = new StringBuilder();
        while (number > 0) {
            number--;
            result.insert(0, (char) ('A' + (number % 26)));
            number /= 26;
        }
        return result.toString();
    }

    private double evaluateExpression(String expression) {
        try {
            // Remove any whitespace
            expression = expression.replaceAll("\\s+", "");
            return new ExpressionParser(expression).parse();
        } catch (Exception e) {
            throw new RuntimeException("Invalid expression: " + expression, e);
        }
    }

    private String handleError(Exception e) {
        logger.error("Formula evaluation error: ", e);
        return "#ERROR!";
    }

    private static class ExpressionParser {
        private final String expression;
        private int pos = -1;
        private char ch;

        public ExpressionParser(String expression) {
            this.expression = expression;
            nextChar();
        }

        private void nextChar() {
            pos++;
            ch = (pos < expression.length()) ? expression.charAt(pos) : '\0';
        }

        private boolean eat(char charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        public double parse() {
            double x = parseExpression();
            if (pos < expression.length()) {
                throw new RuntimeException("Unexpected: " + ch);
            }
            return x;
        }

        private double parseExpression() {
            double x = parseTerm();
            while (true) {
                if (eat('+')) {
                    x += parseTerm();
                } else if (eat('-')) {
                    x -= parseTerm();
                } else {
                    return x;
                }
            }
        }

        private double parseTerm() {
            double x = parseFactor();
            while (true) {
                if (eat('*')) {
                    x *= parseFactor();
                } else if (eat('/')) {
                    double factor = parseFactor();
                    if (factor == 0) {
                        throw new RuntimeException("Division by zero");
                    }
                    x /= factor;
                } else {
                    return x;
                }
            }
        }

        private double parseFactor() {
            if (eat('+')) return +parseFactor();
            if (eat('-')) return -parseFactor();

            double x;
            int startPos = this.pos;

            if (eat('(')) {
                x = parseExpression();
                if (!eat(')')) {
                    throw new RuntimeException("Missing closing parenthesis");
                }
            } else if (Character.isDigit(ch) || ch == '.') {
                while (Character.isDigit(ch) || ch == '.') nextChar();
                x = Double.parseDouble(expression.substring(startPos, this.pos));
            } else {
                throw new RuntimeException("Unexpected: " + ch);
            }

            return x;
        }
    }
} 