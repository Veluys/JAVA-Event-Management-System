package ems.controller;

import ems.model.DeptDao;
import ems.model.EventDAO;
import ems.model.ParticipantDAO;
import ems.model.RegistrationDAO;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

public class RegController {
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

        switch (mainMenu()){
            case 1 -> addRegistration();
            case 2 -> viewRegistered();
            case 3 -> searchRegistered();
//            case 4 -> updateEvents();
//            case 5 -> deleteEvent();
        }
        System.out.println();
    }

    private int eventSelection(){
        displayer.displayHeader("Registration");
        String event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();
        return EventDAO.getEventId(event_name);
    }

    private int mainMenu(){
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("Add Participant", "View Participants", "Search Participant", "Remove Participants", "Exit")
        );
        displayer.showMenu("Select an operation:", operations);
        return inputGetter.getNumberOption(operations.size());
    }

    private void addRegistration(){
        ArrayList<String> deptShortNames = DeptDao.getDeptShortNames();

        if(deptShortNames==null){
            System.out.println("There is an unexpected error in fetching department names.");
            System.out.println("Please try again later.");
            return;
        }

        System.out.println("Please provide all of the following information.");
        String participant_id = String.format("'%s'", inputGetter.getLine("Sr-Code: "));
        String last_name = String.format("'%s'", inputGetter.getLine("Last Name: "));
        String first_name = String.format("'%s'", inputGetter.getLine("First Name: "));

        displayer.showMenu("Departments", deptShortNames);
        int option = inputGetter.getNumberOption(deptShortNames.size());
        int deptId = DeptDao.getDeptId(deptShortNames.get(option - 1));

        String participantDetails = String.format("%s, %d, %s, %s", participant_id, deptId, last_name, first_name);
        String regDetails = eventIdSelected + ", " + participant_id;

        System.out.println();
        ParticipantDAO.insert(participantDetails);
        RegistrationDAO.insert(regDetails);
    }

    private void viewRegistered(){
        ArrayList<ArrayList<String>> participants = RegistrationDAO.show(eventIdSelected);

        if(RegistrationDAO.isEmpty(eventIdSelected) || participants==null){
            System.out.println("There are no participants yet!");
            return;
        }
        ArrayList<String> columnHeaders = new ArrayList<>(
                Arrays.asList("Sr-Code", "Department", "Last Name", "First Name", "Attended")
        );

        displayer.centerAlignRow(columnHeaders);
        for(ArrayList<String> participant : participants){
            if(participant == participants.getFirst()) System.out.println();
            displayer.centerAlignRow(participant);
        }
    }
    private void searchRegistered(){
        if(RegistrationDAO.isEmpty(eventIdSelected)){
            System.out.println("There are no registered participants yet!");
            return;
        }

        String participant_id = inputGetter.getLine("Enter Sr-Code: ");
        System.out.println();

        String[] tableColumns = {"Sr-Code", "Department", "Last Name", "First Name", "Attended"};

        ArrayList<String> matchedParticipant = RegistrationDAO.search(eventIdSelected, participant_id);

        if(matchedParticipant==null){
            System.out.println("There are no participants that matched the given Sr-code!");
            return;
        }

        for(int i = 0; i < matchedParticipant.size(); i++){
            ArrayList<String> record = new ArrayList<>(Arrays.asList(tableColumns[i], matchedParticipant.get(i)));
            displayer.rightAlignRecord(record);
        }
    }
}
