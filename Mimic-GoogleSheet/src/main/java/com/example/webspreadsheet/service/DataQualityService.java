package com.example.webspreadsheet.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataQualityService {

    public String trim(String value) {
        return value != null ? value.trim() : "";
    }

    public String upper(String value) {
        return value != null ? value.toUpperCase() : "";
    }

    public String lower(String value) {
        return value != null ? value.toLowerCase() : "";
    }

    public List<String> removeDuplicates(List<String> values) {
        return new ArrayList<>(new LinkedHashSet<>(values));
    }

    public String findAndReplace(String value, String find, String replace) {
        return value != null ? value.replace(find, replace) : "";
    }

    public String evaluateFunction(String formula, String value, String... params) {
        if (formula == null || formula.trim().isEmpty()) {
            return value;
        }

        try {
            String upperFormula = formula.toUpperCase().trim();
            if (upperFormula.startsWith("=")) {
                String function = upperFormula.substring(1);
                switch (function) {
                    case "TRIM()":
                        return trim(value);
                    case "UPPER()":
                        return upper(value);
                    case "LOWER()":
                        return lower(value);
                    default:
                        if (function.startsWith("FIND_AND_REPLACE(") && params.length >= 2) {
                            return findAndReplace(value, params[0], params[1]);
                        }
                        return "#ERROR!";
                }
            }
            return formula;
        } catch (Exception e) {
            return "#ERROR!";
        }
    }
} 