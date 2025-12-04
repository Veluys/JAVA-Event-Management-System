package ems.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Event {
    private int event_id;
    private String event_name;
    private LocalDate event_date;
    private LocalTime start_time, end_time;
    private int venue_id;

    public Event(String event_name, LocalDate event_date, LocalTime start_time, LocalTime end_time, int venue_id){
        this.event_name = event_name;
        this.event_date = event_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.venue_id = venue_id;
    }

    public Event(int event_id, String event_name, LocalDate event_date, LocalTime start_time, LocalTime end_time, int venue_id){
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_date = event_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.venue_id = venue_id;
    }

    public Event(HashMap<String, Object> event_record, boolean forDisplay){
        if(!forDisplay){
            this.event_id = Integer.parseInt(event_record.get("event_id").toString());
        }
        this.event_name = event_record.get("event_name").toString();
        this.event_date = LocalDate.parse(event_record.get("event_date").toString(), DateTimeFormatter.ofPattern("MMM d, uuuu"));
        this.start_time = LocalTime.parse(event_record.get("start_time").toString(), DateTimeFormatter.ofPattern("hh:mm a"));
        this.end_time = LocalTime.parse(event_record.get("end_time").toString(), DateTimeFormatter.ofPattern("hh:mm a"));;
        this.venue_id = Integer.parseInt(event_record.get("venue_id").toString());

    }

    public void set_event_name(String event_name) {this.event_name = event_name;}
    public void set_event_date(LocalDate event_date) {this.event_date = event_date;}
    public void set_start_time(LocalTime start_time) {this.start_time = start_time;}
    public void set_end_time(LocalTime end_time) {this.end_time = end_time;}
    public void set_venue_id(int venue_id) {this.venue_id = venue_id;}

    public int get_event_id() {return event_id;}
    public String get_event_name() {return event_name;}
    public LocalDate get_event_date() {return event_date;}
    public LocalTime get_start_time() {return start_time;}
    public LocalTime get_end_time() {return end_time;}
    public int get_venue_id() {return venue_id;}
}
