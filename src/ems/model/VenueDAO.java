package ems.model;

import ems.Main;

import java.sql.*;
import java.util.ArrayList;

public class VenueDAO {
    final static Connection connection = Main.connection;

    public static ArrayList<String> getVenueNames(){
        String selectQuery = """
                SELECT venue_name
                FROM venues
                ORDER BY venue_name
            """;

        ArrayList<String> venueNames = new ArrayList<>();

        try(PreparedStatement get_stmt = connection.prepareStatement(selectQuery)){
            try(ResultSet venueSet = get_stmt.executeQuery(selectQuery)){
                if(!venueSet.next()) return null;

                do{
                    venueNames.add(venueSet.getString("venue_name"));
                } while(venueSet.next());

                return venueNames;
            }
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }

    public static int getVenueId(String venue_name){
        String searchQuery = """
                 SELECT venue_id
                 FROM venues
                 WHERE venue_name ILIKE ?;"
            """;

        try(PreparedStatement search_stmt = connection.prepareStatement(searchQuery)){
            search_stmt.setString(1, venue_name);
            try(ResultSet eventResult = search_stmt.executeQuery(searchQuery)){
                if(!eventResult.next()){
                    return -1;
                }
                return eventResult.getInt("venue_id");
            }
        }catch (SQLException e){
            return -1;
        }
    }
}
