package ems.controller;

import ems.model.EventDAO;
import ems.model.ParticipantDAO;
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
        int option = inputGetter.getNumberOption(operations.size());

        while (true){
            switch (option){
//                case 1 -> addRegistration();
//                case 2 -> viewRegistered();
//                case 3 -> searchRegistered();
//                case 4 -> removeRegistered();
                case 5 -> {return;}
            }
            System.out.println();
        }
    }
}
