package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EventDAO {
    final static Connection connection = DBConnection.getConnection();

    public static void insert(String values){
        String insertQuery = String.format("INSERT INTO events (event_name, date, start_time, end_time, venue) " +
                                            "VALUES (%s)", values);

        try{
            Statement insertStatement = connection.createStatement();

            if(insertStatement.executeUpdate(insertQuery) == 1){
                System.out.println("Insert operation successful");
            }else{
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Insert operation unsuccessful!");
        }
    }

    public static ArrayList<ArrayList<String>> show(){
        String selectQuery = "SELECT event_id, event_name, date, venue FROM events";
        ArrayList<ArrayList<String>> events = new ArrayList<>();

        try{
            Statement stmt = connection.createStatement();
            ResultSet eventsSet = stmt.executeQuery(selectQuery);

            if(!eventsSet.next()) return null;
            do{
                ArrayList<String> event = new ArrayList<>();

                event.add(eventsSet.getString("event_id"));
                event.add(eventsSet.getString("event_name"));
                event.add(eventsSet.getString("date"));
                event.add(eventsSet.getString("venue"));

                events.add(event);
            } while(eventsSet.next());

            return events;
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }

    public static int getNumRecords(){
        String countQuery = "SELECT COUNT(*) FROM events";

        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(countQuery);

            resultSet.next();
            return resultSet.getInt("count");
        }catch (SQLException e){
            return -1;
        }
    }

    public static ArrayList<String> search(String condition){
        String searchQuery = "SELECT * FROM events WHERE " + condition;
        ArrayList<String> event = new ArrayList<>();

        try{
            Statement eventStatement = connection.createStatement();
            ResultSet eventResult = eventStatement.executeQuery(searchQuery);

            if(!eventResult.next()) return null;

            event.add(eventResult.getString("event_id"));
            event.add(eventResult.getString("event_name"));
            event.add(eventResult.getString("date"));
            event.add(eventResult.getString("venue"));

            return event;
        }catch (SQLException e){
            System.out.println("Search operation unsuccessful!");
            return null;
        }
    }

    public static void update(ArrayList<String> changes, String condition){
        String updateQuery = "UPDATE events " +
                            " SET " + String.join(", ", changes) +
                            " WHERE " + condition;

        try{
            Statement updStatement = connection.createStatement();

            if(updStatement.executeUpdate(updateQuery) == 1){
                System.out.println("Update operation successful");
            }else{
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Update operation unsuccessful!");
        }
    }

    public static void delete(String condition){
        String deleteQuery = "DELETE FROM events WHERE " + condition;

        try{
            Statement delStatement = connection.createStatement();

            if(delStatement.executeUpdate(deleteQuery) == 1){
                System.out.println("Delete operation successful");
            }else{
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Delete operation unsuccessful!");
        }
    }
}