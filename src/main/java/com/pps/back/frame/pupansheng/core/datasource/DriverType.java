package com.pps.back.frame.pupansheng.core.datasource;

/**
 * @author
 * @discription;
 * @time 2020/9/11 17:33
 */

public enum DriverType {
    MYSQL("MySQL", "MYSQL"),
    ORACLE("Oracle", "ORACLE"),
    SQLSERVER("SQL Server", "SQLSERVER"),
    INFORMIX("Informix", "INFORMIX"),
    H2("H2", "H2"),
    POSTGRESQL("PostgreSQL", "POSTGRESQL");

    private String pattern;
    private String type;

    private DriverType(String pattern, String type) {
        this.pattern = pattern;
        this.type = type;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}