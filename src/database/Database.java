package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:sqlite:ticketDB.db";

    public Database() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                System.out.println("se conectó a la sqlite");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tickets ("
                + "qr_code TEXT PRIMARY KEY,"
                + "status TEXT NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStatus(String qrCode) {

        String sql = "SELECT status FROM tickets WHERE qr_code = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, qrCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertOrUpdateTicket(String qrCode, String status) {
        String sql = "INSERT OR REPLACE INTO tickets (qr_code, status) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, qrCode);
            pstmt.setString(2, status);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
