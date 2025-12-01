package ems.controller;

import ems.model.*;
import ems.view.Displayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class RegController {
    private int eventIdSelected;
    private boolean eventDone;

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

        String event_name = InputGetter.getLine("Enter event name: ");
        System.out.println();
        ArrayList<String> matchedEvent = EventDAO.searchRecord(event_name);

        if(matchedEvent==null){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        eventDone = LocalDate.parse(matchedEvent.get(2)).isBefore(LocalDate.now());
        eventIdSelected = Integer.parseInt(matchedEvent.get(0));

        while (true){
            Displayer.displayHeader("Registration Page");
            switch (mainMenu()){
                case 1 -> addRegistration();
                case 2 -> viewRegistered();
                case 3 -> searchRegistered();
                case 4 -> removeRegistered();
                case 5 -> {return;}
            }
            System.out.println();
        }
    }

    private int mainMenu(){
        Displayer.displaySubheader("Registration Menu");
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("Add Participant", "View Participants", "Search Participant", "Remove Participants", "Exit")
        );
        Displayer.showMenu("Select an operation:", operations);
        return InputGetter.getNumberOption(operations.size());
    }

    private void addRegistration(){
        Displayer.displayHeader("Adding Participant");

        if(eventDone){
            System.out.println("Event is already finished!");
            return;
        }

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
        Displayer.displaySubheader("Registered Participants");
        Displayer.displayTable(columnHeaders, participants, columnWidths);
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
        Displayer.displaySubheader("Matched Participant");
        Displayer.displayTable(columnHeaders, record, columnWidths);
    }
    private void removeRegistered(){
        Displayer.displayHeader("Removing Participant");

        if(eventDone){
            System.out.println("Event is already finished!");
            return;
        }

        if(RegistrationDAO.emptyCheck(eventIdSelected)){
            System.out.println("There are no registered participants yet!");
            return;
        }
        String sr_code = InputGetter.getLine("Enter Sr-code: ");
        System.out.println();
        RegistrationDAO.delete(eventIdSelected, sr_code);
    }
}
