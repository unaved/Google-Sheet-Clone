package com.example.webspreadsheet.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "charts")
@Data
public class Chart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String type; // LINE, BAR, PIE
    private String title;
    
    @Lob
    @Column(name = "data")
    private String data; // JSON string containing chart data
    
    private String xAxis;
    private String yAxis;
    
    @Column(name = "spreadsheet_id")
    private Long spreadsheetId;
} 