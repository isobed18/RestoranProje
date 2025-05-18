package org.restoranproje.db;

import org.restoranproje.model.User;
import org.restoranproje.model.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO { // user girişi kısmına geçildiğinde yapılacak
    public static void saveUser(User user) {
        String sql = "INSERT INTO users (name, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getUserType().name());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateUser(String username, String password, UserType userType) {
        String sql = "SELECT * FROM users WHERE name = ? AND password = ? AND role = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, userType.name());

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a matching user is found

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
