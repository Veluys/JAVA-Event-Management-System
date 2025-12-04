package ems.model;

public class Registration {
    private int event_id;
    private String sr_code;
    private boolean attended;

    public Registration(int event_id, String sr_code){
        this.event_id = event_id;
        this.sr_code = sr_code;
    }

    public void set_event_id(int event_id) {this.event_id = event_id;}
    public void set_sr_code(String sr_code) {this.sr_code = sr_code;}
    public void set_attended(boolean attended) {this.attended = attended;}

    public int get_event_id() {return event_id;}
    public String get_sr_code() {return sr_code;}
    public boolean get_attended() {return attended;}
}
