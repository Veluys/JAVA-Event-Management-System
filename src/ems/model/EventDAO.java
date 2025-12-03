package ems.model;

import ems.Main;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class EventDAO {
    final static Connection connection = Main.connection;

    public static void insert(final String event_name,
                              final LocalDate event_date,
                              final LocalTime start_time,
                              final LocalTime end_time,
                              final int venue_id)
    {
        String insert_query = """
                INSERT INTO events (event_name, event_date, start_time, end_time, venue_id)
                VALUES (?, ?, ?, ?, ?);
                """;

        try(PreparedStatement insert_stmt = connection.prepareStatement(insert_query)){
            insert_stmt.setString(1, event_name);
            insert_stmt.setObject(2, event_date);
            insert_stmt.setObject(3, start_time);
            insert_stmt.setObject(4, end_time);
            insert_stmt.setInt(5, venue_id);

            if(insert_stmt.executeUpdate() == 1){
                System.out.println("Insert operation successful");
            }else{
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Insert operation unsuccessful!");
        }
    }

    public static ArrayList<ArrayList<String>> showEvents(String event_status){
        String[] show_columns = {"event_name", "event_date", "start_time", "end_time", "venue_name"};
        String show_query = get_base_show_query() + """
            WHERE event_status ILIKE ?
            ORDER BY e.event_date
        """;

        if(event_status.equalsIgnoreCase("completed")){
            show_query += " DESC";
        }

        ArrayList<ArrayList<String>> events = new ArrayList<>();

        try(PreparedStatement show_stmt = connection.prepareStatement(show_query)){
            show_stmt.setString(1, event_status);
            try(ResultSet event_set = show_stmt.executeQuery()){
                if(!event_set.next()) return null;

                do{
                    ArrayList<String> event = new ArrayList<>();
                    for(String column : show_columns){
                        event.add(event_set.getString(column));
                    }
                    events.add(event);
                } while(event_set.next());
            }
            return events;
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }

    private static String get_base_show_query(){
        return """
            SELECT
                ed.event_name,
                ed.event_date,
                ed.start_time,
                ed.end_time,
                ed.venue_name
            FROM event_details AS ed
            INNER JOIN events AS e
                ON e.event_name = ed.event_name
        """;
    }

    public static ArrayList<ArrayList<String>> eventsInConflict(final int event_id, final LocalDate event_date, final LocalTime start_time, final int venue_id){
        if(emptyCheck()) return null;

        String[] show_columns = {"event_name", "event_date", "start_time", "end_time", "venue_name"};
        String check_query = get_base_show_query() + """
                    WHERE e.event_id != ?
                        AND e.venue_id = ?
                        AND e.event_date = ?
                        AND ? BETWEEN e.start_time AND e.end_time
                """;

        ArrayList<ArrayList<String>> events = new ArrayList<>();

        try{
            PreparedStatement check_stmt = connection.prepareStatement(check_query);
            check_stmt.setInt(1, event_id);
            check_stmt.setInt(2, venue_id);
            check_stmt.setObject(3, event_date);
            check_stmt.setObject(4, start_time);

            ResultSet eventsSet = check_stmt.executeQuery();

            if(!eventsSet.next()) return null;
            do{
                ArrayList<String> event = new ArrayList<>();

                for(String column : show_columns){
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

    public static boolean emptyCheck(){
        String count_query = """
                SELECT event_id
                FROM events
                LIMIT 1
                """;

        try(PreparedStatement check_stmt = connection.prepareStatement(count_query)) {
            try (ResultSet rs = check_stmt.executeQuery()) {
                return !rs.next();
            }
        }catch (Exception e){
            System.out.println("Checking for event records failed!");
            System.err.println(e.getMessage());
            return true;
        }
    }

    public static boolean eventExist(final String event_name){
        String search_query = "SELECT event_id FROM events WHERE event_name ILIKE ?";

        try(PreparedStatement search_stmt = connection.prepareStatement(search_query)){
            search_stmt.setString(1, event_name);
            try (ResultSet rs = search_stmt.executeQuery()) {
                return rs.next();
            }
        }catch (SQLException e){
            System.out.println("Search operation unsuccessful!");
            return false;
        }
    }

    public static ArrayList<String> searchRecord(final String event_name) {
        String[] search_columns = {"event_id", "event_name", "event_date", "start_time", "end_time", "venue_id", "venue_name"};
        String search_query = """
                SELECT
                	event_id,
                	event_name,
                	event_date,
                	start_time,
                	end_time,
                	e.venue_id,
                	venue_name
                FROM events AS e
                INNER JOIN venues AS v
                	ON e.venue_id = v.venue_id
                WHERE event_name ILIKE ?
                ORDER BY event_date DESC
                """;

        ArrayList<String> event = new ArrayList<>();

        try(PreparedStatement search_stmt = connection.prepareStatement(search_query)){
            search_stmt.setString(1, "%" + event_name + "%"); // Bind the wildcard search pattern
            try(ResultSet event_set = search_stmt.executeQuery()){
                if(!event_set.next()) return null;

                for(String column : search_columns){
                    event.add(event_set.getString(column));
                }

                if(event_set.next()){
                    System.out.println("Multiple events matched! Using the first one.");
                }
            }

            return event;
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful! Error: " + e.getMessage());
            return null;
        }
    }

    public static ArrayList<String> search(final String event_name) {
        String[] search_columns = {"event_name", "event_date", "start_time", "end_time", "venue_name"};
        String search_query = """
                SELECT
                    event_name,
                    event_date,
                    start_time,
                    end_time,
                    venue_name
                FROM event_details
                WHERE event_name ILIKE ?;
                """;

        try(PreparedStatement search_stmt = connection.prepareStatement(search_query)){
            search_stmt.setString(1, event_name);
            try(ResultSet eventsSet = search_stmt.executeQuery()){
                if (!eventsSet.next()) return null;
                ArrayList<String> event = new ArrayList<>();

                for (String column : search_columns) {
                    event.add(eventsSet.getString(column));
                }
                return event;
            }
        } catch (SQLException e) {
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }

    public static void update(LinkedHashMap<String, String> new_values, final String event_name){
        if(new_values.isEmpty()){
            return;
        }

        String update_query = "UPDATE events SET ";

        for(Map.Entry<String, String> new_value : new_values.entrySet()){
            if(new_value.getKey().equals("venue_id")){
                update_query += String.format("%s = %s", new_value.getKey(), new_value.getValue());
            }else{
                update_query += String.format("%s = '%s'", new_value.getKey(), new_value.getValue());
            }

            if(new_value.getKey().equals(new_values.lastEntry().getKey())){
                update_query += " ";
            }else {
                update_query += ", ";
            }
        }
        update_query += String.format("WHERE event_name ILIKE '%s'", event_name);

        try(PreparedStatement update_stmt = connection.prepareStatement(update_query)){
            if(update_stmt.executeUpdate() == 1){
                System.out.println("Update operation successful");
            }else{
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Update operation unsuccessful!");
        }
    }

    public static void delete(final String event_name){
        String delete_query = "DELETE FROM events WHERE event_name ILIKE ?";

        try(PreparedStatement delete_stmt = connection.prepareStatement(delete_query)){
            delete_stmt.setString(1, event_name);

            if(delete_stmt.executeUpdate() == 1){
                System.out.println("Delete operation successful");
            }else{
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Delete operation unsuccessful!");
        }
    }

    public static int getEventId(String event_name){
        String searchQuery = """
                SELECT event_id
                FROM events
                WHERE event_name ILIKE ?
            """;

        try(PreparedStatement get_stmt = connection.prepareStatement(searchQuery);){
            get_stmt.setString(1, event_name);

            try(ResultSet eventResult = get_stmt.executeQuery()){
                if(!eventResult.next()){
                    return -1;
                }
                return eventResult.getInt("event_id");
            }
        }catch (SQLException e){
            return -1;
        }
    }

    public static String checkStatus(String event_name){
        String search_query = """
                SELECT event_status
                FROM event_details
                WHERE event_name ILIKE ?
            """;

        try(PreparedStatement search_stmt = connection.prepareStatement(search_query)){
            search_stmt.setString(1, event_name);

            try(ResultSet eventResult = search_stmt.executeQuery()){
                if(!eventResult.next()){
                    return "not found";
                }
                return eventResult.getString("event_status");
            }
        }catch (SQLException e){
            return "not found";
        }
    }
}