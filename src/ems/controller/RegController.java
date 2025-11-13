package ems.controller;

import ems.model.EventDAO;
import ems.view.Displayer;

import java.util.LinkedHashMap;

public class RegController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();

    public void execute(){
        if(EventDAO.getLatestEventId() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        int selected_event_id = eventSelection();

        participantMenu();
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

    private void participantMenu(){
        displayer.displayHeader("Registration");
        String[] operations = {"Add Participant", "View Participants", "Search Participant", "Remove Participants", "Exit"};
        displayer.showMenu("Select an operation:", operations);
    }

    private int eventSelection(){
        displayer.displayHeader("Registration");
        int event_id;

        do{
            displayer.showPrompt("Enter event id: ");
            event_id = inputGetter.getPositiveInt(EventDAO.getLatestEventId());
        }while (!EventDAO.eventExist("event_id = '" + event_id + "'"));

        return event_id;
    }
}
