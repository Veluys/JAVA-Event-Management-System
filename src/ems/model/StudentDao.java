package ems.model;

import java.sql.*;
import java.util.ArrayList;

public class StudentDao {
    final static Connection conn = DBConnection.getConnection();

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

    public static ArrayList<ArrayList<String>> getStudent(final String sr_code){
        if(!studentExist(sr_code)){
            System.out.printf("Student with an Sr Code of '%s' doesn't exist\n", sr_code);
            return null;
        }

        String[] get_columns = {"sr_code", "pshortname", "year_level", "full_name"};
        String get_query = String.format(
                """
                SELECT
                	sr_code,
                	pshortname,
                	year_level,
                	CONCAT(last_name, ', ', first_name) AS full_name
                FROM students AS s
                INNER JOIN programs AS p
                    ON sr_code = '%s'
                	AND s.program_id = p.program_id
                ORDER BY year_level, pshortname, full_name
                """, sr_code
        );

        ArrayList<ArrayList<String>> students = new ArrayList<>();

        try{
            PreparedStatement get_stmt = conn.prepareStatement(get_query);
            ResultSet student_set = get_stmt.executeQuery();

            if(!student_set.next()) return null;
            do{
                ArrayList<String> student = new ArrayList<>();

                for(String column : get_columns){
                    student.add(student_set.getString(column));
                }
                students.add(student);
            } while(student_set.next());

            return students;
        }catch (Exception e){
            System.out.println("Selecting students results in an error!");
            System.err.println(e.getMessage());
            return null;
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
