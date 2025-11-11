package ems.controller;

import ems.view.Displayer;

public class MainController {
    public static void execute(){
        Displayer displayer = new Displayer();
        InputGetter inputGetter = new InputGetter();

        displayer.displayHeader("Welcome to Event Management System");

        String[] mainMenuOptions = {"Events", "Registration", "Participants", "Attendance", "Exit"};

        while(true){
            displayer.displayHeader("Main Menu");
            displayer.showMenu("What do you want to do or work with today?", mainMenuOptions);

            int option;
            do{
                displayer.numberedMenuPrompt();
                option = inputGetter.getPositiveInt(mainMenuOptions.length);
            }while(option == -1);

            EventController eventController = new EventController();

            switch (option){
                case 1 -> eventController.execute();
                case 5 -> System.exit(0);
            }
        }
    }
}
