package ems.controller;

import ems.model.EventDAO;
import ems.model.VenueDAO;
import ems.view.Displayer;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class EventController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();
    private final String[] event_attributes = {"Event Name", "Date", "Start Time", "End Time", "Venue"};

    public void execute(){
        while(true){
            displayer.displayHeader("Events");
            ArrayList<String> operations = new ArrayList<>(
                    Arrays.asList("Add Events", "View Events", "Search Events", "Update Events", "Delete Events", "Exit")
            );

            displayer.displaySubheader("Main Menu");
            displayer.showMenu("Select an operation:", operations);
            int option = inputGetter.getNumberOption(operations.size());

            if(EventDAO.emptyCheck() && option > 1 && option != 6){
                System.out.println("There are no events yet!");
                return;
            }

            switch (option){
                case 1 -> addEvent();
                case 2 -> viewEvents();
                case 3 -> searchEvent();
                case 4 -> updateEvents();
                case 5 -> deleteEvent();
                case 6 -> {return;}
            }
            System.out.println();
        }
    }

    private void addEvent(){
        ArrayList<String> venueNames = VenueDAO.getVenueNames();
        if(venueNames == null){
            System.out.println("There are no available venues yet!");
            return;
        }

        displayer.displayHeader("Adding New Event");

        String eventName = inputGetter.getLine("Event Name: ");
        LocalDate date = inputGetter.getDate("Event Date");
        LocalTime start_time = inputGetter.getTime("Start Time");
        LocalTime end_time = inputGetter.getTime("End Time");

        if(end_time.isBefore(start_time) || end_time.equals(start_time)){
            System.out.println("Error: End time must be after start time!");
            return;
        }

        displayer.showMenu("Venues", venueNames);
        int option = inputGetter.getNumberOption(venueNames.size());
        int venue_id = VenueDAO.getVenueId(venueNames.get(option - 1));

        ArrayList<ArrayList<String>> conflictEvents = EventDAO.eventsInConflict(-1, date, start_time, venue_id);
        if(conflictEvents!= null){
            System.out.println("The event can't be added to the database!");
            displayer.displayHeader("Overlapping Events");
            displayer.centerAlignRow(new ArrayList<>(Arrays.asList(event_attributes)));
            for(ArrayList<String> event : conflictEvents){
                if(event == conflictEvents.getFirst()) System.out.println();
                displayer.centerAlignRow(event);
            }
            return;
        }

        System.out.println();
        EventDAO.insert(eventName, date, start_time, end_time, venue_id);

    }

    private void viewEvents(){
        ArrayList<ArrayList<String>> events = EventDAO.show();

        displayer.displayHeader("Viewing Events");
        displayer.displaySubheader("Events");
        displayer.centerAlignRow(new ArrayList<>(Arrays.asList(event_attributes)));
        for(ArrayList<String> event : events){
            if(event == events.getFirst()) System.out.println();
            displayer.centerAlignRow(event);
        }
    }

    private void searchEvent(){
        displayer.displayHeader("Searching Event");
        String eventName = inputGetter.getLine("Enter event name: ");
        System.out.println();

        ArrayList<String> columnHeaders = new ArrayList<>(
                Arrays.asList("Event Name", "Date", "Start Time", "End Time", "Venue")
        );

        ArrayList<String> matchedEvent = EventDAO.search(eventName);

        if(matchedEvent==null){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        displayer.displaySubheader("Matched Event");
        displayer.centerAlignRow(columnHeaders);
        displayer.centerAlignRow(matchedEvent);
    }

    private void updateEvents(){
        ArrayList<String> venueNames = VenueDAO.getVenueNames();
        if(venueNames == null){
            System.out.println("There are no available venues yet!");
            return;
        }

        displayer.displayHeader("Updating Event");
        String old_event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();

        ArrayList<String> matchedEvent = EventDAO.searchRecord(old_event_name);

        if(matchedEvent == null){
            System.out.println("Event name of '" + old_event_name + "' doesn't exist!");
            return;
        }

        LinkedHashMap<String, String> new_values = new LinkedHashMap<>();

        System.out.println("Simply press enter to not update that field.");

        String new_event_name = inputGetter.getLine("New Event Name: ",true);
        if(!new_event_name.isBlank()){
            if(EventDAO.eventExist(new_event_name)){
                System.out.println("Event name of '" + new_event_name + "' already exists!");
                return;
            }
            new_values.put("event_name", new_event_name);
        }

        LocalDate new_event_date = inputGetter.getDate("New Event Date",true);
        if(new_event_date != null){
            new_values.put("event_date", String.valueOf(new_event_date));
        }

        LocalTime new_start_time = inputGetter.getTime("New Start Time",true);
        if(new_start_time != null){
            new_values.put("start_time", String.valueOf(new_start_time));
        }

        LocalTime new_end_time = inputGetter.getTime("New End Time",true);
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

        displayer.showMenu("Venues", venueNames);
        int option = inputGetter.getNumberOption(venueNames.size(), true);
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
            displayer.displayHeader("Overlapping Events");
            displayer.centerAlignRow(new ArrayList<>(Arrays.asList(event_attributes)));
            for(ArrayList<String> event : conflictEvents){
                if(event == conflictEvents.getFirst()) System.out.println();
                displayer.centerAlignRow(event);
            }
            return;
        }

        System.out.println();
        EventDAO.update(new_values, old_event_name);
    }

    private void deleteEvent(){
        displayer.displayHeader("Deleting Event");
        String event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();
        EventDAO.delete(event_name);
    }
}
