package ems.controller;

import ems.model.EventDAO;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

public class AttendanceController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();
    private int eventIdSelected;

    public void execute() {
        if (EventDAO.getLatestEventId() == 0) {
            System.out.println("There are no events yet!");
            return;
        }

        eventIdSelected = eventSelection();
        if(eventIdSelected == -1){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        while (true){
            switch (mainMenu()){
//                case 1 -> addRegistration();
//                case 2 -> viewRegistered();
//                case 3 -> searchRegistered();
//                case 4 -> removeRegistered();
                case 5 -> {return;}
            }
            System.out.println();
        }
    }
    private int eventSelection(){
        displayer.displayHeader("Attendance");
        String event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();
        return EventDAO.getEventId(event_name);
    }

    private int mainMenu(){
        displayer.displayHeader("Attendance");
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("View Attendees", "View Absentees", "Set as Present", "Reset as Absent", "Exit")
        );
        displayer.showMenu("Select an operation:", operations);
        return inputGetter.getNumberOption(operations.size());
    }
}
