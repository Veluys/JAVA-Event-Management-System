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

    private void addEvent(){
        LinkedHashMap<String, String> event = new LinkedHashMap<>();

        for(String eventDetailName : event_attributes){
            if(eventDetailName.equals("Date")){
                displayer.showPrompt(eventDetailName + "(Ex: January 1, 2001): ");
                event.put("Date", String.format("'%s'", inputGetter.getDate()));
            }else if(eventDetailName.contains("Time")){
                displayer.showPrompt(eventDetailName + "(Ex. 9:00 a.m.): ");
                event.put(eventDetailName, String.format("'%s'", inputGetter.getTime()));
            }else{
                displayer.showPrompt(eventDetailName + ": ");
                event.put(eventDetailName, String.format("'%s'", inputGetter.getLine()));
            }
        }
        String eventDetails = String.join(",", event.values());
        EventDAO.insert(eventDetails);
    }

    private void viewEvents(){
        System.out.println();
        ArrayList<ArrayList<String>> events = EventDAO.show();

        if(events==null) return;

        displayer.centerAlignRow(new ArrayList<>(Arrays.asList("event_id", "event_name", "date", "venue")));
        for(ArrayList<String> event : events){
            displayer.centerAlignRow(event);
        }
        System.out.println();
    }
}
