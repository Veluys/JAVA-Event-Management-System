package ems.model;

import ems.Main;

import java.sql.*;
import java.util.ArrayList;

public class StudentDao {
    final static Connection conn = Main.connection;

    public static boolean emptyCheck(){
        String count_query = """
                SELECT sr_code
                FROM students
                LIMIT 1
                """;

        try{
            PreparedStatement check_stmt = conn.prepareStatement(count_query);
            return !check_stmt.execute();
        }catch (Exception e){
            System.out.println("Checking for student records failed!");
            System.err.println(e.getMessage());
            return true;
        }
    }

    public static boolean studentExist(final String sr_code){
        String count_query = String.format(
                """
                SELECT sr_code
                FROM students
                WHERE sr_code = '%s'
                """, sr_code
        );

        try{
            PreparedStatement check_stmt = conn.prepareStatement(count_query);
            return !check_stmt.execute();
        }catch (Exception e){
            System.out.println("Checking for student records failed!");
            System.err.println(e.getMessage());
            return true;
        }
    }
}
