package com.jsonpath.example.Model;



public class LookupTable {
    private TableData tableData;

    private String tableName;

    public TableData getTableData() {
        return tableData;
    }

    public void setTableData(TableData tableData) {
        this.tableData = tableData;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "LookupTable [tableData = " + tableData + ", tableName = " + tableName + "]";
    }
}
