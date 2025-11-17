package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ParticipantDAO {
    final static Connection connection = DBConnection.getConnection();

    public static void insert(String values){
        String insertQuery = String.format("INSERT INTO participants (dept_id, last_name, first_name) " +
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

    public static int getLastParticipantId(){
        String countQuery = "SELECT COALESCE(MAX(participant_id), 0) AS latest_participant_id FROM participants";

        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(countQuery);

            resultSet.next();
            return resultSet.getInt("latest_participant_id");
        }catch (SQLException e){
            return -1;
        }
    }
}
