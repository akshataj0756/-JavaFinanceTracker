package com.finance.tracker.login;

import com.finance.tracker.dao.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter Username: ");
		String username = sc.nextLine();

		System.out.print("Enter Password: ");
		String password = sc.nextLine();

		if (validateLogin(username, password)) {
			System.out.println("Login successful! Welcome " + username);
		} else {
			System.out.println("Invalid credentials.");
		}
		sc.close();
	}

	public static boolean validateLogin(String username, String password) {
		boolean isValid = false;

		try {
			Connection conn = DBConnection.getConnection();
			String query = "SELECT * FROM users WHERE username = ? AND password = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);

			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			isValid = rs.next(); // if user exists, rs.next() returns true

			rs.close();
			pstmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isValid;
	}
}
