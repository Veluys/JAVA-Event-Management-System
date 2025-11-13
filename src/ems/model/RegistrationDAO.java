package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RegistrationDAO {
    final static Connection connection = DBConnection.getConnection();

    public static void insert(String values){
        String insertQuery = String.format("INSERT INTO registration (event_id, participant_id) " +
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
        String[] columns = {"registration_id", "participant_id", "last_name", "first_name", "has_attended"};
        String selectQuery = "SELECT r.registration_id, r.participant_id, p.last_name, p.first_name, r.has_attended " +
                             "FROM registration AS r " +
                             "LEFT JOIN participants AS p " +
                                "ON r.participant_id = p.participant_i " +
                             "ORDER BY registration_id DESC;";

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

    public static int getNumRecords(){
        String countQuery = "SELECT COUNT(*) FROM registration";

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
        String[] columns = {"registration_id", "participant_id", "last_name", "first_name", "has_attended"};
        String searchQuery = "SELECT r.registration_id, r.participant_id, p.last_name, p.first_name, r.has_attended" +
                             "FROM registration AS r " +
                             "LEFT JOIN participants AS p " +
                                "ON r.participant_id = p.participant_i " +
                             "WHERE " + condition;
        ArrayList<String> event = new ArrayList<>();

        try{
            Statement eventStatement = connection.createStatement();
            ResultSet eventResult = eventStatement.executeQuery(searchQuery);

            if(!eventResult.next()) return null;

            for(String column : columns){
                event.add(eventResult.getString(column));
            }
            return event;
        }catch (SQLException e){
            System.out.println("Search operation unsuccessful!");
            return null;
        }
    }

}
