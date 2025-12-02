package ems.controller;

import ems.model.AttendanceDAO;
import ems.model.EventDAO;
import ems.model.RegistrationDAO;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

public class AttendanceController {
    private int eventIdSelected;
    private String event_name;

    public void execute() {
        Displayer.displayHeader("Attendance Page");
        if (EventDAO.emptyCheck()) {
            System.out.println("There are no events yet!");
            return;
        }

        event_name = InputGetter.getLine("Enter event name: ");
        System.out.println();

        eventIdSelected = EventDAO.getEventId(event_name);

        if(eventIdSelected==-1){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        boolean should_loop = true;
        while(should_loop){
            String event_status = EventDAO.checkStatus(event_name);

            boolean eventSelectedOngoing = event_status.equalsIgnoreCase("ongoing");
            boolean eventSelectedCompleted = event_status.equalsIgnoreCase("completed");

            if(eventSelectedOngoing){
                if(participantsExist()){
                    base_menu();
                    should_loop = menuForOnGoingEvent();
                }
            }else if(eventSelectedCompleted){
                if(participantsExist()){
                    base_menu();
                    should_loop = menuForCompletedEvent();
                }
            }else{
                System.out.println("Attendance unavailable for non-completed and non-ongoing events");
                return;
            }
        }
    }

    private void base_menu(){
        Displayer.displayHeader("Attendance Page");

        ArrayList<String> event_attributes = new ArrayList<>(
                Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue")
        );
        ArrayList<Double> columnWidths = new ArrayList<>(
                Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20)
        );

        ArrayList<ArrayList<String>> selected_event = new ArrayList<>();
        selected_event.add(EventDAO.search(event_name));

        Displayer.displayTable("Currently Selected Event",event_attributes, selected_event, columnWidths);

        Displayer.displaySubheader("Attendance Menu");
    }

    private boolean participantsExist(){
        if(RegistrationDAO.emptyCheck(eventIdSelected)){
            System.out.println("There are no participants yet!");
            return false;
        }
        return true;
    }

    private boolean menuForOnGoingEvent(){
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("View Attendees", "View Absentees", "Check Participant's Attendance",
                        "Set as Present", "Reset as Absent", "Exit")
        );

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

        String table_name = attendanceStatus ? "Attendees" : "Absentees";
        Displayer.displayTable(table_name, columnHeaders, participants, columnWidths);
        InputGetter.getLine("Press any button to return: ", true);
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
        InputGetter.getLine("Press any button to return: ", true);
    }

    private void markPresent(){
        Displayer.displayHeader("Marking for Present");

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
        InputGetter.getLine("Press any button to return: ", true);
    }

    private void markAbsent(){
        Displayer.displayHeader("Marking for Absent");

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
        InputGetter.getLine("Press any button to return: ", true);
    }
}
