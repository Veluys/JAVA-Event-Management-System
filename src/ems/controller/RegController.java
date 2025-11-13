package ems.controller;

import ems.view.Displayer;

public class RegController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();

    public void execute(){
        EventMenu();
        int option;

        do{
            displayer.numberedMenuPrompt();
            option = inputGetter.getPositiveInt(5);
        }while(option == -1);

        System.out.println();
//        switch (option){
//            case 1 -> addEvent();
//            case 2 -> viewEvents();
//            case 3 -> searchEvent();
//            case 4 -> updateEvents();
//            case 5 -> deleteEvent();
//            case 6 -> {
//                return;
//            }
//        }
        System.out.println();
    }

    private void EventMenu(){
        displayer.displayHeader("Registration");
        String[] operations = {"Add Participant", "View Participants", "Search Participant", "Remove Participants", "Exit"};
        displayer.showMenu("Select an operation:", operations);
    }
}
