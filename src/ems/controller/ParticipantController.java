package ems.controller;

import ems.model.EventDAO;
import ems.model.ParticipantDAO;
import ems.model.RegistrationDAO;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

public class ParticipantController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();

    public void execute() {
        if (!ParticipantDAO.participantsExist()) {
            System.out.println("There are no participants!");
            return;
        }

        while (true){
            switch (mainMenu()){
                case 1 -> viewParticipants();
                case 2 -> searchParticipant();
//                case 3 -> searchRegistered();
//                case 4 -> removeRegistered();
                case 5 -> {return;}
            }
            System.out.println();
        }
    }

    private int mainMenu(){
        displayer.displayHeader("Participants");
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList(
                        "View Participants",
                        "Search Participant",
                        "Update Participant",
                        "Remove Participants",
                        "Exit"
                )
        );
        displayer.showMenu("Select an operation:", operations);
        return inputGetter.getNumberOption(operations.size());
    }

    private void viewParticipants(){
        ArrayList<ArrayList<String>> participants = ParticipantDAO.show();

        if(participants==null){
            System.out.println("There are no participants yet!");
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

    private void searchParticipant(){
        String participant_id = inputGetter.getLine("Enter Sr-Code: ");
        System.out.println();

        String[] tableColumns = {"Sr-Code", "Department", "Last Name", "First Name"};
        String condition = "participant_id = '" + participant_id + "'";

        ArrayList<String> matchedParticipant = ParticipantDAO.search(condition);

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
