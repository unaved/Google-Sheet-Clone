package com.example.webspreadsheet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "spreadsheet_data")
@Data
public class SpreadsheetData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ElementCollection
    @CollectionTable(name = "cell_data", 
        joinColumns = @JoinColumn(name = "spreadsheet_id"))
    @MapKeyColumn(name = "cell_position", length = 10)
    @Column(name = "cell_value", columnDefinition = "TEXT")
    private Map<String, String> cellValues;
    
    @ElementCollection
    @CollectionTable(name = "cell_styles", 
        joinColumns = @JoinColumn(name = "spreadsheet_id"))
    @MapKeyColumn(name = "cell_position", length = 10)
    @Column(name = "cell_style", columnDefinition = "TEXT")
    private Map<String, String> cellStyles;
    
    @ElementCollection
    @CollectionTable(name = "cell_formulas", 
        joinColumns = @JoinColumn(name = "spreadsheet_id"))
    @MapKeyColumn(name = "cell_position", length = 10)
    @Column(name = "cell_formula", columnDefinition = "TEXT")
    private Map<String, String> cellFormulas;
} 