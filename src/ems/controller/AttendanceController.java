package ems.controller;

import ems.model.AttendanceDAO;
import ems.model.EventDAO;
import ems.model.RegistrationDAO;
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
                case 1 -> viewAttendees();
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

    private void viewAttendees(){
        viewParticipants(AttendanceDAO.showAttendees(eventIdSelected));
    }

    private void viewParticipants(ArrayList<ArrayList<String>> presentAttendees){
        if(RegistrationDAO.isEmpty(eventIdSelected)){
            System.out.println("There are no participants yet!");
            return;
        }

        if(presentAttendees == null){
            System.out.println("There are no attendees yet!");
            return;
        }

        ArrayList<String> columnHeaders = new ArrayList<>(
                Arrays.asList("Sr-Code", "Department", "Last Name", "First Name")
        );

        displayer.centerAlignRow(columnHeaders);
        for(ArrayList<String> participant : presentAttendees){
            if(participant == presentAttendees.getFirst()) System.out.println();
            displayer.centerAlignRow(participant);
        }
    }
}
