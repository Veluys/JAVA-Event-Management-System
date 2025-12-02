package ems.controller;

import ems.model.EventDAO;
import ems.model.VenueDAO;
import ems.view.Displayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class EventController {
    private static final ArrayList<String> event_attributes = new ArrayList<>(
            Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue")
    );

    public static void execute(){
        while(true){
            Displayer.displayHeader("Events Page");
            ArrayList<String> operations = new ArrayList<>(
                    Arrays.asList("Add Events", "View Completed Events", "View Scheduled Events",
                                  "View Upcoming Events", "View Ongoing Events", "Search Events",
                                  "Update Events", "Delete Events", "Exit")
            );

            Displayer.displaySubheader("Event Menu");
            Displayer.showMenu("Select an operation:", operations);
            int option = InputGetter.getNumberOption(operations.size());
            System.out.println();

            if(EventDAO.emptyCheck() && option > 1 && option != 9){
                System.out.println("There are no events yet!");
            }else{
                switch (option){
                    case 1 -> addEvent();
                    case 2 -> viewEvents("completed", true);
                    case 3 -> viewEvents("scheduled", true);
                    case 4 -> viewEvents("upcoming", true);
                    case 5 -> viewEvents("ongoing", true);
                    case 6 -> searchEvent();
                    case 7 -> updateEvents();
                    case 8 -> deleteEvent();
                    case 9 -> {return;}
                }
            }
            System.out.println();
        }
    }

    private static void addEvent(){
        ArrayList<String> venueNames = VenueDAO.getVenueNames();
        if(venueNames == null){
            System.out.println("There are no available venues yet!");
            return;
        }

        Displayer.displayHeader("Adding New Event");

        String eventName = InputGetter.getLine("Event Name: ");
        if(EventDAO.eventExist(eventName)){
            System.out.printf("Error: Event Name '%s' already exists!\n", eventName);
            return;
        }

        LocalDate date = InputGetter.getDate("Event Date");

        if(date.isBefore(LocalDate.now()) || date.equals(LocalDate.now())){
            System.out.println("Error: Event Date must be at least 1 day from now");
            return;
        }

        LocalTime start_time = InputGetter.getTime("Start Time");
        LocalTime end_time = InputGetter.getTime("End Time");

        if(end_time.isBefore(start_time) || end_time.equals(start_time)){
            System.out.println("Error: End time must be after start time!");
            return;
        }

        Displayer.showMenu("Venues", venueNames);
        int option = InputGetter.getNumberOption(venueNames.size());
        int venue_id = VenueDAO.getVenueId(venueNames.get(option - 1));

        ArrayList<ArrayList<String>> conflictEvents = EventDAO.eventsInConflict(-1, date, start_time, venue_id);
        if(conflictEvents!= null){
            System.out.println("The event can't be added to the database!");
            ArrayList<Double> columnWidths = new ArrayList<>(
                    Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20)
            );
            Displayer.displayTable("Overlapped Events", event_attributes, conflictEvents, columnWidths);
            return;
        }

        EventDAO.insert(eventName, date, start_time, end_time, venue_id);
    }

    public static void viewEvents(String event_status, boolean displayNoneMsg){
        ArrayList<ArrayList<String>> events = new ArrayList<>();

        boolean isValidStatus = event_status.equalsIgnoreCase("upcoming") ||
                                event_status.equalsIgnoreCase("scheduled") ||
                                event_status.equalsIgnoreCase("ongoing") ||
                                event_status.equalsIgnoreCase("completed");
        if(isValidStatus){
            events = EventDAO.showEvents(event_status);
        }else{
            System.out.println("Event status provided was invalid!");
        }

        if(events==null){
            if(displayNoneMsg){
                System.out.printf("There are no %s events yet.\n", event_status);
            }
            return;
        }

        ArrayList<Double> columnWidths = new ArrayList<>(
                Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20)
        );

        String table_name = event_status.toUpperCase() + " EVENTS";
        Displayer.displayTable(table_name, event_attributes, events, columnWidths);
    }

    private static void searchEvent(){
        Displayer.displayHeader("Searching Event");
        String eventName = InputGetter.getLine("Enter event name: ");
        System.out.println();

        ArrayList<String> matchedEvent = EventDAO.search(eventName);
        ArrayList<ArrayList<String>> records = new ArrayList<>(
                Arrays.asList(matchedEvent)
        );

        if(matchedEvent==null){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        ArrayList<Double> columnWidths = new ArrayList<>(
                Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20)
        );

        Displayer.displayTable("Events", event_attributes, records, columnWidths);
    }

    private static void updateEvents(){
        ArrayList<String> venueNames = VenueDAO.getVenueNames();
        if(venueNames == null){
            System.out.println("There are no available venues yet!");
            return;
        }

        Displayer.displayHeader("Updating Event");
        String old_event_name = InputGetter.getLine("Enter event name: ");
        System.out.println();

        ArrayList<String> matchedEvent = EventDAO.searchRecord(old_event_name);

        if(matchedEvent == null){
            System.out.println("Event name of '" + old_event_name + "' doesn't exist!");
            return;
        }

        boolean eventOngoing = EventDAO.checkStatus(old_event_name).equalsIgnoreCase("ongoing");
        boolean eventCompleted = EventDAO.checkStatus(old_event_name).equalsIgnoreCase("completed");
        if(eventOngoing){
            System.out.println("Ongoing events can't be updated!");
            return;
        }

        if(eventCompleted){
            System.out.println("Completed events can't be updated!");
            return;
        }

        LinkedHashMap<String, String> new_values = new LinkedHashMap<>();

        System.out.println("Simply press enter to not update that field.");

        String new_event_name = InputGetter.getLine("New Event Name: ",true);
        if(!new_event_name.isBlank()){
            if(EventDAO.eventExist(new_event_name)){
                System.out.println("Event name of '" + new_event_name + "' already exists!");
                return;
            }
            new_values.put("event_name", new_event_name);
        }

        LocalDate new_event_date = InputGetter.getDate("New Event Date",true);
        if(new_event_date != null){
            if(new_event_date.isBefore(LocalDate.now()) || new_event_date.equals(LocalDate.now())){
                System.out.println("Error: Event Date must be at least 1 day from now");
                return;
            }
            new_values.put("event_date", String.valueOf(new_event_date));
        }

        LocalTime new_start_time = InputGetter.getTime("New Start Time",true);
        if(new_start_time != null){
            new_values.put("start_time", String.valueOf(new_start_time));
        }

        LocalTime new_end_time = InputGetter.getTime("New End Time",true);
        if(new_end_time != null){
            new_values.put("end_time", String.valueOf(new_end_time));
        }

        LocalTime check_start_time = new_start_time == null ?
                LocalTime.parse(matchedEvent.get(3)) : new_start_time;
        LocalTime check_end_time = new_end_time == null ?
                LocalTime.parse(matchedEvent.get(4)) : new_end_time;

        if(check_end_time.isBefore(check_start_time) || check_end_time.equals(check_start_time)){
            System.out.println("Error: End time must be after start time!");
            return;
        }

        Displayer.showMenu("Venues", venueNames);
        int option = InputGetter.getNumberOption(venueNames.size(), true);
        int new_venue_id = -1;
        if(option != -1){
            new_venue_id = VenueDAO.getVenueId(venueNames.get(option - 1));
            new_values.put("venue_id", String.valueOf(new_venue_id));
        }

        int event_id = Integer.parseInt(matchedEvent.get(0));

        LocalDate event_date = new_event_date == null ?
                LocalDate.parse(matchedEvent.get(2)) : new_event_date;

        LocalTime start_time = new_start_time == null ?
                LocalTime.parse(matchedEvent.get(3)) : new_start_time;

        int venue_id = new_venue_id == -1 ?
                Integer.parseInt(matchedEvent.get(5)) : new_venue_id;

        ArrayList<ArrayList<String>> conflictEvents =
                EventDAO.eventsInConflict(event_id, event_date, start_time, venue_id);
        if(conflictEvents!= null){
            System.out.println("The event can't be updated!");
            ArrayList<Double> columnWidths = new ArrayList<>(
                    Arrays.asList(0.30, 0.20, 0.15, 0.15, 0.20)
            );

            Displayer.displayTable("Overlapped Events", event_attributes, conflictEvents, columnWidths);
            return;
        }

        EventDAO.update(new_values, old_event_name);
    }

    private static void deleteEvent(){
        Displayer.displayHeader("Deleting Event");
        String event_name = InputGetter.getLine("Enter event name: ");
        System.out.println();

        boolean eventOngoing = EventDAO.checkStatus(event_name).equalsIgnoreCase("ongoing");
        if(eventOngoing){
            System.out.println("Ongoing events can't be deleted!");
            return;
        }

        System.out.println();
        EventDAO.delete(event_name);
    }
}
