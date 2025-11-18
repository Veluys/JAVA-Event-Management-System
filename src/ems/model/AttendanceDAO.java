package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AttendanceDAO {
    final static Connection connection = DBConnection.getConnection();

    public static ArrayList<ArrayList<String>> showAttendees(int event_id){
        return show(event_id, true);
    }

    public static ArrayList<ArrayList<String>> showAbsentees(int event_id){
        return show(event_id, false);
    }

    private static ArrayList<ArrayList<String>> show(int event_id, boolean hasAttended){
        String[] columns = {"participant_id", "dept_shortname", "last_name", "first_name"};
        String selectQuery = "SELECT p.participant_id, dept_shortname, last_name, first_name " +
                             "FROM participants AS p " +
                             "INNER JOIN registration AS r " +
                             "  ON p.participant_id = r.participant_id " +
                             "  AND r.attended = " + hasAttended +
                             "  AND r.event_id = " + event_id + " " +
                             "INNER JOIN departments AS d " +
                             "  ON p.dept_id = d.dept_id";

        ArrayList<ArrayList<String>> events = new ArrayList<>();

        try{
            Statement stmt = connection.createStatement();
            ResultSet eventsSet = stmt.executeQuery(selectQuery);

            if(!eventsSet.next()) return null;
            do{
                ArrayList<String> event = new ArrayList<>();

                for(String column : columns){
                    event.add(eventsSet.getString(column));
                }
                events.add(event);
            } while(eventsSet.next());

            return events;
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }
}
