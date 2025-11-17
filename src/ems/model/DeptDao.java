package ems.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DeptDao {
    final static Connection connection = DBConnection.getConnection();

    public static ArrayList<String> getDeptShortNames(){
        String selectQuery = "SELECT dept_shortname " +
                "FROM departments " +
                "ORDER BY dept_id";

        ArrayList<String> venueNames = new ArrayList<>();

        try{
            Statement stmt = connection.createStatement();
            ResultSet venueSet = stmt.executeQuery(selectQuery);

            if(!venueSet.next()) return null;

            do{
                venueNames.add(venueSet.getString("dept_shortname"));
            } while(venueSet.next());

            return venueNames;
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }

    public static int getLatestDeptId(){
        String countQuery = "SELECT COALESCE(MAX(dept_id), 0) AS latest_dept_id FROM departments";

        try{
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(countQuery);

            resultSet.next();
            return resultSet.getInt("latest_dept_id");
        }catch (SQLException e){
            return -1;
        }
    }

    public static int getDeptId(String dept_shortname){
        String searchQuery = String.format("SELECT dept_id FROM departments WHERE dept_shortname = '%s'", dept_shortname);

        try{
            Statement eventStatement = connection.createStatement();
            ResultSet eventResult = eventStatement.executeQuery(searchQuery);

            if(!eventResult.next()){
                return -1;
            }

            return eventResult.getInt("dept_id");
        }catch (SQLException e){
            return -1;
        }
    }
}
