package com.example.dbcrawler.service;

import com.example.dbcrawler.model.TableMetadata;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Service
public class DatabaseCrawlerService {
    private final DataSource dataSource;

    public DatabaseCrawlerService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Get all tables
    public List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        }
        return tables;
    }

    // Get metadata for a specific table
    public TableMetadata getTableMetadata(String tableName) throws SQLException {
        TableMetadata metadata = new TableMetadata();
        metadata.setTableName(tableName);

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            // Get columns
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                boolean isNullable = "YES".equals(columns.getString("IS_NULLABLE"));
                metadata.addColumn(columnName, columnType, columnSize, isNullable);
            }

            // Get primary keys
            metadata.setPrimaryKeys(getPrimaryKey(tableName));

            // Get foreign keys
            metadata.setForeignKeys(getForeignKeys(tableName));

            // Get indexes
            metadata.setIndexes(getIndexes(tableName));
        }

        return metadata;
    }

    // Get primary key for a table
    public List<String> getPrimaryKey(String tableName) throws SQLException {
        List<String> primaryKeys = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getPrimaryKeys(null, null, tableName);
            while (resultSet.next()) {
                primaryKeys.add(resultSet.getString("COLUMN_NAME"));
            }
        }
        return primaryKeys;
    }

    // Get foreign keys for a table
    public List<Map<String, String>> getForeignKeys(String tableName) throws SQLException {
        List<Map<String, String>> foreignKeys = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getImportedKeys(null, null, tableName);
            while (resultSet.next()) {
                Map<String, String> fkMap = new HashMap<>();
                fkMap.put("FK_COLUMN", resultSet.getString("FKCOLUMN_NAME"));
                fkMap.put("PK_TABLE", resultSet.getString("PKTABLE_NAME"));
                fkMap.put("PK_COLUMN", resultSet.getString("PKCOLUMN_NAME"));
                foreignKeys.add(fkMap);
            }
        }
        return foreignKeys;
    }

    // Get indexes for a table
    public List<Map<String, String>> getIndexes(String tableName) throws SQLException {
        List<Map<String, String>> indexes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getIndexInfo(null, null, tableName, false, false);
            while (resultSet.next()) {
                Map<String, String> indexMap = new HashMap<>();
                indexMap.put("INDEX_NAME", resultSet.getString("INDEX_NAME"));
                indexMap.put("COLUMN_NAME", resultSet.getString("COLUMN_NAME"));
                indexes.add(indexMap);
            }
        }
        return indexes;
    }

    // Get full metadata for the entire database
    public Map<String, TableMetadata> getDatabaseMetadata() throws SQLException {
        Map<String, TableMetadata> databaseMetadata = new HashMap<>();
        List<String> tables = getTables();

        for (String table : tables) {
            databaseMetadata.put(table, getTableMetadata(table));
        }
        return databaseMetadata;
    }

    // Placeholder for generating model classes
    public Map<String, String> generateAllModels() {
        return Collections.emptyMap(); // Implementation can be added
    }
}
