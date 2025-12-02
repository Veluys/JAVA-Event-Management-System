package ems.controller;

import ems.model.*;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

public class RegController {
    private int eventIdSelected;

    public void execute() {
        Displayer.displayHeader("Registration Page");

        if (EventDAO.emptyCheck()) {
            System.out.println("There are no events yet!");
            return;
        }

        if (StudentDao.emptyCheck()) {
            System.out.println("There are no students yet!");
            return;
        }

        String eventName = InputGetter.getLine("Enter event name: ");
        System.out.println();
        eventIdSelected = EventDAO.getEventId(eventName);

        if (eventIdSelected == -1) {
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        boolean shouldLoop = true;
        while (shouldLoop) {
            displaySelectedEvent(eventName);

            Displayer.displaySubheader("Registration Menu");
            String status = EventDAO.checkStatus(eventName).toLowerCase();

            boolean selectedEventCompleted = status.equalsIgnoreCase("completed");
            if(selectedEventCompleted){
                shouldLoop = menuForCompletedEvents();
            }else {
                shouldLoop = menuForNonCompletedEvents();
            }
        }
    }

    private void displaySelectedEvent(String eventName) {
        Displayer.displayHeader("Registration Page");

        ArrayList<String> eventAttributes = new ArrayList<>(
                Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue")
        );

        ArrayList<Double> columnWidths = new ArrayList<>(
                Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20)
        );

        ArrayList<ArrayList<String>> selectedEvent = new ArrayList<>();
        selectedEvent.add(EventDAO.search(eventName));

        Displayer.displayTable("Currently Selected Event", eventAttributes, selectedEvent, columnWidths);
    }

    private boolean hasNoParticipants() {
        return RegistrationDAO.show(eventIdSelected) == null;
    }

    private boolean menuForNonCompletedEvents() {
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("Add Participant", "View Participants", "Search Participant", "Remove Participants", "Exit")
        );
        Displayer.showMenu("Select an operation:", operations);
        int option = InputGetter.getNumberOption(operations.size());

        if (hasNoParticipants() && option != 1 && option != 5) {
            System.out.println("There are currently no participants");
            System.out.println();
            return true;
        }

        switch (option) {
            case 1 -> addRegistration();
            case 2 -> viewRegistered();
            case 3 -> searchRegistered();
            case 4 -> removeRegistered();
            case 5 -> { return false; }
        }
        System.out.println();
        return true;
    }

    private boolean menuForCompletedEvents() {
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("View Participants", "Search Participant", "Exit")
        );
        Displayer.showMenu("Select an operation:", operations);
        int option = InputGetter.getNumberOption(operations.size());

        if (hasNoParticipants()) {
            System.out.println("There are no participants for this event.");
            return true;
        }

        switch (option) {
            case 1 -> viewRegistered();
            case 2 -> searchRegistered();
            case 3 -> { return false; }
        }
        System.out.println();
        return true;
    }

    private void addRegistration(){
        Displayer.displayHeader("Adding Participant");
        String sr_code = InputGetter.getLine("Sr-Code: ");
        System.out.println();
        RegistrationDAO.insert(eventIdSelected, sr_code);
    }

    private void viewRegistered(){
        Displayer.displayHeader("Viewing Participants");
        ArrayList<ArrayList<String>> participants = RegistrationDAO.show(eventIdSelected);

        if(RegistrationDAO.emptyCheck(eventIdSelected) || participants==null){
            System.out.println("There are no participants yet!");
            return;
        }

        ArrayList<String> columnHeaders = new ArrayList<>(
                Arrays.asList("Sr-Code", "Department", "Year Level", "Full Name")
        );

        ArrayList<Double> columnWidths = new ArrayList<>(
                Arrays.asList(0.15, 0.20, 0.15, 0.50)
        );
        Displayer.displayTable("Registered Participants", columnHeaders, participants, columnWidths);
    }

    private void searchRegistered(){
        Displayer.displayHeader("Searching Participant");
        if(RegistrationDAO.emptyCheck(eventIdSelected)){
            System.out.println("There are no registered participants yet!");
            return;
        }

        String sr_code = InputGetter.getLine("Enter Sr-Code: ");
        System.out.println();

        ArrayList<String> columnHeaders = new ArrayList<>(
                Arrays.asList("Sr-Code", "Department", "Year Level", "Full Name")
        );

        ArrayList<String> matchedParticipant = RegistrationDAO.search(eventIdSelected, sr_code);
        ArrayList<ArrayList<String>> record = new ArrayList<>(
                Arrays.asList(matchedParticipant)
        );

        if(matchedParticipant==null){
            System.out.println("There are no participants that matched the given Sr-code!");
            return;
        }

        ArrayList<Double> columnWidths = new ArrayList<>(
                Arrays.asList(0.15, 0.20, 0.15, 0.50)
        );

        Displayer.displayTable("Matched Participant", columnHeaders, record, columnWidths);
    }
    private void removeRegistered(){
        Displayer.displayHeader("Removing Participant");
        if(RegistrationDAO.emptyCheck(eventIdSelected)){
            System.out.println("There are no registered participants yet!");
            return;
        }
        String sr_code = InputGetter.getLine("Enter Sr-code: ");
        System.out.println();
        RegistrationDAO.delete(eventIdSelected, sr_code);
    }
}
