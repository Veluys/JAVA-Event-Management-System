package ems.controller;

import ems.model.Event;
import ems.dao.EventDAO;
import ems.dao.VenueDAO;
import ems.view.Displayer;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class EventController {
    private final EventDAO event_dao;
    private final VenueDAO venue_dao;

    private final ArrayList<String> _VIEW_COLUMN_HEADERS = new ArrayList<>(Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue"));
    private final ArrayList<Double> _VIEW_COLUMN_SIZES = new ArrayList<>(Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20));

    public EventController(Connection conn) {
        this.event_dao = new EventDAO(conn);
        this.venue_dao = new VenueDAO(conn);
    }

    public void execute() {
        while (true) {
            Displayer.displayHeader("Events Page");

            try {
                if (venue_dao.empty_check()) {
                    System.out.println("There are currently no venues!\n");
                    return;
                }
            } catch (Exception e) {
                Displayer.show_error(e);
                return;
            }

            Displayer.displaySubheader("Event Menu");
            ArrayList<String> event_operations = new ArrayList<>(
                    Arrays.asList("Add Events", "View Completed Events", "View Scheduled Events",
                            "View Upcoming Events", "View Ongoing Events", "Search Events",
                            "Update Events", "Delete Events", "Exit")
            );

            Displayer.showMenu("Select an operation: ", event_operations);
            int option = InputGetter.getNumberOption(event_operations.size());

            try {
                if (option >= 2 && option != 9 && this.event_dao.empty_check()) {
                    System.out.println("There are currently no events!\n");
                    return;
                }
            } catch (Exception e) {
                Displayer.show_error(e);
                return;
            }

            switch (option) {
                case 1 -> addEvent();
                case 2 -> viewEvents("completed");
                case 3 -> viewEvents("scheduled");
                case 4 -> viewEvents("upcoming");
                case 5 -> viewEvents("ongoing");
                case 6 -> searchEvent();
                case 7 -> updateEvent();
                case 8 -> deleteEvent();
                case 9 -> {
                    return;
                }
            }
            System.out.println();
        }
    }

    private ArrayList<ArrayList<String>> get_matchedEvent(String event_name){
        try {
            return this.event_dao.display_search(event_name);
        } catch (Exception e) {
            System.out.println("Checking if event_name already exists failed!");
            Displayer.show_error(e);
            return null;
        }
    }

    private ArrayList<ArrayList<String>> get_overlapped_events(Event event){
        try {
            return this.event_dao.view_overlapped_events(event);
        } catch (Exception e) {
            System.out.println("Checking for overlapped events failed!");
            Displayer.show_error(e);
            return null;
        }
    }

    private ArrayList<String> get_venues(){
        try {
            return this.venue_dao.getVenueNames();
        } catch (Exception e) {
            System.out.println("Fetching venue names failed!");
            Displayer.show_error(e);
            return null;
        }
    }

    private void addEvent() {
        Displayer.displaySubheader("Adding Event");

        ArrayList<String> venues = get_venues();
        if(venues==null) return;

        String event_name = InputGetter.getLine("Event name: ");
        ArrayList<ArrayList<String>> matched_event = get_matchedEvent(event_name);

        if (matched_event != null) {
            System.out.printf("An event with an event name of '%s' already exists in the database!\n", event_name);
            Displayer.displayTable("Matched Event Name", this._VIEW_COLUMN_HEADERS, matched_event, this._VIEW_COLUMN_SIZES);
            return;
        }

        LocalDate event_date = InputGetter.getDate("Event Date");
        LocalTime start_time = InputGetter.getTime("Start Time");
        LocalTime end_time;
        while (true) {
            end_time = InputGetter.getTime("End Time");

            try {
                if (!end_time.isAfter(start_time)) {
                    throw new InputMismatchException("Error! End Time is before Start Time!");
                }
            } catch (Exception e) {
                Displayer.show_error(e);
                continue;
            }
            break;
        }

        Displayer.showMenu("Venues: ", venues);
        int venue_option = InputGetter.getNumberOption(venues.size()) - 1;

        int venue_id;
        try {
            venue_id = this.venue_dao.getVenueId(venues.get(venue_option));
        } catch (Exception e) {
            System.out.println("Matching venue id for the selected venue name failed");
            Displayer.show_error(e);
            return;
        }

        Event new_event = new Event(event_name, event_date, start_time, end_time, venue_id);

        ArrayList<ArrayList<String>> overlapped_events = get_overlapped_events(new_event);

        if (overlapped_events != null) {
            System.out.println("The event can't be added to the database!");
            Displayer.displayTable("Overlapped Events", this._VIEW_COLUMN_HEADERS, overlapped_events, this._VIEW_COLUMN_SIZES);
            return;
        }

        try {
            if (this.event_dao.insert_event(new_event)) {
                System.out.println("New event was added successfully");
            } else throw new Exception();
        } catch (Exception e) {
            System.out.println("Adding new event failed!");
            Displayer.show_error(e);
        }
    }

    private void viewEvents(String event_status) {
        ArrayList<ArrayList<String>> events = new ArrayList<>();
        try {
            events = this.event_dao.view_events(event_status);
        } catch (Exception e) {
            System.out.println("Fetching event records failed!");
            Displayer.show_error(e);
            return;
        }

        if (events == null) {
            System.out.printf("There are currently no %s events!\n", event_status);
            return;
        }

        String table_name = String.format("%s EVENTS", event_status.toUpperCase());
        Displayer.displayTable(table_name, this._VIEW_COLUMN_HEADERS, events, this._VIEW_COLUMN_SIZES);
    }

    private void searchEvent() {
        Displayer.displaySubheader("Search Event");

        String event_name = InputGetter.getLine("Event Name: ");
        System.out.println();

        ArrayList<ArrayList<String>> matched_event = get_matchedEvent(event_name);

        if (matched_event == null) {
            System.out.printf("There are no records that matched the event name '%s'\n", event_name);
            return;
        }

        Displayer.displayTable("Matched Event", this._VIEW_COLUMN_HEADERS, matched_event, this._VIEW_COLUMN_SIZES);
    }

    private boolean is_manipulable(String event_name, String operation) {
        String event_status;
        try {
            event_status = this.event_dao.check_status(event_name);
        } catch (Exception e) {
            System.out.println("Checking event status failed!");
            Displayer.show_error(e);
            return false;
        }

        boolean is_completed = event_status.equalsIgnoreCase("completed");
        boolean is_ongoing = event_status.equalsIgnoreCase("ongoing");

        if(operation.equalsIgnoreCase("update") && is_completed){
            System.out.println("Completed events can't be updated!");
            return false;
        }

        if(operation.equalsIgnoreCase("delete") && is_ongoing){
            System.out.println("Ongoing events can't be deleted!");
            return false;
        }

        return true;
    }

    private void updateEvent() {
        Displayer.displaySubheader("Updating Event");
        String old_event_name = InputGetter.getLine("Event name: ");

        Event upd_event;
        try {
            upd_event = this.event_dao.record_search(old_event_name);
        } catch (Exception e) {
            System.out.println("Searching event records failed!");
            Displayer.show_error(e);
            return;
        }

        if (upd_event == null) {
            System.out.printf("There are no records that matched the event name '%s' \n", old_event_name);
            return;
        }

        if (!this.is_manipulable(old_event_name, "update")) {
            return;
        }

        ArrayList<String> venues = get_venues();
        if(venues==null) return;

        System.out.println("Note: Only provide values to the field(s) you want to update. Otherwise simply press enter.");

        String new_event_name = InputGetter.getLine("Event name: ", true);
        if (!new_event_name.isBlank()) {
            ArrayList<ArrayList<String>> matched_event = get_matchedEvent(new_event_name);

            if (matched_event != null) {
                System.out.printf("An event with an event name of '%s' already exists in the database\n", new_event_name);
                Displayer.displayTable("Matched Event Name", this._VIEW_COLUMN_HEADERS, matched_event, this._VIEW_COLUMN_SIZES);
                return;
            } else {
                upd_event.set_event_name(new_event_name);
            }
        }

        LocalDate event_date = InputGetter.getDate("Event Date", true);
        if (event_date != null) upd_event.set_event_date(event_date);

        LocalTime start_time = InputGetter.getTime("Start Time", true);
        if (start_time != null) upd_event.set_start_time(start_time);

        LocalTime end_time;
        while (true) {
            end_time = InputGetter.getTime("End Time", true);

            if (end_time != null) {
                try {
                    if (!end_time.isAfter(upd_event.get_start_time())) {
                        throw new InputMismatchException("Error! End Time is before Start Time!");
                    }
                } catch (Exception e) {
                    Displayer.show_error(e);
                    continue;
                }
            }
            break;
        }

        Displayer.showMenu("Venues: ", venues);
        int venue_option = InputGetter.getNumberOption(venues.size(), true);

        if (venue_option != -1) {
            try {
                upd_event.set_venue_id(this.venue_dao.getVenueId(venues.get(venue_option - 1)));
            } catch (Exception e) {
                System.out.println("Matching venue id for the selected venue name failed");
                Displayer.show_error(e);
                return;
            }
        }

        ArrayList<ArrayList<String>> overlapped_events = get_overlapped_events(upd_event);

        if (overlapped_events != null) {
            System.out.println("The event can't be updated!");
            Displayer.displayTable("Overlapped Events", this._VIEW_COLUMN_HEADERS, overlapped_events, this._VIEW_COLUMN_SIZES);
            return;
        }

        try {
            if (this.event_dao.update_event(upd_event)) {
                System.out.println("Event was updated successfully");
            } else throw new Exception();
        } catch (Exception e) {
            System.out.printf("Updating the event record of %s failed!", old_event_name);
            Displayer.show_error(e);
        }
    }

    private void deleteEvent() {
        Displayer.displaySubheader("Deleting Event");
        System.out.println("Note: Deleting an event would also delete its participants!");

        String event_name = InputGetter.getLine("Event name: ");
        System.out.println();

        ArrayList<ArrayList<String>> matched_event = get_matchedEvent(event_name);

        if (matched_event == null) {
            System.out.printf("There are no records that matched the event name '%s' \n", event_name);
            return;
        }

        if (!this.is_manipulable(event_name, "delete")) {
            return;
        }

        try {
            if (this.event_dao.delete_event(event_name)) {
                System.out.println("Event was deleted successfully");
            } else throw new Exception();
        } catch (Exception e) {
            System.out.printf("Deleting the event record of %s failed!", event_name);
            Displayer.show_error(e);
        }
    }
}
