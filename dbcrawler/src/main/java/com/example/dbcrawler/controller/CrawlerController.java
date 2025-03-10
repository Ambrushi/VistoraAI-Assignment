package com.example.dbcrawler.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.dbcrawler.model.TableMetadata;
import com.example.dbcrawler.service.DatabaseCrawlerService;
import com.example.dbcrawler.util.ModelGenerator;

@RestController
@RequestMapping("/api")
public class CrawlerController {
    
    @Autowired
    private DatabaseCrawlerService crawlerService;

    // Fetch all tables in the database
    @GetMapping("/tables")
    public List<String> getTables() throws SQLException {
        return crawlerService.getTables();
    }

    // Fetch complete metadata for a specific table
    @GetMapping("/tables/{tableName}/metadata")
    public TableMetadata getTableMetadata(@PathVariable String tableName) throws SQLException {
        return crawlerService.getTableMetadata(tableName);
    }

    // Fetch only column details of a table
    @GetMapping("/tables/{tableName}/columns")
    public TableMetadata getTableColumns(@PathVariable String tableName) throws SQLException {
        return crawlerService.getTableMetadata(tableName);
    }

    // Fetch primary keys of a table
    @GetMapping("/tables/{tableName}/primary-key")
    public List<String> getPrimaryKey(@PathVariable String tableName) throws SQLException {
        return crawlerService.getPrimaryKey(tableName);
    }

    // Fetch foreign keys of a table
    @GetMapping("/tables/{tableName}/foreign-keys")
    public List<Map<String, String>> getForeignKeys(@PathVariable String tableName) throws SQLException {
        return crawlerService.getForeignKeys(tableName);
    }

    // Fetch indexes of a table
    @GetMapping("/tables/{tableName}/indexes")
    public List<Map<String, String>> getIndexes(@PathVariable String tableName) throws SQLException {
        return crawlerService.getIndexes(tableName);
    }

    // Fetch overall database metadata (all tables and their details)
    @GetMapping("/metadata")
    public Map<String, TableMetadata> getDatabaseMetadata() throws SQLException {
        return crawlerService.getDatabaseMetadata();
    }

    // Generate model class for a specific table
    @GetMapping("/models/{tableName}")
    public String generateModel(@PathVariable String tableName) throws Exception {
        TableMetadata metadata = crawlerService.getTableMetadata(tableName);
        return ModelGenerator.generateClass(metadata);
    }

    // Generate model classes for all tables
    @GetMapping("/models")
    public Map<String, String> generateAllModels() throws Exception {
        return crawlerService.generateAllModels();
    }
}
