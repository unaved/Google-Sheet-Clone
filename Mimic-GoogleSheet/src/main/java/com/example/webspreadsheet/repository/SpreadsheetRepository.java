package com.example.webspreadsheet.repository;

import com.example.webspreadsheet.model.SpreadsheetData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpreadsheetRepository extends JpaRepository<SpreadsheetData, Long> {
    List<SpreadsheetData> findAllByOrderByUpdatedAtDesc();
} 