package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VenueDAO {
    final static Connection connection = DBConnection.getConnection();

    public static void insert(String venue){
        String insertQuery = String.format("INSERT INTO venues (venue_name) VALUES (%s)", venue);

        try{
            Statement insertStatement = connection.createStatement();

            if(insertStatement.executeUpdate(insertQuery) != 1){
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Insert operation unsuccessful!");
        }
    }

    public static ArrayList<String> getVenueNames(){
        String selectQuery = "SELECT venue_name " +
                "FROM venues " +
                "ORDER BY venue_name";

        ArrayList<String> venueNames = new ArrayList<>();

        try{
            Statement stmt = connection.createStatement();
            ResultSet venueSet = stmt.executeQuery(selectQuery);

            if(!venueSet.next()) return null;

            do{
                venueNames.add(venueSet.getString("venue_name"));
            } while(venueSet.next());

            return venueNames;
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }

    public static int getLatestVenueId(){
        String countQuery = "SELECT COALESCE(MAX(venue_id), 0) AS latest_venue_id FROM venues";

        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(countQuery);

            resultSet.next();
            return resultSet.getInt("latest_venue_id");
        }catch (SQLException e){
            return -1;
        }
    }

    public static int getVenueId(String venue_name){
        String searchQuery = String.format("SELECT venue_id FROM venues WHERE venue_name = '%s'", venue_name);

        try{
            Statement eventStatement = connection.createStatement();
            ResultSet eventResult = eventStatement.executeQuery(searchQuery);

            if(!eventResult.next()){
                return -1;
            }

            return eventResult.getInt("venue_id");
        }catch (SQLException e){
            return -1;
        }
    }
}
