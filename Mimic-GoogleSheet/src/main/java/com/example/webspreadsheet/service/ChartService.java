package com.example.webspreadsheet.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.webspreadsheet.model.Chart;
import com.example.webspreadsheet.repository.ChartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChartService {
    
    @Autowired
    private ChartRepository chartRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    public Chart createChart(String type, String title, List<String> labels, 
                           List<Double> data, String xAxis, String yAxis, 
                           Long spreadsheetId) throws Exception {
        Chart chart = new Chart();
        chart.setType(type);
        chart.setTitle(title);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setSpreadsheetId(spreadsheetId);

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("data", data);
        
        chart.setData(objectMapper.writeValueAsString(chartData));
        
        return chartRepository.save(chart);
    }

    public List<Chart> getChartsForSpreadsheet(Long spreadsheetId) {
        return chartRepository.findBySpreadsheetId(spreadsheetId);
    }

    public void deleteChart(Long chartId) {
        chartRepository.deleteById(chartId);
    }

    public Chart updateChart(Long chartId, List<String> labels, 
                           List<Double> data) throws Exception {
        Chart chart = chartRepository.findById(chartId)
            .orElseThrow(() -> new RuntimeException("Chart not found"));

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("data", data);
        
        chart.setData(objectMapper.writeValueAsString(chartData));
        
        return chartRepository.save(chart);
    }
} 