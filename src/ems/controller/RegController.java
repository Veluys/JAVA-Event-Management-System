package ems.controller;

import ems.model.*;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

public class RegController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();
    private int eventIdSelected;

    public void execute() {
        if (EventDAO.emptyCheck()) {
            System.out.println("There are no events yet!");
            return;
        }

        if (StudentDao.emptyCheck()) {
            System.out.println("There are no students yet!");
            return;
        }

        eventIdSelected = eventSelection();
        if(eventIdSelected == -1){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        while (true){
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

    private int eventSelection(){
        displayer.displayHeader("Registration");
        String event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();
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

    private void addRegistration(){
        /*ArrayList<String> deptShortNames = DeptDao.getDeptShortNames();

        if(deptShortNames==null){
            System.out.println("There is an unexpected error in fetching department names.");
            System.out.println("Please try again later.");
            return;
        }*/

        String sr_code = inputGetter.getLine("Sr-Code: ");
        System.out.println();
        RegistrationDAO.insert(eventIdSelected, sr_code);
    }

    private void viewRegistered(){
        ArrayList<ArrayList<String>> participants = RegistrationDAO.show(eventIdSelected);

        if(RegistrationDAO.emptyCheck(eventIdSelected) || participants==null){
            System.out.println("There are no participants yet!");
            return;
        }

        ArrayList<String> columnHeaders = new ArrayList<>(
                Arrays.asList("Sr-Code", "Program", "Year Level", "Full Name")
        );

        displayer.centerAlignRow(columnHeaders);
        for(ArrayList<String> participant : participants){
            if(participant == participants.getFirst()) System.out.println();
            displayer.centerAlignRow(participant);
        }
    }

    private void searchRegistered(){
        if(RegistrationDAO.emptyCheck(eventIdSelected)){
            System.out.println("There are no registered participants yet!");
            return;
        }

        String sr_code = inputGetter.getLine("Enter Sr-Code: ");
        System.out.println();

        ArrayList<String> columnHeaders = new ArrayList<>(
                Arrays.asList("Sr-Code", "Program", "Year Level", "Full Name")
        );

        ArrayList<String> matchedParticipant = RegistrationDAO.search(eventIdSelected, sr_code);

        if(matchedParticipant==null){
            System.out.println("There are no participants that matched the given Sr-code!");
            return;
        }

        displayer.centerAlignRow(columnHeaders);
        displayer.centerAlignRow(matchedParticipant);
    }
    private void removeRegistered(){
        if(RegistrationDAO.emptyCheck(eventIdSelected)){
            System.out.println("There are no registered participants yet!");
            return;
        }
        String sr_code = inputGetter.getLine("Enter Sr-code: ");
        System.out.println();
        RegistrationDAO.delete(eventIdSelected, sr_code);
    }
}
