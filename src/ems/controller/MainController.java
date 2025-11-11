package ems.controller;

import ems.view.Displayer;

public class MainController {
    public static void execute(){
        Displayer displayer = new Displayer();
        displayer.displayHeader("Welcome to Event Management System");

        String[] mainMenuOptions = {"Events", "Registration", "Participants", "Attendance", "Exit"};
        displayer.showMenu("What do you want to do or work with today?", mainMenuOptions);
    }
}
