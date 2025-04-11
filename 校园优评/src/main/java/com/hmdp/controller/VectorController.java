package com.hmdp.controller;

import com.hmdp.entity.VectorData;
import com.hmdp.service.VectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vector")
public class VectorController {
    
    @Autowired
    private VectorService vectorService;
    
    @PostMapping("/add")
    public void addVector(@RequestBody VectorData vectorData) {
        vectorService.addVector(vectorData);
    }
    
    @PostMapping("/batch")
    public void batchAddVectors(@RequestBody List<VectorData> vectorDataList) {
        vectorService.batchAddVectors(vectorDataList);
    }
    
    @GetMapping("/search")
    public List<VectorData> searchSimilar(
            @RequestParam String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "5") int topK) {
        return vectorService.searchSimilar(query, type, topK);
    }
    
    @DeleteMapping("/{id}")
    public void deleteVector(@PathVariable String id) {
        vectorService.deleteVector(id);
    }
} 