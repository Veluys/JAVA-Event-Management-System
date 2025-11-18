package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ParticipantDAO {
    final static Connection connection = DBConnection.getConnection();

    public static void insert(String values){
        String insertQuery = String.format(
                "INSERT INTO participants (participant_id, dept_id, last_name, first_name) " +
                "VALUES (%s)", values);

        try{
            Statement insertStatement = connection.createStatement();

            if(insertStatement.executeUpdate(insertQuery) != 1){
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Insert operation unsuccessful!");
        }
    }

    public static boolean participantsExist(){
        String countQuery = "SELECT COALESCE(MAX(event_id), 0) AS latest_event_id FROM events";

        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(countQuery);
            resultSet.next();
            return resultSet.getInt("latest_event_id")!=0;
        }catch (SQLException e){
            System.out.println("Search operation unsuccessful!");
            return true;
        }
    }

    public static ArrayList<ArrayList<String>> show(){
        String[] columns = {"participant_id", "dept_shortname", "last_name", "first_name"};
        String selectQuery = "SELECT p.participant_id, dept_shortname, last_name, first_name " +
                             "FROM participants AS p " +
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

    public static ArrayList<String> search(String condition){
        String[] columns = {"participant_id", "dept_shortname", "last_name", "first_name"};
        String searchQuery = "SELECT p.participant_id, dept_shortname, last_name, first_name " +
                             "FROM participants AS p " +
                             "INNER JOIN departments AS d " +
                             "  ON p.dept_id = d.dept_id " +
                             "WHERE " + condition;

        ArrayList<String> participant = new ArrayList<>();

        try{
            Statement searchStatement = connection.createStatement();
            ResultSet searchResult = searchStatement.executeQuery(searchQuery);

            if(!searchResult.next()) return null;

            for(String column : columns){
                participant.add(searchResult.getString(column));
            }
            return participant;
        }catch (SQLException e){
            System.out.println("Search operation unsuccessful!");
            return null;
        }
    }

    public static void update(ArrayList<String> changes, String condition){
        if(changes.isEmpty()){
            return;
        }

        String updateQuery = "UPDATE participants " +
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

    public static boolean participantExist(String condition){
        String searchQuery = "SELECT COUNT(*) FROM participants WHERE " + condition;

        try{
            Statement searchStatement = connection.createStatement();
            ResultSet searchResult = searchStatement.executeQuery(searchQuery);

            searchResult.next();
            return searchResult.getInt("count") != 0;
        }catch (SQLException e){
            System.out.println("Search operation unsuccessful!");
            return false;
        }
    }
}
