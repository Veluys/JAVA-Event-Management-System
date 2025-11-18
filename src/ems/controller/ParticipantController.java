package ems.controller;

import ems.model.*;
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
                case 3 -> updateParticipant();
                case 4 -> removeParticipant();
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

    private void updateParticipant(){
        String participant_id = inputGetter.getLine("Enter Sr-Code: ");
        System.out.println();

        if(!ParticipantDAO.participantExist("participant_id = '" + participant_id + "'")){
            System.out.println("There are no participants with Sr-Code of '" + participant_id + "'.");
            return;
        }

        String condition = "participant_id = '" + participant_id + "'";

        ArrayList<String> deptShortNames = DeptDao.getDeptShortNames();

        if(deptShortNames==null){
            System.out.println("There is an unexpected error in fetching department names.");
            System.out.println("Please try again later.");
            return;
        }

        ArrayList<String> changes = new ArrayList<>();

        System.out.println("Only provide information to the fields you want to update.");

        String participantId = inputGetter.getLine("Sr-Code: ", true);
        if(!participantId.isBlank()){
            changes.add("participant_id = '" + participantId + "'");
        }

        String last_name = inputGetter.getLine("Last Name: ", true);
        if(!last_name.isBlank()) {
            changes.add("last_name = '" + last_name + "'");
        }

        String first_name = inputGetter.getLine("First Name: ", true);
        if(!first_name.isBlank()){
            changes.add("first_name = '" + first_name + "'");
        }

        displayer.showMenu("Departments", deptShortNames);
        int option = inputGetter.getNumberOption(deptShortNames.size(), true);
        int dept_id = DeptDao.getDeptId(deptShortNames.get(option - 1));
        if(dept_id != -1){
            changes.add("dept_id = " + dept_id);
        }

        System.out.println();
        ParticipantDAO.update(changes, condition);
    }

    private void removeParticipant(){
        String participant_id = inputGetter.getLine("Enter Sr-Code: ");
        System.out.println();

        if(!ParticipantDAO.participantExist("participant_id = '" + participant_id + "'")){
            System.out.println("There are no participants with Sr-Code of '" + participant_id + "'.");
            return;
        }

        String condition = "participant_id = '" + participant_id + "'";

        System.out.println();
        RegistrationDAO.delAllParticipantReg(participant_id);
        ParticipantDAO.delete(condition);
    }
}
