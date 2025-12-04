package ems.dao;

import java.sql.*;
import java.util.ArrayList;

public class VenueDAO {
    private final Connection conn;

    public VenueDAO(Connection conn){
        this.conn = conn;
    }

    public ArrayList<String> getVenueNames() throws SQLException {
        String view_query = """
                SELECT venue_name
                FROM venues
                ORDER BY venue_name
            """;

        try(PreparedStatement get_stmt = this.conn.prepareStatement(view_query)){
            ArrayList<String> venueNames = new ArrayList<>();

            try(ResultSet venueSet = get_stmt.executeQuery()){
                if(!venueSet.next()) return null;

                do{
                    venueNames.add(venueSet.getString("venue_name"));
                }while(venueSet.next());
                return venueNames;
            }
        }
    }

    public int getVenueId(String venue_name) throws SQLException {
        String get_query = """
                 SELECT venue_id
                 FROM venues
                 WHERE venue_name ILIKE ?;
            """;

        try(PreparedStatement get_stmt = this.conn.prepareStatement(get_query)){
            get_stmt.setString(1, venue_name);
            try(ResultSet eventResult = get_stmt.executeQuery()){
                if(!eventResult.next()){
                    return -1;
                }
                return eventResult.getInt("venue_id");
            }
        }
    }
    public boolean empty_check() throws SQLException {
        String check_query = """
                    SELECT COUNT(*)
                    FROM venues
                """;

        try (PreparedStatement check_stmt = this.conn.prepareStatement(check_query)) {
            try (ResultSet view_rs = check_stmt.executeQuery()) {
                if (view_rs.next()) {
                    return view_rs.getInt("count") == 0;
                } else {
                    throw new SQLException("No result");
                }
            }
        }
    }
}
