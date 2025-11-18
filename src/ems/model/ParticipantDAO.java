package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
