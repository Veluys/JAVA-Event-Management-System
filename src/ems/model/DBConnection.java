package ems.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static Connection getConnection(){
        final String username = "postgres";
        final String password = "byte";
        final String url = "jdbc:postgresql://localhost:5432/plan_et";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("An unexpected error has occurred!");
            System.out.printf("Error: %s \n", e.getMessage());
        }

        return connection;
    }
}
