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

            if(insertStatement.executeUpdate(insertQuery) == 1){
                System.out.println("Insert operation successful");
            }else{
                throw new SQLException();
            }
        }catch (SQLException e){
            System.out.println("Insert operation unsuccessful!");
        }
    }
}
