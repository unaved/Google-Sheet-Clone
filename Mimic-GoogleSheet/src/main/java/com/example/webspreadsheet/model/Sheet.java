package com.example.webspreadsheet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Sheet {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Cell> cells;
    
    private Integer rowCount = 100;
    private Integer columnCount = 26;
} 