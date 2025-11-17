package ems.controller;

import ems.model.DeptDao;
import ems.model.EventDAO;
import ems.model.ParticipantDAO;
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
//            case 2 -> viewEvents();
//            case 3 -> searchEvent();
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
        String last_name = String.format("'%s'", inputGetter.getLine("Last Name: "));
        String first_name = String.format("'%s'", inputGetter.getLine("First Name: "));

        displayer.showMenu("Departments", deptShortNames);
        int option = inputGetter.getNumberOption(deptShortNames.size());
        int deptId = DeptDao.getDeptId(deptShortNames.get(option - 1));

        String participantDetails = String.format("%d, %s, %s", deptId, last_name, first_name);
        String regDetails = eventIdSelected + ", " + ParticipantDAO.getLastParticipantId();

        System.out.println();
        ParticipantDAO.insert(participantDetails);
        EventDAO.insert(regDetails);
    }
}
