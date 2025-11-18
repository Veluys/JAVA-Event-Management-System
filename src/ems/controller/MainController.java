package ems.controller;

import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

public class MainController {
    public static void execute(){
        Displayer displayer = new Displayer();
        InputGetter inputGetter = new InputGetter();

        displayer.displayHeader("Welcome to Event Management System");

        ArrayList<String> mainMenuOptions = new ArrayList<>(
                Arrays.asList("Events", "Registration", "Participants", "Attendance", "Exit")
        );

        while(true){
            displayer.displayHeader("Main Menu");
            displayer.showMenu("What do you want to do or work with today?", mainMenuOptions);
            int option = inputGetter.getNumberOption(mainMenuOptions.size());

            EventController eventController = new EventController();
            RegController regController = new RegController();
            AttendanceController attendanceController = new AttendanceController();
            ParticipantController participantController = new ParticipantController();

            switch (option){
                case 1 -> eventController.execute();
                case 2 -> regController.execute();
                case 3 -> participantController.execute();
                case 4 -> attendanceController.execute();
                case 5 -> System.exit(0);
            }
        }
    }
}
