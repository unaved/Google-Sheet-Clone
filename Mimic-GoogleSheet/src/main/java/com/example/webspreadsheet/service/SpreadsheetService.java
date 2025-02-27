package com.example.webspreadsheet.service;

import com.example.webspreadsheet.model.SpreadsheetData;
import com.example.webspreadsheet.repository.SpreadsheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class SpreadsheetService {
    
    @Autowired
    private SpreadsheetRepository repository;
    
    private Map<String, Object> spreadsheetData = new HashMap<>();
    
    public SpreadsheetData saveSpreadsheet(SpreadsheetData data) {
        if (data.getId() == null) {
            data.setCreatedAt(LocalDateTime.now());
        }
        data.setUpdatedAt(LocalDateTime.now());
        return repository.save(data);
    }
    
    public Optional<SpreadsheetData> loadSpreadsheet(Long id) {
        return repository.findById(id);
    }
    
    public List<SpreadsheetData> getAllSpreadsheets() {
        return repository.findAllByOrderByUpdatedAtDesc();
    }
    
    public void deleteSpreadsheet(Long id) {
        repository.deleteById(id);
    }
    
    public Map<String, Object> saveSpreadsheet(Map<String, Object> data) {
        spreadsheetData = data;
        return data;
    }
    
    public Map<String, Object> loadSpreadsheet() {
        return spreadsheetData;
    }
} 