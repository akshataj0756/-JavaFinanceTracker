package com.finance.tracker.app;

import com.finance.tracker.dao.TransactionDAO;
import com.finance.tracker.model.Transaction1;
import com.finance.tracker.model.User;
import com.finance.tracker.dao.UserDAO;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionApp {
	private static final Scanner sc = new Scanner(System.in);
	// private static final TransactionDAO dao = new TransactionDAO();

	public static void main(String[] args) throws SQLException {
		System.out.println("Welcome to Java Finance Tracker!");

		// Simple login/signup interface
		int userId = authenticateUser();

		TransactionDAO dao = new TransactionDAO();

		boolean continueProgram = true;

		while (continueProgram) {
			System.out.println("\n===== Transaction Menu =====");
			System.out.println("1. Add Transaction");
			System.out.println("2. View All Transactions");
			System.out.println("3. Update Transaction");
			System.out.println("4. Delete Transaction");
			System.out.println("5. Filter Transactions");
			System.out.println("6. View Summary");
			System.out.println("7. Exit");
			System.out.print("Choose an option: ");
			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				addTransaction(userId, dao);
				break;
			case 2:
				viewAllTransactions(userId, dao);
				break;
			case 3:
				updateTransaction(dao);
				break;
			case 4:
				deleteTransaction(dao);
				break;
			case 5:
				filterTransactions(userId, dao);
				break;
			case 6:
				showSummary(userId, dao);
				break;
			case 7:
				System.out.println("Exiting program. Thank you!");
				continueProgram = false;
				break;
			default:
				System.out.println("Invalid choice! Please select again.");
			}
		}
		sc.close();
	}

	private static int authenticateUser() throws SQLException {
		System.out.print("Enter your User ID (or 0 to sign up): ");
		int userId = sc.nextInt();
		sc.nextLine(); // consume newline

		if (userId == 0) {
			return signUpUser();
		}
		return userId;
	}

	private static int signUpUser() throws SQLException {
		System.out.println("===== Sign Up =====");
		System.out.print("Enter your name: ");
		String name = sc.nextLine();

		System.out.print("Enter your email: ");
		String email = sc.nextLine();

		System.out.print("Enter your password: ");
		String password = sc.nextLine();

		User user = new User(name, email, password);
		UserDAO userDao = new UserDAO();
		int userId = userDao.addUser(user);

		if (userId > 0) {
			System.out.println("Sign up successful! Your User ID is: " + userId);
			return userId;
		} else {
			System.out.println("Sign up failed. Please try again.");
			return authenticateUser(); // retry
		}
	}

	private static void addTransaction(int userId, TransactionDAO dao) throws SQLException {
		Transaction1 t = new Transaction1();

		double amount;
		while (true) {
			System.out.print("Enter amount: ");
			amount = sc.nextDouble();
			if (amount >= 0)
				break;
			System.out.println("Amount cannot be negative. Try again.");
		}
		t.setAmount(amount);

		sc.nextLine();
		System.out.print("Enter category: ");
		t.setCategory(sc.nextLine());

		String dateStr;
		while (true) {
			System.out.print("Enter date (dd-MM-yyyy): ");
			dateStr = sc.nextLine();
			if (isValidDate(dateStr, dao))
				break;
			System.out.println("Invalid date format! Please enter date as dd-MM-yyyy.");
		}
		t.setDate(dateStr);

		String paymentMode;
		while (true) {
			System.out.print("Enter payment mode (Cash/UPI/Card/Other): ");
			paymentMode = sc.nextLine();
			if (Arrays.asList("Cash", "UPI", "Card", "Other").contains(paymentMode))
				break;
			System.out.println("Invalid mode! Choose from Cash, UPI, Card, Other.");
		}
		t.setPaymentMode(paymentMode);

		t.setUserId(userId);
		boolean added = dao.addTransaction(t);
		System.out.println("Transaction added: " + added);
	}

	private static void viewAllTransactions(int userId, TransactionDAO dao) throws SQLException {
		List<Transaction1> transactions = dao.getAllTransactions(userId);
		if (transactions.isEmpty()) {
			System.out.println("No transactions found.");
		} else {
			System.out.println("Your Transactions:");
			for (Transaction1 tr : transactions) {
				System.out.println("Transaction ID: " + tr.getId() + ", Amount: " + tr.getAmount() + ", Category: "
						+ tr.getCategory() + ", Date: " + tr.getDate() + ", Mode: " + tr.getPaymentMode());
			}
		}
	}

	private static void updateTransaction(TransactionDAO dao) throws SQLException {
		System.out.print("Enter transaction ID to update: ");
		int updateId = sc.nextInt();
		Transaction1 txToUpdate = dao.getTransactionById(updateId);
		if (txToUpdate == null) {
			System.out.println("Transaction not found.");
			return;
		}

		sc.nextLine();
		System.out.print("Do you really want to update this transaction? (yes/no): ");
		if (!sc.nextLine().equalsIgnoreCase("yes")) {
			System.out.println("Update cancelled.");
			return;
		}

		double amount;
		while (true) {
			System.out.print("Enter new amount: ");
			amount = sc.nextDouble();
			if (amount >= 0)
				break;
			System.out.println("Amount cannot be negative. Try again.");
		}
		txToUpdate.setAmount(amount);

		sc.nextLine();
		System.out.print("Enter new category: ");
		txToUpdate.setCategory(sc.nextLine());

		String updateDate;
		while (true) {
			System.out.print("Enter new date (dd-MM-yyyy): ");
			updateDate = sc.nextLine();
			if (isValidDate(updateDate, dao))
				break;
			System.out.println("Invalid date format! Please enter date as dd-MM-yyyy.");
		}
		txToUpdate.setDate(updateDate);

		String paymentMode;
		while (true) {
			System.out.print("Enter new payment mode (Cash/UPI/Card/Other): ");
			paymentMode = sc.nextLine();
			if (Arrays.asList("Cash", "UPI", "Card", "Other").contains(paymentMode))
				break;
			System.out.println("Invalid mode! Choose from Cash, UPI, Card, Other.");
		}
		txToUpdate.setPaymentMode(paymentMode);

		boolean updated = dao.updateTransaction(txToUpdate);
		System.out.println("Transaction updated: " + updated);
	}

	private static void deleteTransaction(TransactionDAO dao) throws SQLException {
		System.out.print("Enter transaction ID to delete: ");
		int deleteId = sc.nextInt();
		Transaction1 txToDelete = dao.getTransactionById(deleteId);
		if (txToDelete == null) {
			System.out.println("Transaction not found.");
			return;
		}

		sc.nextLine();
		System.out.print("Are you sure you want to delete this transaction? (yes/no): ");
		if (!sc.nextLine().equalsIgnoreCase("yes")) {
			System.out.println("Deletion cancelled.");
			return;
		}

		boolean deleted = dao.deleteTransaction(deleteId);
		System.out.println("Transaction deleted: " + deleted);
	}

	private static void filterTransactions(int userId, TransactionDAO dao) throws SQLException {
		sc.nextLine();
		System.out.println("Filter by: 1. Date Range  2. Category");
		int filterChoice = sc.nextInt();
		sc.nextLine();

		List<Transaction1> filtered = new ArrayList<>();

		if (filterChoice == 1) {
			System.out.print("Enter start date (dd-MM-yyyy): ");
			String start = sc.nextLine();
			System.out.print("Enter end date (dd-MM-yyyy): ");
			String end = sc.nextLine();

			filtered = dao.getTransactionsByDateRange(userId, start, end);
		} else if (filterChoice == 2) {
			System.out.print("Enter category: ");
			String cat = sc.nextLine();
			filtered = dao.getTransactionsByCategory(userId, cat);
		}

		if (filtered.isEmpty()) {
			System.out.println("No matching transactions found.");
		} else {
			for (Transaction1 t : filtered) {
				System.out.println("Transaction ID: " + t.getId() + ", Amount: " + t.getAmount() + ", Category: "
						+ t.getCategory() + ", Date: " + t.getDate() + ", Mode: " + t.getPaymentMode());
			}
		}
	}

	private static void showSummary(int userId, TransactionDAO dao) throws SQLException {
		System.out.println("Monthly Summary:");
		Map<String, Double> monthly = dao.getMonthlySummary(userId);
		monthly.forEach((month, total) -> System.out.println(month + ": ₹" + total));

		System.out.println("\nYearly Summary:");
		Map<String, Double> yearly = dao.getYearlySummary(userId);
		yearly.forEach((year, total) -> System.out.println(year + ": ₹" + total));
	}

	private static boolean isValidDate(String dateStr, TransactionDAO dao) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}
