package ems.dao;

import ems.model.Registration;

import java.sql.*;
import java.util.ArrayList;

public class RegistrationDAO {
    private final Connection conn;

    public RegistrationDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insert_participant(Registration reg_record) throws SQLException {
        String insert_query = """
                INSERT INTO registration (event_id, sr_code)
                VALUES (?, ?)
        """;

        try(PreparedStatement insert_stmt = this.conn.prepareStatement(insert_query)){
            insert_stmt.setInt(1, reg_record.get_event_id());
            insert_stmt.setString(2, reg_record.get_sr_code());
            insert_stmt.executeUpdate();
            return true;
        }
    }

    private String get_base_view_query(){
        return """
                SELECT
                    sr_code,
                    dept_shortname,
                    year_level,
                    full_name
                FROM participant_details
                WHERE event_id = ?
            """;
    }

    public ArrayList<ArrayList<String>> view_registered(int event_id) throws SQLException {
        String view_query = get_base_view_query() + "\nORDER BY year_level, dept_shortname, full_name;";

        try(PreparedStatement show_stmt = this.conn.prepareStatement(view_query)){
            show_stmt.setInt(1, event_id);

            ArrayList<ArrayList<String>> students = new ArrayList<>();
            try(ResultSet student_set = show_stmt.executeQuery()){
                if(!student_set.next()) return null;
                do{
                    ArrayList<String> student = new ArrayList<>();
                    student.add(student_set.getString("sr_code"));
                    student.add(student_set.getString("dept_shortname"));
                    student.add(student_set.getString("year_level"));
                    student.add(student_set.getString("full_name"));

                    students.add(student);
                } while(student_set.next());
                return students;
            }
        }
    }

    public ArrayList<ArrayList<String>> search_registered(Registration reg_record) throws SQLException {
        String view_query = get_base_view_query() + "\n AND sr_code ILIKE ?";

        try(PreparedStatement search_stmt = this.conn.prepareStatement(view_query)){
            search_stmt.setInt(1, reg_record.get_event_id());
            search_stmt.setString(2, reg_record.get_sr_code());

            ArrayList<ArrayList<String>> students = new ArrayList<>();
            try (ResultSet student_set = search_stmt.executeQuery()) {
                if (student_set.next()) {
                    ArrayList<String> student = new ArrayList<>();
                    student.add(student_set.getString("sr_code"));
                    student.add(student_set.getString("dept_shortname"));
                    student.add(student_set.getString("year_level"));
                    student.add(student_set.getString("full_name"));

                    students.add(student);
                    return students;
                }
            }
            return null;
        }
    }

    public boolean delete_registration(Registration reg_record) throws SQLException {
        String delete_query = """
                DELETE FROM registration
                WHERE event_id = ?
                    AND sr_code = ?;
                """;

        try(PreparedStatement delete_stmt = this.conn.prepareStatement(delete_query)){
            delete_stmt.setInt(1, reg_record.get_event_id());
            delete_stmt.setString(2, reg_record.get_sr_code());

            delete_stmt.executeUpdate();
            return true;
        }
    }

    public boolean empty_check(int event_id) throws SQLException {
        String check_query = """
                    SELECT COUNT(*)
                    FROM registration
                    WHERE event_id = ?
                """;

        try (PreparedStatement check_stmt = this.conn.prepareStatement(check_query)) {
            check_stmt.setInt(1, event_id);
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
