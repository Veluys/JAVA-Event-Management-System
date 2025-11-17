package ems.controller;

import ems.model.EventDAO;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class RegController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();

    public void execute() {
        if (EventDAO.getLatestEventId() == 0) {
            System.out.println("There are no events yet!");
            return;
        }

        if(eventSelection() == -1){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        switch (mainMenu()){
//            case 1 -> addEvent();
//            case 2 -> viewEvents();
//            case 3 -> searchEvent();
//            case 4 -> updateEvents();
//            case 5 -> deleteEvent();
        }
        System.out.println();
    }

    private int eventSelection(){
        displayer.displayHeader("Registration");
        String event_name = inputGetter.getLine("Enter event name: ");
        return EventDAO.getEventId(event_name);
    }

    private int mainMenu(){
        displayer.displayHeader("Registration");
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("Add Participant", "View Participants", "Search Participant", "Remove Participants", "Exit")
        );
        displayer.showMenu("Select an operation:", operations);
        return inputGetter.getNumberOption(operations.size());
    }
}
