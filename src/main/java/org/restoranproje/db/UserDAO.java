package org.restoranproje.db;

import org.restoranproje.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO { // user girişi kısmına geçildiğinde yapılacak
    public static void saveUser(User user, String role) {
        String sql = "INSERT INTO users (name, role) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, role);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
