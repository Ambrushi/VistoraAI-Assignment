package com.example.dbcrawler.model;



import java.util.*;

public class TableMetadata {
    private String tableName;
    private Map<String, ColumnMetadata> columns = new HashMap<>();
    private List<String> primaryKeys = new ArrayList<>();
    private Map<String, ForeignKeyMetadata> foreignKeys = new HashMap<>();
    private Map<String, List<String>> indexes = new HashMap<>();

    // Getters and Setters
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public Map<String, ColumnMetadata> getColumns() { return columns; }
    public void addColumn(String columnName, String columnType, int columnSize, boolean isNullable) {
        columns.put(columnName, new ColumnMetadata(columnType, columnSize, isNullable));
    }

    public List<String> getPrimaryKeys() { return primaryKeys; }
    public void addPrimaryKey(String columnName) { primaryKeys.add(columnName); }

    public Map<String, ForeignKeyMetadata> getForeignKeys() { return foreignKeys; }
    public void addForeignKey(String fkColumnName, String pkTableName, String pkColumnName) {
        foreignKeys.put(fkColumnName, new ForeignKeyMetadata(pkTableName, pkColumnName));
    }

    public Map<String, List<String>> getIndexes() { return indexes; }
    public void addIndex(String indexName, String columnName) {
        indexes.computeIfAbsent(indexName, k -> new ArrayList<>()).add(columnName);
    }

    // Inner classes for metadata
    public static class ColumnMetadata {
        private String columnType;
        private int columnSize;
        private boolean isNullable;

        public ColumnMetadata(String columnType, int columnSize, boolean isNullable) {
            this.columnType = columnType;
            this.columnSize = columnSize;
            this.isNullable = isNullable;
        }

        // Getters
        public String getColumnType() { return columnType; }
        public int getColumnSize() { return columnSize; }
        public boolean isNullable() { return isNullable; }
    }

    public static class ForeignKeyMetadata {
        private String pkTableName;
        private String pkColumnName;

        public ForeignKeyMetadata(String pkTableName, String pkColumnName) {
            this.pkTableName = pkTableName;
            this.pkColumnName = pkColumnName;
        }

        // Getters
        public String getPkTableName() { return pkTableName; }
        public String getPkColumnName() { return pkColumnName; }
    }

	public void setPrimaryKeys(List<String> primaryKey) {
		// TODO Auto-generated method stub
		
	}
	public void setForeignKeys(List<Map<String, String>> foreignKeys2) {
		// TODO Auto-generated method stub
		
	}
	public void setIndexes(List<Map<String, String>> indexes2) {
		// TODO Auto-generated method stub
		
	}
}