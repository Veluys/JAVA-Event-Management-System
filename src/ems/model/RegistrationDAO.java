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

            if(insertStatement.executeUpdate(insertQuery) != 1){
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Insert operation unsuccessful!");
        }
    }

    public static ArrayList<ArrayList<String>> show(int event_id){
        String[] columns = {"participant_id", "dept_shortname", "last_name", "first_name", "attended"};
        String selectQuery = "SELECT p.participant_id, dept_shortname, last_name, first_name, " +
                             "CASE " +
                                "WHEN attended IS NULL THEN 'N/A' " +
                                "WHEN attended = TRUE THEN 'Y' " +
                                "WHEN attended = FALSE THEN 'N' " +
                             "END AS attended " +
                             "FROM participants AS p " +
                             "INNER JOIN registration AS r " +
                             "  ON p.participant_id = r.participant_id " +
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

    public static boolean isEmpty(final int event_id){
        String checkQuery = "SELECT COALESCE(MAX(reg_id), 0) AS count FROM registration " +
                            "WHERE event_id = " + event_id;

        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(checkQuery);

            resultSet.next();
            return resultSet.getInt("count") == 0;
        }catch (SQLException e){
            return false;
        }
    }

    public static ArrayList<String> search(int event_id, String participant_id){
        String[] columns = {"participant_id", "dept_shortname", "last_name", "first_name", "attended"};
        String searchQuery = "SELECT p.participant_id, dept_shortname, last_name, first_name, " +
                             "CASE " +
                                "WHEN attended IS NULL THEN 'N/A' " +
                                "WHEN attended = TRUE THEN 'Y' " +
                                "WHEN attended = FALSE THEN 'N' " +
                             "END AS attended " +
                             "FROM participants AS p " +
                             "INNER JOIN registration AS r " +
                             "  ON p.participant_id = r.participant_id " +
                             "  AND r.event_id = " + event_id + " " +
                             "  AND r.participant_id = '" + participant_id + "' " +
                             "INNER JOIN departments AS d " +
                             "  ON p.dept_id = d.dept_id ";

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

    public static void delete(int event_id, String participant_id){
        String deleteQuery = "DELETE FROM registration " +
                             "WHERE event_id = " + event_id +
                                " AND participant_id = '" + participant_id + "'";

        try{
            Statement delStatement = connection.createStatement();

            if(delStatement.executeUpdate(deleteQuery) != 1){
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Delete operation unsuccessful!");
        }
    }

}
