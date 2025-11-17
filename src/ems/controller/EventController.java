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
        displayer.displayHeader("Events");
        ArrayList<String> operations = new ArrayList<>(
                Arrays.asList("Add Events", "View Events", "Search Events", "Update Events", "Delete Events", "Exit")
        );

        displayer.showMenu("Select an operation:", operations);
        int option = inputGetter.getNumberOption(operations.size());

        switch (option){
            case 1 -> addEvent();
            case 2 -> viewEvents();
//            case 3 -> searchEvent();
//            case 4 -> updateEvents();
//            case 5 -> deleteEvent();
            case 6 -> {
                return;
            }
        }
        System.out.println();
    }

    private void addEvent(){
        String eventName = String.format("'%s'", inputGetter.getLine("Event Name: "));
        String date = String.format("'%s'", inputGetter.getDate("Event Date"));
        String start_time = String.format("'%s'", inputGetter.getTime("Start Time"));
        String end_time = String.format("'%s'", inputGetter.getTime("End Time"));

        ArrayList<String> venueNames = VenueDAO.getVenueNames();
        int venue_id;

        if(venueNames != null){
            displayer.showMenu("Venues", venueNames);
            int option = inputGetter.getNumberOption(venueNames.size());
            venue_id = VenueDAO.getVenueId(venueNames.get(option - 1));
        }else{
            String venue = inputGetter.getLine("Venue: ");
            VenueDAO.insert(venue);
            venue_id = VenueDAO.getLatestVenueId();
        }

        ArrayList<String> eventDetails = new ArrayList<>(
                Arrays.asList(eventName, date, start_time, end_time, String.format("'%s'", venue_id))
        );

        System.out.println();
        EventDAO.insert(String.join(",", eventDetails));

    }

    private void viewEvents(){
        if(EventDAO.getLatestEventId() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        ArrayList<ArrayList<String>> events = EventDAO.show();

        displayer.centerAlignRow(new ArrayList<>(Arrays.asList(event_attributes)));
        for(ArrayList<String> event : events){
            if(event == events.getFirst()) System.out.println();
            displayer.centerAlignRow(event);
        }
    }
    /*
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

        displayer.showPrompt("Enter event name: ");
        String event_name = inputGetter.getLine();
        System.out.println();

        String condition = "event_name = '" + event_name + "'";


        ArrayList<String> changes = new ArrayList<>();

        System.out.println("Simply press enter to not update that field.");

        displayer.showPrompt("New Event Name: ");
        String eventName = inputGetter.getLine(true);
        if(!eventName.isBlank()){
            changes.add("event_name = '" + eventName + "'");
        }

        displayer.showPrompt("New Event Date: ");
        String date = inputGetter.getDate(true);
        if(!date.isBlank()){
            changes.add("date = '" + date + "'");
        }

        displayer.showPrompt("New Start Time: ");
        String start_time = inputGetter.getTime(true);
        if(!start_time.isBlank()){
            changes.add("start_time = '" + start_time + "'");
        }

        displayer.showPrompt("New End Time: ");
        String end_time = inputGetter.getTime(true);
        if(!end_time.isBlank()){
            changes.add("end_time = '" + end_time + "'");
        }

        displayer.showPrompt("New Event Venue: ");
        String venue = inputGetter.getLine(true);
        if(!venue.isBlank()){
            changes.add("venue = '" + venue + "'");
        }

        System.out.println();
        EventDAO.update(changes, condition);
    }

    private void deleteEvent(){
        if(EventDAO.getLatestEventId() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        displayer.showPrompt("Enter event name. : ");
        String event_name = inputGetter.getLine();

        String condition = "event_name = '" + event_name + "'";

        System.out.println();
        EventDAO.delete(condition);
    }*/
}
