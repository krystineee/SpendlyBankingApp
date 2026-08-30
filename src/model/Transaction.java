package model;

import java.time.LocalDateTime;

public class Transaction {

    public enum Type {
        CASH_IN,
        TRANSFER_SENT,
        TRANSFER_RECEIVED
    }

    private Type type;
    private double amount;
    private String details;
    private LocalDateTime dateTime;

    public Transaction(Type type, double amount, String details, LocalDateTime dateTime) {
        this.type = type;
        this.amount = amount;
        this.details = details;
        this.dateTime = dateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    //Transaction History
    @Override
    public String toString() {
        return dateTime + " | " + type + " | ₱" + amount + " | " + details;
    }
}
