package com.finance.tracker.dao;

import com.finance.tracker.model.Transaction1;
import java.sql.*;
import java.util.*;

public class TransactionDAO {
	private Connection connection;

	public TransactionDAO() throws SQLException {
		connection = DBConnection.getConnection();
	}

	// Add a transaction
	public boolean addTransaction(Transaction1 transaction) {
		String query = "INSERT INTO transactions (amount, category, date, payment_mode, user_id) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setDouble(1, transaction.getAmount());
			ps.setString(2, transaction.getCategory());
			ps.setString(3, transaction.getDate());
			ps.setString(4, transaction.getPaymentMode());
			ps.setInt(5, transaction.getUserId());

			int rowsInserted = ps.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Get all transactions for a user
	public List<Transaction1> getAllTransactions(int userId) {
		List<Transaction1> transactions = new ArrayList<>();
		String query = "SELECT * FROM transactions WHERE user_id = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				transactions.add(mapResultSetToTransaction(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}

	// Delete transaction
	public boolean deleteTransaction(int id) {
		String query = "DELETE FROM transactions WHERE id = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, id);
			int rowsDeleted = ps.executeUpdate();
			return rowsDeleted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Update transaction
	public boolean updateTransaction(Transaction1 transaction) {
		String query = "UPDATE transactions SET amount=?, category=?, date=?, payment_mode=? WHERE id=?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setDouble(1, transaction.getAmount());
			ps.setString(2, transaction.getCategory());
			ps.setString(3, transaction.getDate());
			ps.setString(4, transaction.getPaymentMode());
			ps.setInt(5, transaction.getId());

			int rowsUpdated = ps.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Get transaction by ID
	public Transaction1 getTransactionById(int id) {
		String sql = "SELECT * FROM transactions WHERE id = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return mapResultSetToTransaction(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Filter by date range
	public List<Transaction1> getTransactionsByDateRange(int userId, String startDate, String endDate)
			throws SQLException {
		List<Transaction1> list = new ArrayList<>();
		String sql = "SELECT * FROM transactions WHERE user_id = ? AND date BETWEEN ? AND ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToTransaction(rs));
			}
		}
		return list;
	}

	// Filter by category
	public List<Transaction1> getTransactionsByCategory(int userId, String category) throws SQLException {
		List<Transaction1> list = new ArrayList<>();
		String sql = "SELECT * FROM transactions WHERE user_id = ? AND category = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.setString(2, category);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(mapResultSetToTransaction(rs));
			}
		}
		return list;
	}

	// Monthly Summary
	public Map<String, Double> getMonthlySummary(int userId) throws SQLException {
		Map<String, Double> monthly = new LinkedHashMap<>();
		String sql = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(amount) AS total "
				+ "FROM transactions WHERE user_id = ? GROUP BY month ORDER BY month";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				monthly.put(rs.getString("month"), rs.getDouble("total"));
			}
		}
		return monthly;
	}

	// Yearly Summary
	public Map<String, Double> getYearlySummary(int userId) throws SQLException {
		Map<String, Double> yearly = new LinkedHashMap<>();
		String sql = "SELECT YEAR(date) AS year, SUM(amount) AS total "
				+ "FROM transactions WHERE user_id = ? GROUP BY year ORDER BY year";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				yearly.put(rs.getString("year"), rs.getDouble("total"));
			}
		}
		return yearly;
	}

	// Helper:Convert ResultSet to Transaction object
	private Transaction1 mapResultSetToTransaction(ResultSet rs) throws SQLException {
		Transaction1 t = new Transaction1();
		t.setId(rs.getInt("id"));
		t.setAmount(rs.getDouble("amount"));
		t.setCategory(rs.getString("category"));
		t.setDate(rs.getString("date"));
		t.setPaymentMode(rs.getString("payment_mode"));
		t.setUserId(rs.getInt("user_id"));
		return t;
	}
}
