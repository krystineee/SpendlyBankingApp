package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long userId;
    private String mobileNumber;
    private String pin;
    private String fullName;
    private double balance;
    private List<Transaction> transactions;

    //existing user
    public User(long userId, String mobileNumber, String pin, String fullName, double balance, List<Transaction> transactions) {
        this.userId = userId;
        this.mobileNumber = mobileNumber;
        this.pin = pin;
        this.fullName = fullName;
        this.balance = balance;
        this.transactions = transactions;
    }

    //new user
    public User(String mobileNumber, String pin, String fullName) {
        this.mobileNumber = mobileNumber;
        this.pin = pin;
        this.fullName = fullName;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction t) {
        this.transactions.add(t);
    }
}