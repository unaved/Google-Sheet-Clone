package com.example.webspreadsheet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Cell {
    @Id
    @GeneratedValue
    private Long id;
    private String row;
    private Integer column;
    private String value;
    private String formula;
    private String style;
    private String dataType;
} 