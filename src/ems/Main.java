package ems;

import ems.controller.*;
import ems.view.Displayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static Connection connection = getConnection();
    public static void main(String[] args) {
        Displayer.displayHeader("Welcome to PLAN ET: The BSU-MALVAR Event Management System");

        while(true){
            Displayer.displayHeader("Start Page");
            Displayer.displaySubheader("Main Menu");
            ArrayList<String> mainMenuOptions = new ArrayList<>(
                    Arrays.asList("Events", "Registration", "Attendance", "Exit")
            );
            Displayer.showMenu("What do you want to do or work with today?", mainMenuOptions);
            int option = InputGetter.getNumberOption(mainMenuOptions.size());

            EventController eventController = new EventController();
            RegController regController = new RegController();
            AttendanceController attendanceController = new AttendanceController();

            switch (option){
                case 1 -> eventController.execute();
                case 2 -> regController.execute();
                case 3 -> attendanceController.execute();
                case 4 -> {
                    closeConnection();
                    System.out.println("Thank you for using Plan_ET. Happy Planning!");
                    System.exit(0);
                }
            }
        }
    }

    private static Connection getConnection(){
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
    private static void closeConnection() {
        try{
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException err) {
            System.out.println("Connection can't be closed!");
            System.out.println("System would be forcefully terminated!");
            System.exit(1);
        }
    }
}
