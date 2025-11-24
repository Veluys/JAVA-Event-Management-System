package ems.controller;

import ems.model.EventDAO;
import ems.model.VenueDAO;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;

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

        String eventName = String.format("'%s'", inputGetter.getLine("Event Name: "));
        String date = String.format("'%s'", inputGetter.getDate("Event Date"));
        String start_time = String.format("'%s'", inputGetter.getTime("Start Time"));
        String end_time = String.format("'%s'", inputGetter.getTime("End Time"));

        displayer.showMenu("Venues", venueNames);
        int option = inputGetter.getNumberOption(venueNames.size());
        int venue_id = VenueDAO.getVenueId(venueNames.get(option - 1));

        ArrayList<String> eventDetails = new ArrayList<>(
                Arrays.asList(eventName, date, start_time, end_time, String.format("'%s'", venue_id))
        );

        System.out.println();
        EventDAO.insert(String.join(",", eventDetails));

    }

    private void viewEvents(){
        ArrayList<ArrayList<String>> events = EventDAO.show();

        if(EventDAO.getLatestEventId() == 0 || events==null){
            System.out.println("There are no events yet!");
            return;
        }

        displayer.centerAlignRow(new ArrayList<>(Arrays.asList(event_attributes)));
        for(ArrayList<String> event : events){
            if(event == events.getFirst()) System.out.println();
            displayer.centerAlignRow(event);
        }
    }

    private void searchEvent(){
        if(EventDAO.getLatestEventId() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        String eventName = inputGetter.getLine("Enter event name: ");
        System.out.println();

        String condition = "event_name = '" + eventName + "'";
        String[] tableColumns = {"Event ID", "Event Name", "Date", "Start Time", "End Time", "Venue"};

        ArrayList<String> matchedEvent = EventDAO.search(condition);

        if(matchedEvent==null){
            System.out.println("There are no events that matched the given event name!");
            return;
        }

        for(int i = 0; i < matchedEvent.size(); i++){
            displayer.rightAlignRecord(new ArrayList<>(Arrays.asList(tableColumns[i], matchedEvent.get(i))));
        }
    }

    private void updateEvents(){
        if(EventDAO.getLatestEventId() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        ArrayList<String> venueNames = VenueDAO.getVenueNames();
        if(venueNames == null){
            System.out.println("There are no available venues yet!");
            return;
        }

        String event_name = inputGetter.getLine("Enter event name: ");
        System.out.println();

        if(!EventDAO.eventExist("event_name = '" + event_name + "'")){
            System.out.println("Event name of '" + event_name + "' doesn't exist!");
            return;
        }

        String condition = "event_name = '" + event_name + "'";

        ArrayList<String> changes = new ArrayList<>();

        System.out.println("Simply press enter to not update that field.");

        String eventName = inputGetter.getLine("New Event Name: ",true);
        if(!eventName.isBlank()){
            if(EventDAO.eventExist("event_name = '" + event_name + "'")){
                System.out.println("Event name of '" + event_name + "' already exists!");
                return;
            }
            changes.add("event_name = '" + eventName + "'");
        }

        String date = inputGetter.getDate("New Event Date: ",true);
        if(!date.isBlank()){
            changes.add("event_date = '" + date + "'");
        }

        String start_time = inputGetter.getTime("New Start Time: ",true);
        if(!start_time.isBlank()){
            changes.add("start_time = '" + start_time + "'");
        }

        String end_time = inputGetter.getTime("New End Time: ",true);
        if(!end_time.isBlank()){
            changes.add("end_time = '" + end_time + "'");
        }

        displayer.showMenu("Venues", venueNames);
        int option = inputGetter.getNumberOption(venueNames.size(), true);
        int venue_id = VenueDAO.getVenueId(venueNames.get(option - 1));

        if(venue_id != -1){
            changes.add("venue_id = '" + venue_id + "'");
        }

        System.out.println();
        EventDAO.update(changes, condition);
    }


    private void deleteEvent(){
        if(EventDAO.getLatestEventId() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        String event_name = inputGetter.getLine("Enter event name: ");
        String condition = "event_name = '" + event_name + "'";

        System.out.println();
        EventDAO.delete(condition);
    }
}
