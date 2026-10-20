package com.scc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "guest_name TEXT NOT NULL, " +
                "room_type TEXT NOT NULL, " +
                "nightly_rate REAL NOT NULL, " +
                "payment TEXT NOT NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addReservation(
            String guestName,
            String roomType,
            double nightlyRate,
            String payment) throws SQLException {

        String sql = "INSERT INTO reservations " +
                "(guest_name, room_type, nightly_rate, payment) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, guestName);
            pstmt.setString(2, roomType);
            pstmt.setDouble(3, nightlyRate);
            pstmt.setString(4, payment);

            pstmt.executeUpdate();
        }
    }

    public List<String[]> getAllReservations()
            throws SQLException {

        List<String[]> reservations = new ArrayList<>();

        String sql = "SELECT id, guest_name, room_type, " +
                "nightly_rate, payment FROM reservations ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String[] reservation = {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("guest_name"),
                        rs.getString("room_type"),
                        String.format("%.2f",
                                rs.getDouble("nightly_rate")),
                        rs.getString("payment")
                };

                reservations.add(reservation);
            }
        }

        return reservations;
    }
}
