package com.example.librarymanagementsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DB {
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getDbUrl(),
                DatabaseConfig.getDbUsername(),
                DatabaseConfig.getDbPassword());
    }
}