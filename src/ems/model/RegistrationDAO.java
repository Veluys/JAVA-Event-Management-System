package ems.model;

import ems.Main;

import java.sql.*;
import java.util.ArrayList;

public class RegistrationDAO {
    final static Connection connection = Main.connection;

    public static void insert(final int event_id, final String sr_code){
        String insert_query = """
                INSERT INTO registration (event_id, sr_code)
                VALUES (?, ?)
                """;

        try{
            PreparedStatement insert_stmt = connection.prepareStatement(insert_query);
            insert_stmt.setInt(1, event_id);
            insert_stmt.setString(2, sr_code);

            if(insert_stmt.executeUpdate() != 1){
                throw new Exception();
            }
        }catch (Exception e){
            System.out.println("Insert operation unsuccessful!");
        }
    }

    public static ArrayList<ArrayList<String>> show(int event_id){
        String[] show_columns = {"sr_code", "pshortname", "year_level", "full_name"};
        String show_query = """
                SELECT
                	s.sr_code,
                	pshortname,
                	year_level,
                	CONCAT(last_name, ', ', first_name) AS full_name
                FROM students AS s
                INNER JOIN registration AS r
                    ON event_id = ?
                    AND s.sr_code = r.sr_code
                INNER JOIN programs AS p
                	ON s.program_id = p.program_id
                ORDER BY year_level, pshortname, full_name
                """;

        ArrayList<ArrayList<String>> students = new ArrayList<>();

        try{
            PreparedStatement show_stmt = connection.prepareStatement(show_query);
            show_stmt.setInt(1, event_id);
            ResultSet student_set = show_stmt.executeQuery();

            if(!student_set.next()) return null;
            do{
                ArrayList<String> student = new ArrayList<>();

                for(String column : show_columns){
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

    public static boolean emptyCheck(final int event_id){
        String count_query = """
                SELECT reg_id
                FROM registration
                WHERE event_id = ?
                LIMIT 1;
        """;

        try{
            PreparedStatement check_stmt = connection.prepareStatement(count_query);
            check_stmt.setInt(1, event_id);
            return !check_stmt.execute();
        }catch (Exception e){
            System.out.println("Checking for student records failed!");
            System.err.println(e.getMessage());
            return true;
        }
    }

    public static ArrayList<String> search(int event_id, final String sr_code){
        String[] show_columns = {"sr_code", "pshortname", "year_level", "full_name"};
        String show_query = """
                SELECT
                	s.sr_code,
                	pshortname,
                	year_level,
                	CONCAT(last_name, ', ', first_name) AS full_name
                FROM students AS s
                INNER JOIN registration AS r
                    ON s.sr_code = ?
                    AND event_id = ?
                    AND s.sr_code = r.sr_code
                INNER JOIN programs AS p
                	ON s.program_id = p.program_id
                ORDER BY year_level, pshortname, full_name
                """;

        try{
            PreparedStatement show_stmt = connection.prepareStatement(show_query);
            show_stmt.setString(1, sr_code);
            show_stmt.setInt(2, event_id);

            ResultSet student_set = show_stmt.executeQuery();

            if(!student_set.next()) return null;

            ArrayList<String> student = new ArrayList<>();

            for(String column : show_columns){
                student.add(student_set.getString(column));
            }

            return student;
        }catch (Exception e){
            System.out.println("Selecting students results in an error!");
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void delete(int event_id, String participant_id){
        String delete_query = """
                DELETE FROM registration
                WHERE event_id = ?
                    AND sr_code = ?;
                """;

        try{
            PreparedStatement delete_stmt = connection.prepareStatement(delete_query);
            delete_stmt.setInt(1, event_id);
            delete_stmt.setString(2, participant_id);

            if(delete_stmt.executeUpdate() != 1){
                throw new Exception();
            }
        }catch (Exception e){
            System.out.println("Delete operation unsuccessful!");
        }
    }
}