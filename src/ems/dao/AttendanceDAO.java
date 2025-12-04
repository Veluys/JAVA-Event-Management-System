package ems.dao;

import ems.model.Registration;

import java.sql.*;
import java.util.ArrayList;

public class AttendanceDAO {
    private final Connection conn;

    public AttendanceDAO(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<ArrayList<String>> view_attendance(int event_id, boolean attended) throws SQLException {
        String view_query = """
                SELECT
                    sr_code,
                    dept_shortname,
                    year_level,
                    full_name
                FROM participant_details
                WHERE event_id = ?
                    AND attended ILIKE ?
            """;

        try (PreparedStatement view_stmt = this.conn.prepareStatement(view_query)) {
            view_stmt.setInt(1, event_id);
            if(attended){
                view_stmt.setString(2, "Yes");
            }else{
                view_stmt.setString(2, "No");
            }

            ArrayList<ArrayList<String>> participants = new ArrayList<>();
            try (ResultSet view_rs = view_stmt.executeQuery()) {
                if (!view_rs.next()) return null;

                ArrayList<String> participant = new ArrayList<>();
                do {
                    participant.add(view_rs.getString("sr_code"));
                    participant.add(view_rs.getString("dept_shortname"));
                    participant.add(view_rs.getString("year_level"));
                    participant.add(view_rs.getString("full_name"));

                    participants.add(participant);
                } while (view_rs.next());
                return participants;
            }
        }
    }

    public boolean is_attendee(Registration reg_record) throws SQLException {
        String check_query = """
                SELECT attended
                FROM registration
                WHERE event_id = ?
                    AND sr_code = ?
            """;

        try (PreparedStatement check_stmt = this.conn.prepareStatement(check_query)) {
            check_stmt.setInt(1, reg_record.get_event_id());
            check_stmt.setString(2, reg_record.get_sr_code());

            try (ResultSet view_rs = check_stmt.executeQuery()) {
                if (view_rs.next()) {
                    return view_rs.getString("attended").equalsIgnoreCase("yes");
                } else {
                    throw new SQLException("No result");
                }
            }
        }
    }

    public boolean update_attendance(Registration reg_record, boolean setPresent) throws SQLException {
        String update_query = """
                    UPDATE registration
                    SET attended = ?
                    WHERE event_id = ?
                        AND sr_code = ?
                """;

        try (PreparedStatement upd_stmt = this.conn.prepareStatement(update_query)) {
            upd_stmt.setBoolean(1, setPresent);
            upd_stmt.setInt(2, reg_record.get_event_id());
            upd_stmt.setString(3, reg_record.get_sr_code());
            upd_stmt.executeUpdate();
            return true;
        }
    }
}