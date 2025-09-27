package com.finance.tracker.dao;

import com.finance.tracker.model.User;
import java.sql.*;

public class UserDAO {

	public int addUser(User user) throws SQLException {
		String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());

			int rows = ps.executeUpdate();
			if (rows > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1); // Return the generated user ID
				}
			}
		}
		return -1; // Indicates failure
	}
}
