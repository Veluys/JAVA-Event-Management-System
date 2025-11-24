package ems.controller;

import ems.model.AttendanceDAO;
import ems.model.EventDAO;
import ems.model.RegistrationDAO;
import ems.model.VenueDAO;
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

        if(RegistrationDAO.emptyCheck(eventIdSelected)){
            System.out.println("There are no participants yet!");
            return;
        }

        while (true){
            switch (mainMenu()){
                case 1 -> viewAttendees();
                case 2 -> viewAbsentees();
                case 3 -> markPresent();
                case 4 -> markAbsent();
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
        viewParticipants(AttendanceDAO.showAttendees(eventIdSelected), true);
    }

    private void viewAbsentees(){
        viewParticipants(AttendanceDAO.showAbsentees(eventIdSelected), false);
    }

    private void viewParticipants(ArrayList<ArrayList<String>> participants, boolean attendanceStatus){
        if(participants == null){
            if(attendanceStatus){
                System.out.println("There are no attendees!");
            }else{
                System.out.println("There are no absentees!");
            }
            return;
        }

        ArrayList<String> columnHeaders = new ArrayList<>(
                Arrays.asList("Sr-Code", "Department", "Last Name", "First Name")
        );

        displayer.centerAlignRow(columnHeaders);
        for(ArrayList<String> participant : participants){
            if(participant == participants.getFirst()) System.out.println();
            displayer.centerAlignRow(participant);
        }
    }

    private void markPresent(){
        String participant_id = inputGetter.getLine("Enter the Sr-Code of the participant: ");
        System.out.println();

        if(RegistrationDAO.search(eventIdSelected, participant_id) == null){
            System.out.println("A participant with an Sr-Code of " + participant_id + " is not registered!");
            return;
        }
        AttendanceDAO.markPresent(eventIdSelected, participant_id);

        System.out.println("Participant '" + participant_id + "' was successfully marked as present.");
    }

    private void markAbsent(){
        String participant_id = inputGetter.getLine("Enter the Sr-Code of the participant: ");
        System.out.println();

        if(RegistrationDAO.search(eventIdSelected, participant_id) == null){
            System.out.println("A participant with an Sr-Code of " + participant_id + " is not registered!");
            return;
        }
        AttendanceDAO.markAbsent(eventIdSelected, participant_id);

        System.out.println("Participant '" + participant_id + "' was successfully marked as absent.");
    }
}
