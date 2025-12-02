package ems.controller;

import ems.model.AttendanceDAO;
import ems.model.EventDAO;
import ems.model.RegistrationDAO;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

public class AttendanceController {
    private int eventIdSelected;

    public void execute() {
        Displayer.displayHeader("Attendance Page");
        if (EventDAO.emptyCheck()) {
            System.out.println("There are no events yet!");
            return;
        }

        String event_name = InputGetter.getLine("Enter event name: ");
        System.out.println();
        eventIdSelected = EventDAO.getEventId(event_name);

        if(eventIdSelected==-1){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        if(RegistrationDAO.emptyCheck(eventIdSelected)){
            System.out.println("There are no participants yet!");
            return;
        }

        boolean should_loop = true;
        while(should_loop){
            Displayer.displayHeader("Attendance Page");

            Displayer.displaySubheader("Currently Selected Event");
            ArrayList<String> event_attributes = new ArrayList<>(
                    Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue")
            );

            ArrayList<Double> columnWidths = new ArrayList<>(
                    Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20)
            );

            ArrayList<ArrayList<String>> selected_event = new ArrayList<>();
            selected_event.add(EventDAO.search(event_name));

            Displayer.displayTable(event_attributes, selected_event, columnWidths);

            Displayer.displaySubheader("Attendance Menu");
            if(EventDAO.checkStatus(event_name).equalsIgnoreCase("scheduled")){
                should_loop = menuForScheduledEvent();
            }else if(EventDAO.checkStatus(event_name).equalsIgnoreCase("completed")){
                should_loop = menuForCompletedEvent();
            }else{
                System.out.println("Event is not categorized as scheduled or completed!");
            }
        }
    }

    private boolean menuForScheduledEvent(){
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("View Attendees", "View Absentees", "Check Participant's Attendance",
                        "Set as Present", "Reset as Absent", "Exit")
        );

        Displayer.displaySubheader("Attendance Menu");
        Displayer.showMenu("Select an operation:", operations);
        int option = InputGetter.getNumberOption(operations.size());

        switch (option){
            case 1 -> viewAttendees();
            case 2 -> viewAbsentees();
            case 3 -> checkAttendance();
            case 4 -> markPresent();
            case 5 -> markAbsent();
            case 6 -> {return false;}
        }
        System.out.println();
        return true;
    }

    private boolean menuForCompletedEvent(){
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("View Attendees", "View Absentees", "Check Participant's Attendance", "Exit")
        );

        Displayer.displaySubheader("Attendance Menu");
        Displayer.showMenu("Select an operation:", operations);
        int option = InputGetter.getNumberOption(operations.size());

        switch (option){
            case 1 -> viewAttendees();
            case 2 -> viewAbsentees();
            case 3 -> checkAttendance();
            case 4 -> {return false;}
        }
        System.out.println();
        return true;
    }

    private void viewAttendees(){
        Displayer.displayHeader("Viewing Attendees");
        viewParticipants(AttendanceDAO.showAttendees(eventIdSelected), true);
    }

    private void viewAbsentees(){
        Displayer.displayHeader("Viewing Absentees");
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
            Displayer.displaySubheader("Attendees");
        }else{
            Displayer.displaySubheader("Absentees");
        }


        Displayer.displayTable(columnHeaders, participants, columnWidths);
    }

    private void checkAttendance(){
        Displayer.displayHeader("Check Participant's Attendance");

        String sr_code = InputGetter.getLine("Enter the Sr-Code of the participant: ");
        System.out.println();

        if(RegistrationDAO.search(eventIdSelected, sr_code) == null){
            System.out.println("A participant with an Sr-Code of " + sr_code + " is not registered!");
            return;
        }

        if(AttendanceDAO.checkAttendanceStatus(eventIdSelected, sr_code)){
            System.out.println("The participant with an Sr-Code of " + sr_code + " is present.");
        }else{
            System.out.println("The participant with an Sr-Code of " + sr_code + " is absent.");
        }
    }

    private void markPresent(){
        Displayer.displayHeader("Marking for Present");
        if(eventDone){
            System.out.println("Event is already finished!");
            return;
        }

        String sr_code = InputGetter.getLine("Enter the Sr-Code of the participant: ");
        System.out.println();

        if(AttendanceDAO.checkAttendanceStatus(eventIdSelected, sr_code)){
            System.out.println("The participant with an Sr-Code of " + sr_code + " already present!");
            return;
        }

        if(RegistrationDAO.search(eventIdSelected, sr_code) == null){
            System.out.println("A participant with an Sr-Code of " + sr_code + " is not registered!");
            return;
        }
        AttendanceDAO.markPresent(eventIdSelected, sr_code);

        System.out.println("Participant '" + sr_code + "' was successfully marked as present.");
    }

    private void markAbsent(){
        Displayer.displayHeader("Marking for Absent");
        if(eventDone){
            System.out.println("Event is already finished!");
            return;
        }

        String sr_code = InputGetter.getLine("Enter the Sr-Code of the participant: ");
        System.out.println();

        if(!AttendanceDAO.checkAttendanceStatus(eventIdSelected, sr_code)){
            System.out.println("The participant with an Sr-Code of " + sr_code + " already absent!");
            return;
        }

        if(RegistrationDAO.search(eventIdSelected, sr_code) == null){
            System.out.println("A participant with an Sr-Code of " + sr_code + " is not registered!");
            return;
        }
        AttendanceDAO.markAbsent(eventIdSelected, sr_code);

        System.out.println("Participant '" + sr_code + "' was successfully marked as absent.");
    }
}
