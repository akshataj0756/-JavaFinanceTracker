package com.finance.tracker.dao;

import java.sql.Connection;

import java.sql.SQLException;

public class TestDBConnection {

	public static void main(String[] args) {
		try {
			Connection conn = DBConnection.getConnection();
			if (conn != null) {
				System.out.println("Connection successful!");
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("Connection failed!");
			e.printStackTrace();
		}
	}
}
