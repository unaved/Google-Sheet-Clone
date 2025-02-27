package com.example.webspreadsheet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpreadsheetController {
    
    @GetMapping("/")
    public String showSpreadsheet(Model model) {
        return "spreadsheet";
    }
} 