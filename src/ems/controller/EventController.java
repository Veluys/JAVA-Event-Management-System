package ems.controller;

import ems.model.EventDAO;
import ems.view.Displayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class EventController {
    Displayer displayer = new Displayer();
    InputGetter inputGetter = new InputGetter();
    private final String[] event_attributes = {"Event Name", "Date", "Start Time", "End Time", "Venue"};

    public void execute(){
        EventMenu();
        int option;

        do{
            displayer.numberedMenuPrompt();
            option = inputGetter.getPositiveInt(6);
        }while(option == -1);

        System.out.println();
        switch (option){
            case 1 -> addEvent();
            case 2 -> viewEvents();
            case 3 -> searchEvent();
            case 4 -> updateEvents();
            case 5 -> deleteEvent();
            case 6 -> {
                return;
            }
        }
        System.out.println();
    }

    private void EventMenu(){
        displayer.displayHeader("Events");
        String[] operations = {"Add Events", "View Events", "Search Events", "Update Events", "Delete Events", "Exit"};
        displayer.showMenu("Select an operation:", operations);
    }

    private void addEvent(){
        LinkedHashMap<String, String> event = new LinkedHashMap<>();

        for(String eventDetailName : event_attributes){
            if(eventDetailName.equals("Date")){
                displayer.showPrompt(eventDetailName + " (Ex: January 1, 2001): ");
                event.put("Date", String.format("'%s'", inputGetter.getDate()));
            }else if(eventDetailName.contains("Time")){
                displayer.showPrompt(eventDetailName + " (Ex. 9:00 a.m.): ");
                event.put(eventDetailName, String.format("'%s'", inputGetter.getTime()));
            }else{
                displayer.showPrompt(eventDetailName + ": ");
                event.put(eventDetailName, String.format("'%s'", inputGetter.getLine()));
            }
        }
        String eventDetails = String.join(",", event.values());
        System.out.println();
        EventDAO.insert(eventDetails);
    }

    private void viewEvents(){
        if(EventDAO.getNumRecords() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        ArrayList<ArrayList<String>> events = EventDAO.show();

        displayer.centerAlignRow(new ArrayList<>(Arrays.asList("event_id", "event_name", "date", "venue")));
        for(ArrayList<String> event : events){
            if(event == events.getFirst()) System.out.println();
            displayer.centerAlignRow(event);
        }
    }

    private void searchEvent(){
        if(EventDAO.getNumRecords() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        displayer.showPrompt("Enter event name: ");
        String eventName = inputGetter.getLine();
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
        if(EventDAO.getNumRecords() == 0){
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
        if(EventDAO.getNumRecords() == 0){
            System.out.println("There are no events yet!");
            return;
        }

        displayer.showPrompt("Enter event name. : ");
        String event_name = inputGetter.getLine();

        String condition = "event_name = '" + event_name + "'";

        System.out.println();
        EventDAO.delete(condition);
    }
}
