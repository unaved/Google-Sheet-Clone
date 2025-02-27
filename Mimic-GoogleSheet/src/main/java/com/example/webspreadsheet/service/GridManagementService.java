package com.example.webspreadsheet.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

@Service
public class GridManagementService {
    
    public Map<String, String> addRow(int index, Map<String, String> cellValues) {
        Map<String, String> updatedValues = new TreeMap<>();
        
        // Shift existing values down
        cellValues.forEach((pos, value) -> {
            String[] parts = pos.split("-");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            
            if (row >= index) {
                // Move row down
                updatedValues.put((row + 1) + "-" + col, value);
            } else {
                // Keep row as is
                updatedValues.put(pos, value);
            }
        });
        
        return updatedValues;
    }
    
    public Map<String, String> deleteRow(int index, Map<String, String> cellValues) {
        Map<String, String> updatedValues = new TreeMap<>();
        
        // Shift existing values up
        cellValues.forEach((pos, value) -> {
            String[] parts = pos.split("-");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            
            if (row > index) {
                // Move row up
                updatedValues.put((row - 1) + "-" + col, value);
            } else if (row < index) {
                // Keep row as is
                updatedValues.put(pos, value);
            }
            // Skip the row being deleted
        });
        
        return updatedValues;
    }
    
    public Map<String, String> addColumn(int index, Map<String, String> cellValues) {
        Map<String, String> updatedValues = new TreeMap<>();
        
        // Shift existing values right
        cellValues.forEach((pos, value) -> {
            String[] parts = pos.split("-");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            
            if (col >= index) {
                // Move column right
                updatedValues.put(row + "-" + (col + 1), value);
            } else {
                // Keep column as is
                updatedValues.put(pos, value);
            }
        });
        
        return updatedValues;
    }
    
    public Map<String, String> deleteColumn(int index, Map<String, String> cellValues) {
        Map<String, String> updatedValues = new TreeMap<>();
        
        // Shift existing values left
        cellValues.forEach((pos, value) -> {
            String[] parts = pos.split("-");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            
            if (col > index) {
                // Move column left
                updatedValues.put(row + "-" + (col - 1), value);
            } else if (col < index) {
                // Keep column as is
                updatedValues.put(pos, value);
            }
            // Skip the column being deleted
        });
        
        return updatedValues;
    }
    
    public Map<String, Integer> getColumnWidths() {
        Map<String, Integer> widths = new HashMap<>();
        // Default column widths
        for (int i = 1; i <= 26; i++) {
            widths.put(String.valueOf(i), 100); // Default width: 100px
        }
        return widths;
    }
    
    public Map<String, Integer> getRowHeights() {
        Map<String, Integer> heights = new HashMap<>();
        // Default row heights
        for (int i = 1; i <= 100; i++) {
            heights.put(String.valueOf(i), 25); // Default height: 25px
        }
        return heights;
    }
} 