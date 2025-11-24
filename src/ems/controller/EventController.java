package ems.controller;

import ems.model.EventDAO;
import ems.model.VenueDAO;
import ems.view.Displayer;

import java.sql.Date;
import java.sql.Time;
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

        String eventName = inputGetter.getLine("Event Name: ");
        Date date = inputGetter.getDate("Event Date");
        Time start_time = inputGetter.getTime("Start Time");
        Time end_time = inputGetter.getTime("End Time");

        displayer.showMenu("Venues", venueNames);
        int option = inputGetter.getNumberOption(venueNames.size());
        int venue_id = VenueDAO.getVenueId(venueNames.get(option - 1));

        System.out.println();
        EventDAO.insert(eventName, date, start_time, end_time, venue_id);

    }

    private void viewEvents(){
        ArrayList<ArrayList<String>> events = EventDAO.show();

        displayer.centerAlignRow(new ArrayList<>(Arrays.asList(event_attributes)));
        for(ArrayList<String> event : events){
            if(event == events.getFirst()) System.out.println();
            displayer.centerAlignRow(event);
        }
    }

    private void searchEvent(){
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

        displayer.centerAlignRow(columnHeaders);
        displayer.centerAlignRow(matchedEvent);
    }

    private void updateEvents(){
        ArrayList<String> venueNames = VenueDAO.getVenueNames();
        if(venueNames == null){
            System.out.println("There are no available venues yet!");
            return;
        }

        String old_event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();

        if(!EventDAO.eventExist(old_event_name)){
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

        Date date = inputGetter.getDate("New Event Date",true);
        if(date != null){
            new_values.put("event_date", String.valueOf(date));
        }

        Time start_time = inputGetter.getTime("New Start Time",true);
        if(start_time != null){
            new_values.put("start_time", String.valueOf(start_time));
        }

        Time end_time = inputGetter.getTime("New End Time",true);
        if(end_time != null){
            new_values.put("end_time", String.valueOf(end_time));
        }

        displayer.showMenu("Venues", venueNames);
        int option = inputGetter.getNumberOption(venueNames.size(), true);

        if(option != -1){
            int venue_id = VenueDAO.getVenueId(venueNames.get(option - 1));
            new_values.put("venue_id", String.valueOf(venue_id));
        }

        System.out.println();
        EventDAO.update(new_values, old_event_name);
    }

    private void deleteEvent(){
        String event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();
        EventDAO.delete(event_name);
    }
}
