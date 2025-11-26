package ems.controller;

import ems.model.AttendanceDAO;
import ems.model.EventDAO;
import ems.model.RegistrationDAO;
import ems.view.Displayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class AttendanceController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();
    private int eventIdSelected;
    private boolean eventDone;

    public void execute() {
        displayer.displayHeader("Attendance Page");
        if (EventDAO.emptyCheck()) {
            System.out.println("There are no events yet!");
            return;
        }

        String event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();
        ArrayList<String> matchedEvent = EventDAO.searchRecord(event_name);

        if(matchedEvent==null){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        eventDone = LocalDate.parse(matchedEvent.get(2)).isBefore(LocalDate.now());

        eventIdSelected = Integer.parseInt(matchedEvent.get(0));

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

    private int mainMenu(){
        displayer.displaySubheader("Attendance Menu");
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("View Attendees", "View Absentees", "Set as Present", "Reset as Absent", "Exit")
        );
        displayer.showMenu("Select an operation:", operations);
        return inputGetter.getNumberOption(operations.size());
    }

    private void viewAttendees(){
        displayer.displayHeader("Viewing Attendees");
        viewParticipants(AttendanceDAO.showAttendees(eventIdSelected), true);
    }

    private void viewAbsentees(){
        displayer.displayHeader("Viewing Absentees");
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
                Arrays.asList("Sr-Code", "Program", "Year Level", "Full Name")
        );

        ArrayList<Double> columnWidths = new ArrayList<>(
                Arrays.asList(0.15, 0.20, 0.15, 0.50)
        );

        if(attendanceStatus){
            displayer.displaySubheader("Attendees");
        }else{
            displayer.displaySubheader("Absentees");
        }


        displayer.displayTable(columnHeaders, participants, columnWidths);
    }

    private void markPresent(){
        displayer.displayHeader("Marking for Present");
        if(eventDone){
            System.out.println("Event is already finished!");
            return;
        }

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
        displayer.displayHeader("Marking for Absent");
        if(eventDone){
            System.out.println("Event is already finished!");
            return;
        }

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
