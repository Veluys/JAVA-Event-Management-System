package ems.model;

import ems.Main;

import java.sql.*;
import java.util.ArrayList;

public class AttendanceDAO {
    final static Connection connection = Main.connection;

    public static ArrayList<ArrayList<String>> showAttendees(int event_id){
        return show(event_id, true);
    }

    public static ArrayList<ArrayList<String>> showAbsentees(int event_id){
        return show(event_id, false);
    }

    private static ArrayList<ArrayList<String>> show(int event_id, boolean hasAttended){
        String[] show_columns = {"sr_code", "dept_shortname", "year_level", "full_name"};
        String show_query = """
                SELECT
                    s.sr_code,
                    dept_shortname,
                    year_level,
                    CONCAT(last_name, ', ', first_name) AS full_name
                FROM students AS s
                INNER JOIN registration AS r
                    ON s.sr_code = r.sr_code
                INNER JOIN departments AS d
                    ON d.dept_id = s.dept_id
                WHERE event_id = ?
                    AND attended = ?
                ORDER BY year_level, dept_shortname, full_name
                """;

        ArrayList<ArrayList<String>> students = new ArrayList<>();

        try(PreparedStatement show_stmt = connection.prepareStatement(show_query)){
            show_stmt.setInt(1, event_id);
            show_stmt.setBoolean(2, hasAttended);

            try(ResultSet student_set = show_stmt.executeQuery()){
                if(!student_set.next()) return null;
                do{
                    ArrayList<String> student = new ArrayList<>();

                    for(String column : show_columns){
                        student.add(student_set.getString(column));
                    }
                    students.add(student);
                } while(student_set.next());
                return students;
            }
        }catch (SQLException e){
            System.out.println("SELECT operation unsuccessful!");
            return null;
        }
    }

    public static void markPresent(int event_id, String participant_id){
        updateAttendance(event_id, participant_id, true);
    }

    public static void markAbsent(int event_id, String participant_id){
        updateAttendance(event_id, participant_id, false);
    }

    private static void updateAttendance(final int event_id, final String sr_code, final boolean present){
        String update_query = """
                UPDATE registration
                SET attended = ?
                WHERE event_id = ?
                    AND sr_code = ?
                """;

        try(PreparedStatement update_stmt = connection.prepareStatement(update_query)){
            update_stmt.setBoolean(1, present);
            update_stmt.setInt(2, event_id);
            update_stmt.setString(3, sr_code);

            if(update_stmt.executeUpdate() != 1){
                throw new Exception();
            }
        }catch (Exception e){
            System.out.println("Update operation unsuccessful!");
        }
    }

    public static boolean checkAttendanceStatus(final int event_id, String sr_code){
        String check_query = """
                SELECT attended
                FROM registration
                WHERE event_id = ?
                    AND sr_code = ?
            """;

        try(PreparedStatement check_stmt = connection.prepareStatement(check_query)){
            check_stmt.setInt(1, event_id);
            check_stmt.setString(2, sr_code);

            try(ResultSet attendance_set = check_stmt.executeQuery()){
                attendance_set.next();
                return attendance_set.getBoolean("attended");
            }
        } catch (Exception e) {
            System.out.println("Checking attendance unsuccessful!");
            return false;
        }
    }
}
