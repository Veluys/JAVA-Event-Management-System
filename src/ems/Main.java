package ems;

import ems.controller.*;
import ems.model.EventDAO;
import ems.view.Displayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static Connection connection = getConnection();
    public static void main(String[] args) {
        Displayer displayer = new Displayer();
        InputGetter inputGetter = new InputGetter();

        displayer.displayHeader("Welcome to Event Management System");
        ArrayList<String> mainMenuOptions = new ArrayList<>(
                Arrays.asList("Events", "Registration", "Attendance", "Exit")
        );

        while(true){
            displayer.displayHeader("Start Page");
            ArrayList<ArrayList<String>> upcoming_events = EventDAO.showUpcoming();
            if(upcoming_events!=null){
                ArrayList<String> event_attributes = new ArrayList<>(
                        Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue")
                );
                ArrayList<Double> columnWidths = new ArrayList<>(
                        Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20)
                );
                displayer.displaySubheader("Upcoming Events");
                displayer.displayTable(event_attributes, upcoming_events, columnWidths);
            }

            displayer.displaySubheader("Main Menu");
            displayer.showMenu("What do you want to do or work with today?", mainMenuOptions);
            int option = inputGetter.getNumberOption(mainMenuOptions.size());

            EventController eventController = new EventController();
            RegController regController = new RegController();
            AttendanceController attendanceController = new AttendanceController();

            switch (option){
                case 1 -> eventController.execute();
                case 2 -> regController.execute();
                case 3 -> attendanceController.execute();
                case 4 -> System.exit(0);
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
}
