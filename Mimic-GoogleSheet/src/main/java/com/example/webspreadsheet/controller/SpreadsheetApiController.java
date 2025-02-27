package com.example.webspreadsheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.webspreadsheet.service.SpreadsheetService;
import java.util.Map;

@RestController
@RequestMapping("/api/spreadsheet")
@CrossOrigin(origins = "*")
public class SpreadsheetApiController {
    
    @Autowired
    private SpreadsheetService spreadsheetService;
    
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveSpreadsheet(@RequestBody Map<String, Object> data) {
        Map<String, Object> savedData = spreadsheetService.saveSpreadsheet(data);
        return ResponseEntity.ok(savedData);
    }
    
    @GetMapping("/load")
    public ResponseEntity<Map<String, Object>> loadSpreadsheet() {
        Map<String, Object> data = spreadsheetService.loadSpreadsheet();
        return ResponseEntity.ok(data);
    }
} 