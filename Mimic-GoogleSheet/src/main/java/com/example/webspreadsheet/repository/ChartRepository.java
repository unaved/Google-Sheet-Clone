package com.example.webspreadsheet.repository;

import com.example.webspreadsheet.model.Chart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChartRepository extends JpaRepository<Chart, Long> {
    List<Chart> findBySpreadsheetId(Long spreadsheetId);
} 