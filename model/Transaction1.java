package com.finance.tracker.model;

public class Transaction1 {
	private int id;
	private int userId;
	private double amount;
	private String type;
	private String category;
	private String date;
	private String paymentMode;

	// Default constructor
	public Transaction1() {
	}

	// Parameterized constructor
	public Transaction1(int id, int userId, double amount, String type, String category, String date,
			String paymentMode) {
		this.id = id;
		this.userId = userId;
		this.amount = amount;
		this.type = type;
		this.category = category;
		this.date = date;
		this.paymentMode = paymentMode;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
}
