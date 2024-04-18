package de.hsh.dbs2.imdb.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MovieDB_Connection {
    private static Connection conn;

    static {
        try {
            conn = conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\ngoku\\OneDrive\\Documents\\HS\\WS_2023\\DBS2\\ue\\06\\moviedb\\movieDB.db");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public static Connection getConnection() {
        return conn;
    }


}
