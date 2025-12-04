package ems.controller;

import ems.dao.AttendanceDAO;
import ems.dao.EventDAO;
import ems.dao.RegistrationDAO;
import ems.model.Event;
import ems.model.Registration;
import ems.view.Displayer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

public class AttendanceController {
    private final EventDAO event_dao;
    private final RegistrationDAO reg_dao;
    private final AttendanceDAO att_dao;
    private Event selected_event;
    private ArrayList<ArrayList<String>> selected_event_details;
    private String selected_event_status;

    private final ArrayList<String> _VIEW_PARTICIPANTS_COL = new ArrayList<>(
            Arrays.asList("Sr-Code", "Department", "Year Level", "Full Name")
    );
    private final ArrayList<Double> _VIEW_PARTICIPANTS_SIZE = new ArrayList<>(Arrays.asList(0.15, 0.20, 0.15, 0.50));

    private final ArrayList<String> _VIEW_EVENT_COLUMN_HEADERS = new ArrayList<>(
            Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue")
    );
    private final ArrayList<Double> _VIEW_EVENT_COLUMN_SIZES = new ArrayList<>(Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20));

    public AttendanceController(Connection conn){
        this.event_dao = new EventDAO(conn);
        this.reg_dao = new RegistrationDAO(conn);
        this.att_dao = new AttendanceDAO(conn);
    }

    public void execute() {
        Displayer.displayHeader("Attendance Page");

        try {
            if (this.event_dao.empty_check()) {
                System.out.println("There are currently no events!");
                return;
            }
        } catch (Exception e) {
            Displayer.show_error(e);
            return;
        }

        String event_name = InputGetter.getLine("Event Name: ");
        System.out.println();

        try {
            selected_event = this.event_dao.record_search(event_name);
            this.selected_event_details = this.event_dao.display_search(event_name);
        } catch (Exception e) {
            System.out.println("Searching event records failed!");
            Displayer.show_error(e);
            return;
        }

        if (selected_event == null) {
            System.out.printf("There are no records that matched the event name '%s'", event_name);
            return;
        }

        try{
            if(this.reg_dao.empty_check(selected_event.get_event_id())){
                System.out.println("There are no participants for this event!");
                return;
            }
        } catch (Exception e) {
            System.out.println("Checking registration records failed!");
            Displayer.show_error(e);
            return;
        }

        try {
            this.selected_event_status = this.event_dao.check_status(event_name);
        } catch (Exception e) {
            System.out.println("Checking event status failed!");
            Displayer.show_error(e);
            return;
        }

        boolean isOngoing = selected_event_status.equalsIgnoreCase("ongoing");
        boolean completed = selected_event_status.equalsIgnoreCase("completed");

        if (isOngoing || completed) {
            attendance_menu(selected_event_status);
            System.out.println();
        }else{
            System.out.println("Attendance unavailable for noncompleted or non-ongoing events!");
        }
    }

    private void attendance_menu(String event_status){
        boolean is_completed = event_status.equals("completed");

        ArrayList<String> operations = new ArrayList<>();

        if(is_completed){
            operations.add("View Attendees");
            operations.add("View Absentees");
            operations.add("Search Participant");
            operations.add("Exit");
        }else{
            operations.add("View Attendees");
            operations.add("View Absentees");
            operations.add("Search Participant");
            operations.add("Set as Present");
            operations.add("Reset as Absent");
            operations.add("Exit");
        }

        while (true){
            Displayer.displayHeader("Attendance Page");
            Displayer.displayTable("Selected Event", this._VIEW_EVENT_COLUMN_HEADERS,
                    this.selected_event_details, this._VIEW_EVENT_COLUMN_SIZES);

            Displayer.displaySubheader("Attendance Menu");
            Displayer.showMenu("Select an operation: ", operations);
            int option = InputGetter.getNumberOption(operations.size());

            if(is_completed){
                switch (option){
                    case 1 -> view_attendance(true);
                    case 2 -> view_attendance(false);
                    case 3 -> get_attendance();
                    case 4 -> {
                        return;
                    }
                }
            }else{
                switch (option){
                    case 1 -> view_attendance(true);
                    case 2 -> view_attendance(false);
                    case 3 -> get_attendance();
                    case 4 -> update_attendance(true);
                    case 5 -> update_attendance(false);
                    case 6 -> {
                        return;
                    }
                }
            }
            System.out.println();
        }
    }

    private boolean is_ongoing(String event_name){
        try {
            this.selected_event_status = this.event_dao.check_status(event_name);
        } catch (Exception e) {
            System.out.println("Checking event status failed!");
            Displayer.show_error(e);
            return false;
        }

        if (!selected_event_status.equalsIgnoreCase("ongoing")) {
            System.out.println("Attendance unavailable for non-ongoing events!");
            return false;
        }
        return true;
    }

    private void main_att_menu(){
        while (true) {
            Displayer.displayHeader("Attendance Page");
            Displayer.displayTable("Selected Event", this._VIEW_EVENT_COLUMN_HEADERS,
                    this.selected_event_details, this._VIEW_EVENT_COLUMN_SIZES);

            Displayer.displaySubheader("Attendance Menu");
            ArrayList<String> att_operations = new ArrayList<>(
                    Arrays.asList("View Attendees", "View Absentees", "Search Participant",
                            "Set as Present", "Reset as Absent", "Exit")
            );

            Displayer.showMenu("Select an operation: ", att_operations);
            int option = InputGetter.getNumberOption(att_operations.size());

            switch (option) {
                case 1 -> view_attendance(true);
                case 2 -> view_attendance(false);
                case 3 -> get_attendance();
                case 4 -> update_attendance(true);
                case 5 -> update_attendance(false);
                case 6 -> {
                    return;
                }
            }
            System.out.println();
        }
    }

    private void view_attendance(boolean attended){
        String participant_status = attended ? "Attendees" : "Absentees";

        Displayer.displaySubheader(String.format("Viewing %s", participant_status));

        ArrayList<ArrayList<String>> participants;
        try{
            participants = this.att_dao.view_attendance(this.selected_event.get_event_id(), attended);
        }catch (Exception e) {
            System.out.printf("Fetching records of %s failed!\n", participant_status);
            Displayer.show_error(e);
            return;
        }

        if(participants==null){
            System.out.printf("There are currently no %s\n", participant_status);
            return;
        }

        Displayer.displayTable(participant_status, this._VIEW_PARTICIPANTS_COL,
                participants, this._VIEW_PARTICIPANTS_SIZE);
    }

    private void get_attendance(){
        Displayer.displaySubheader("Finding Participant's Attendance");
        String sr_code = InputGetter.getLine("Student Sr Code: ");
        System.out.println();

        Registration reg_record = new Registration(this.selected_event.get_event_id(), sr_code);

        try{
            if(this.reg_dao.search_registered(reg_record)==null){
                System.out.printf("There are no participants with an Sr Code of '%s'", sr_code);
                return;
            }
        }catch (Exception e) {
            System.out.println("Fetching student or/and registration records failed!");
            Displayer.show_error(e);
            return;
        }

        try{
            if(this.att_dao.is_attendee(reg_record)){
                System.out.printf("The participant with an Sr Code of %s is an attendee", sr_code);
            }else{
                System.out.printf("The participant with an Sr Code of %s is an absentee", sr_code);
            }
        }catch (Exception e) {
            System.out.println("Fetching the attendance status failed!");
            Displayer.show_error(e);
        }
    }

    private boolean can_update(Registration reg_record, boolean setPresent){
        try{
            if(this.reg_dao.search_registered(reg_record)==null){
                System.out.printf("There are no participants with an Sr Code of '%s'", reg_record.get_sr_code());
                return false;
            }
        }catch (Exception e) {
            System.out.println("Fetching student or/and registration records failed!");
            Displayer.show_error(e);
            return false;
        }

        boolean isPresent;
        try{
            isPresent = this.att_dao.is_attendee(reg_record);
        }catch (Exception e) {
            System.out.printf("Fetching records of %s failed!\n", reg_record.get_sr_code());
            Displayer.show_error(e);
            return false;
        }

        if(isPresent == setPresent){
            System.out.printf("The participant with an Sr-Code of %s is already marked as an %s\n",
                              reg_record.get_sr_code(), setPresent ? "attendee" : "absentee");
            return false;
        }
        return true;
    }

    private void update_attendance(boolean setPresent){
        if (setPresent){
            Displayer.displaySubheader("Marking Participant as Present");
        }else{
            Displayer.displaySubheader("Marking Participant as Absent");
        }

        String sr_code = InputGetter.getLine("Student Sr Code: ");
        System.out.println();

        Registration reg_record = new Registration(this.selected_event.get_event_id(), sr_code);
        if(!can_update(reg_record, setPresent)){
            return;
        }

        try{
            if(this.att_dao.update_attendance(reg_record, setPresent)){
                String success_msg = String.format("The participant with an Sr Code of %s was successfully marked as an %s",
                        reg_record.get_sr_code(), setPresent ? "attendee" : "absentee");

                System.out.println(success_msg);
            }else{
                throw new Exception();
            }
        }catch (Exception e) {
            System.out.printf("Updating attendance status of %s failed!", reg_record.get_sr_code());
            Displayer.show_error(e);
        }
    }
}