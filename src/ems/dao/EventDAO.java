package ems.model;

import ;

import java.sql.*;
import java.util.ArrayList;

public class EventDAO {
    private Connection conn;

    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insert_event(Event event) throws SQLException {
        String insert_query = """
                        INSERT INTO events (event_name, event_date, start_time, end_time, venue_id)
                                VALUES (?, ?, ?, ?, ?);
                """;

        try (PreparedStatement insert_stmt = this.conn.prepareStatement(insert_query)) {
            insert_stmt.setString(1, event.get_event_name());
            insert_stmt.setObject(2, event.get_event_date());
            insert_stmt.setObject(3, event.get_start_time());
            insert_stmt.setObject(4, event.get_end_time());
            insert_stmt.setInt(5, event.get_venue_id());

            insert_stmt.executeUpdate();
            return true;
        }
    }

    private String _get_base_view_query() {
        return """
                    SELECT
                        ed.event_name,
                        ed.event_date,
                        ed.start_time,
                        ed.end_time,
                        ed.venue_name
                    FROM event_details AS ed
                    INNER JOIN events AS e
                        ON ed.event_name = e.event_name
                """;
    }

    public ArrayList<ArrayList<String>> view_events(String event_status) throws SQLException {
        String view_query = _get_base_view_query() + """
                    WHERE event_status ILIKE ?
                """;

        try (PreparedStatement view_stmt = this.conn.prepareStatement(view_query)) {
            view_stmt.setString(1, event_status);

            ArrayList<ArrayList<String>> events = new ArrayList<>();
            try (ResultSet view_rs = view_stmt.executeQuery()) {
                if (!view_rs.next()) return null;

                ArrayList<String> event = new ArrayList<>();
                do {
                    event.add(view_rs.getString("event_name"));
                    event.add(view_rs.getString("event_date"));
                    event.add(view_rs.getString("start_time"));
                    event.add(view_rs.getString("end_time"));
                    event.add(view_rs.getString("venue_name"));

                    events.add(event);

                } while (view_rs.next());
                return events;
            }
        }
    }

    public ArrayList<ArrayList<String>> display_search(String searched_event_name) throws SQLException {
        String view_query = _get_base_view_query() + """
                    WHERE ed.event_name ILIKE ?
                """;

        try (PreparedStatement view_stmt = this.conn.prepareStatement(view_query)) {
            view_stmt.setString(1, searched_event_name);

            ArrayList<ArrayList<String>> events = new ArrayList<>();
            try (ResultSet view_rs = view_stmt.executeQuery()) {
                if (view_rs.next()) {
                    ArrayList<String> event = new ArrayList<>();
                    event.add(view_rs.getString("event_name"));
                    event.add(view_rs.getString("event_date"));
                    event.add(view_rs.getString("start_time"));
                    event.add(view_rs.getString("end_time"));
                    event.add(view_rs.getString("venue_name"));

                    events.add(event);
                    return events;
                }
            }
            return null;
        }
    }

    public ArrayList<ArrayList<String>> view_overlapped_events(Event event) throws SQLException {
        String view_query = _get_base_view_query() + """
                    WHERE e.event_id != ?
                        AND e.venue_id = ?
                        AND e.event_date = ?
                        AND ?::time BETWEEN e.start_time AND e.end_time
                """;

        try (PreparedStatement view_stmt = this.conn.prepareStatement(view_query)) {
            view_stmt.setInt(1, event.get_event_id());
            view_stmt.setInt(2, event.get_venue_id());
            view_stmt.setObject(3, event.get_event_date());
            view_stmt.setObject(4, event.get_start_time());

            ArrayList<ArrayList<String>> matched_events = new ArrayList<>();
            try (ResultSet view_rs = view_stmt.executeQuery()) {
                if (!view_rs.next()) return null;

                ArrayList<String> matched_event = new ArrayList<>();
                do {
                    matched_event.add(view_rs.getString("event_name"));
                    matched_event.add(view_rs.getString("event_date"));
                    matched_event.add(view_rs.getString("start_time"));
                    matched_event.add(view_rs.getString("end_time"));
                    matched_event.add(view_rs.getString("venue_name"));

                    matched_events.add(matched_event);

                } while (view_rs.next());
                return matched_events;
            }
        }
    }

    public Event record_search(String event_name) throws SQLException {
        String search_query = """
                    SELECT
                        event_name,
                        event_date,
                        start_time,
                        end_time,
                        venue_id,
                        event_id
                    FROM events
                    WHERE event_name ILIKE %s
                """;

        try (PreparedStatement search_stmt = this.conn.prepareStatement(search_query)) {
            try (ResultSet search_rs = search_stmt.executeQuery()) {
                if (search_rs.next()) {
                    return new Event(
                            search_rs.getString("event_name"),
                            search_rs.getDate("event_date").toLocalDate(),
                            search_rs.getTime("start_time").toLocalTime(),
                            search_rs.getTime("end_time").toLocalTime(),
                            search_rs.getInt("venue_id")
                    );
                }
            }
            return null;
        }
    }

    public boolean update_event(Event upd_event) throws SQLException {
        String update_query = """
                    UPDATE events
                    SET
                        event_name = ?,
                        event_date = ?,
                        start_time = ?,
                        end_time = ?,
                        venue_id = ?
                    WHERE event_id = ?
                """;

        try (PreparedStatement update_stmt = this.conn.prepareStatement(update_query)) {
            update_stmt.setString(1, upd_event.get_event_name());
            update_stmt.setObject(2, upd_event.get_event_date());
            update_stmt.setObject(3, upd_event.get_start_time());
            update_stmt.setObject(4, upd_event.get_end_time());
            update_stmt.setInt(5, upd_event.get_venue_id());

            update_stmt.executeUpdate();
            return true;
        }
    }

    public boolean delete_event(String event_name) throws SQLException {
        String delete_query = """
                    DELETE FROM events
                    WHERE event_name ILIKE ?
                """;

        try (PreparedStatement delete_stmt = this.conn.prepareStatement(delete_query)) {
            delete_stmt.setString(1, event_name);
            delete_stmt.executeUpdate();
            return true;
        }
    }

    public String check_status(String event_name) throws SQLException {
        String check_query = """
                    SELECT event_status
                    FROM event_details
                    WHERE event_name ILIKE ?
                """;

        try (PreparedStatement check_stmt = this.conn.prepareStatement(check_query)) {
            check_stmt.setString(1, event_name);

            try (ResultSet view_rs = check_stmt.executeQuery()) {
                if (view_rs.next()) {
                    return view_rs.getString("event_status");
                }
            }
            return null;
        }
    }

    public boolean empty_check() throws SQLException {
        String check_query = """
                    SELECT COUNT(*)
                    FROM events
                """;

        try (PreparedStatement check_stmt = this.conn.prepareStatement(check_query)) {
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