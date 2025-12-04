package ems.controller;

import ems.dao.EventDAO;
import ems.dao.RegistrationDAO;
import ems.dao.StudentDao;
import ems.model.*;
import ems.view.Displayer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

public class RegController {
    private final EventDAO event_dao;
    private final RegistrationDAO reg_dao;
    private final StudentDao student_dao;

    private Event eventSelected;
    private ArrayList<ArrayList<String>> eventSelectedDetails;
    private final ArrayList<String> _VIEW_PARTICIPANT_HEADERS = new ArrayList<>(
            Arrays.asList("Sr-Code", "Department", "Year Level", "Full Name")
    );
    private final ArrayList<Double> _VIEW_PARTICIPANT_COLUMN_SIZES = new ArrayList<>(Arrays.asList(0.15, 0.20, 0.15, 0.50));

    private final ArrayList<String> _VIEW_EVENT_COLUMN_HEADERS = new ArrayList<>(
            Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue")
    );
    private final ArrayList<Double> _VIEW_EVENT_COLUMN_SIZES = new ArrayList<>(Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20));

    public RegController(Connection conn) {
        this.event_dao = new EventDAO(conn);
        this.reg_dao = new RegistrationDAO(conn);
        this.student_dao = new StudentDao(conn);
    }

    public void execute(){
        Displayer.displayHeader("Registration Page");

        try{
            if(this.event_dao.empty_check()){
                System.out.println("There are currently no events!\n");
                return;
            }
        }catch (Exception e){
            Displayer.show_error(e);
            return;
        }

        String event_name = InputGetter.getLine("Event Name: ");
        System.out.println();

        try{
            eventSelected = this.event_dao.record_search(event_name);
        }catch (Exception e){
            System.out.println("Searching event records failed!");
            Displayer.show_error(e);
            return;
        }

        if(eventSelected==null){
            System.out.printf("There are no records that matched the event name '%s' \n", event_name);
            return;
        }

        String selectedEventStatus;
        try{
            selectedEventStatus = this.event_dao.check_status(eventSelected.get_event_name());
            eventSelectedDetails = this.event_dao.display_search(event_name);
        }catch (Exception e){
            System.out.println("Checking event status failed!");
            Displayer.show_error(e);
            return;
        }

        registration_menu(selectedEventStatus);

        System.out.println();
    }

    private void registration_menu(String event_status){
        boolean is_completed = event_status.equals("completed");

        ArrayList<String> operations = new ArrayList<>();

        if(is_completed){
            operations.add("View Participants");
            operations.add("Search Participant");
            operations.add("Exit");
        }else{
            operations.add("Add Participants");
            operations.add("View Participants");
            operations.add("Search Participant");
            operations.add("Remove Participants");
            operations.add("Exit");
        }

        while (true){
            Displayer.displayHeader("Registration Page");
            Displayer.displayTable("Selected Event", this._VIEW_EVENT_COLUMN_HEADERS,
                    this.eventSelectedDetails, this._VIEW_EVENT_COLUMN_SIZES);

            Displayer.displaySubheader("Registration Menu");
            Displayer.showMenu("Select an operation: ", operations);
            int option = InputGetter.getNumberOption(operations.size());

            if (is_completed || (option >= 2 && option != operations.size())){
                try{
                    if(this.reg_dao.empty_check(this.eventSelected.get_event_id())){
                        System.out.println("There are no participants!");
                        continue;
                    }
                }catch (Exception e){
                    System.out.println("Checking registration records failed!");
                    Displayer.show_error(e);
                    return;
                }
            }

            if(is_completed){
                switch (option){
                    case 1 -> view_registered();
                    case 2 -> search_registered();
                    case 3 -> {
                        return;
                    }
                }
            }else{
                switch (option){
                    case 1 -> addRegistration();
                    case 2 -> view_registered();
                    case 3 -> search_registered();
                    case 4 -> remove_participant();
                    case 5 -> {
                        return;
                    }
                }
            }
            System.out.println();
        }
    }

    private void addRegistration(){
        Displayer.displaySubheader("Adding Participant");
        String sr_code = InputGetter.getLine("Student Sr Code: ");

        try{
            if(!this.student_dao.studentExist(sr_code)){
                System.out.printf("There are no students that matched the given Sr-Code %s\n", sr_code);
                return;
            }
        }catch (Exception e){
            System.out.println("Searching student records failed!");
            Displayer.show_error(e);
            return;
        }

        Registration new_reg_record = new Registration(this.eventSelected.get_event_id(), sr_code);

        try{
            if(this.reg_dao.search_registered(new_reg_record)!=null){
                System.out.printf("Student with Sr-Code of %s is already registered \n", sr_code);
                return;
            }
        }catch (Exception e){
            System.out.println("Searching student records failed!");
            Displayer.show_error(e);
            return;
        }

        try{
            if(this.reg_dao.insert_participant(new_reg_record)){
                System.out.println("New participant was added successfully!");
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            System.out.println("Inserting new registration failed!");
            Displayer.show_error(e);
        }
    }

    private ArrayList<ArrayList<String>> get_all_participant(){
        ArrayList<ArrayList<String>> participants = new ArrayList<>();
        try{
            participants = this.reg_dao.view_registered(this.eventSelected.get_event_id());
            return participants;
        }catch (Exception e){
            System.out.println("Fetching student records failed!");
            Displayer.show_error(e);
            return null;
        }
    }

    private ArrayList<ArrayList<String>> get_one_participant(Registration reg_record){
        ArrayList<ArrayList<String>> participants = new ArrayList<>();
        try{
            participants = this.reg_dao.search_registered(reg_record);
            return participants;
        }catch (Exception e){
            System.out.println("Fetching student records failed!");
            Displayer.show_error(e);
            return null;
        }
    }

    private void view_registered(){
        Displayer.displaySubheader("Viewing Participants");
        ArrayList<ArrayList<String>> participants = get_all_participant();

        if(participants==null){
            System.out.println("There are currently no participants!");
            return;
        }
        Displayer.displayTable("Registered Participants", this._VIEW_PARTICIPANT_HEADERS,
                participants, this._VIEW_PARTICIPANT_COLUMN_SIZES);
    }

    private void search_registered(){
        Displayer.displaySubheader("Searching Participants");
        String sr_code = InputGetter.getLine("Student Sr Code: ");
        System.out.println();

        ArrayList<ArrayList<String>> participant = get_one_participant(new Registration(eventSelected.get_event_id(), sr_code));

        if(participant==null){
            System.out.printf("There are no participants with an Sr-Code '%s'\n", sr_code);
        }else{
            Displayer.displayTable("Matched Participants", this._VIEW_PARTICIPANT_HEADERS,
                    participant, this._VIEW_PARTICIPANT_COLUMN_SIZES);
        }
    }

    private void remove_participant(){
        Displayer.displaySubheader("Remove Participant");
        String sr_code = InputGetter.getLine("Student Sr Code: ");
        System.out.println();

        ArrayList<ArrayList<String>> participant = get_one_participant(new Registration(eventSelected.get_event_id(), sr_code));

        if(participant==null) {
            System.out.printf("There are no participants with an Sr-Code '%s'\n", sr_code);
            return;
        }

        try{
            if(this.reg_dao.delete_registration(new Registration(eventSelected.get_event_id(), sr_code))){
                System.out.println("Participant was removed successfully");
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            System.out.println("Removing the participant failed!");
            Displayer.show_error(e);
        }
    }
}
