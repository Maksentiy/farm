package com.konon.farm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/farm", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
