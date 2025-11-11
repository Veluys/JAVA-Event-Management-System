package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EventDAO {
    final Connection connection = DBConnection.getConnection();

    public void insert(String columns, String values){
        String insertQuery = String.format("INSERT INTO events (%s) VALUES (%s)", columns, values);

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

    public ArrayList<ArrayList<String>> show(String viewColumns){
        String selectQuery = String.format("SELECT %s FROM events", viewColumns);
        ArrayList<ArrayList<String>> events = new ArrayList<>();

        try{
            Statement stmt = connection.createStatement();
            ResultSet eventsSet = stmt.executeQuery(selectQuery);

            //centerAlignRow(viewColumns.split(","));
            while(eventsSet.next()){
                ArrayList<String> event = new ArrayList<>();

                event.add(eventsSet.getString("event_id"));
                event.add(eventsSet.getString("event_name"));
                event.add(eventsSet.getString("date"));
                event.add(eventsSet.getString("venue"));

                events.add(event);
            }
            return events;
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }

    public ArrayList<String> search(String condition){
        String searchQuery = "SELECT * FROM events WHERE " + condition;
        ArrayList<String> event = new ArrayList<>();

        try{
            Statement eventStatement = connection.createStatement();
            ResultSet eventResult = eventStatement.executeQuery(searchQuery);

            if(eventResult.next()){
                event.add(eventResult.getString("event_id"));
                event.add(eventResult.getString("event_name"));
                event.add(eventResult.getString("date"));
                event.add(eventResult.getString("venue"));
            }

            return event;
        }catch (SQLException e){
            System.out.println("Search operation unsuccessful!");
            return null;
        }
    }

    public void update(ArrayList<String> changes, int event_id, String condition){
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

    public void delete(String condition){
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
