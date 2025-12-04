package ems.dao;

import java.sql.*;

public class StudentDao {
    private final Connection conn;

    public StudentDao(Connection conn) {
        this.conn = conn;
    }

    public boolean studentExist(final String sr_code) throws SQLException {
        String search_query = """
            SELECT
                CASE
                    WHEN COUNT(*) > 0 THEN true
                    ELSE false
                END AS student_exists
            FROM students
            WHERE sr_code ILIKE ?
        """;

        try(PreparedStatement check_stmt = conn.prepareStatement(search_query)){
            check_stmt.setString(1, sr_code);
            try(ResultSet check_rs = check_stmt.executeQuery()){
                check_rs.next();
                return check_rs.getBoolean("student_exists");
            }
        }
    }
}
