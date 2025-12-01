package ems.model;

import ems.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VenueDAO {
    final static Connection connection = Main.connection;

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
