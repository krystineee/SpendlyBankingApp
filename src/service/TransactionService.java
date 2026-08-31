package service;

import model.Transaction;
import model.User;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    // Get transaction history
    public List<Transaction> getHistory(long userId) {
        List<Transaction> history = new ArrayList<>();

        String sql = "SELECT * FROM transactions " +
                "WHERE user_id = ? ORDER BY date_time DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    history.add(
                            new Transaction(
                                    Transaction.Type.valueOf(
                                            rs.getString("type")
                                    ),
                                    rs.getDouble("amount"),
                                    rs.getString("details"),
                                    rs.getTimestamp("date_time")
                                            .toLocalDateTime()
                            )
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }

    // Cash in
    public boolean cashIn(User user, double amount) {

        if (amount <= 0) {
            return false;
        }

        String updateBalance =
                "UPDATE users SET balance = balance + ? " +
                        "WHERE user_id = ?";

        String insertTxn =
                "INSERT INTO transactions " +
                        "(user_id, type, amount, details, date_time) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            // Update balance
            try (PreparedStatement stmt1 =
                         conn.prepareStatement(updateBalance)) {

                stmt1.setDouble(1, amount);
                stmt1.setLong(2, user.getUserId());

                int rowsUpdated = stmt1.executeUpdate();

                // Make sure user exists
                if (rowsUpdated == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // Create transaction record
            LocalDateTime now = LocalDateTime.now();

            try (PreparedStatement stmt2 =
                         conn.prepareStatement(insertTxn)) {

                stmt2.setLong(1, user.getUserId());
                stmt2.setString(
                        2,
                        Transaction.Type.CASH_IN.name()
                );
                stmt2.setDouble(3, amount);
                stmt2.setString(4, "Cash in");
                stmt2.setTimestamp(
                        5,
                        Timestamp.valueOf(now)
                );

                stmt2.executeUpdate();
            }

            conn.commit();

            // Update current
            user.setBalance(
                    user.getBalance() + amount
            );

            user.addTransaction(
                    new Transaction(
                            Transaction.Type.CASH_IN,
                            amount,
                            "Cash in",
                            now
                    )
            );

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Transfer results
    public enum TransferResult {
        SUCCESS,
        RECEIVER_NOT_FOUND,
        INSUFFICIENT_BALANCE,
        INVALID_AMOUNT,
        ERROR
    }


    // Transfer money
    public TransferResult transfer(
            User sender,
            String receiverMobile,
            double amount) {

        // Check amount
        if (amount <= 0) {
            return TransferResult.INVALID_AMOUNT;
        }

        // Check sender balance
        if (amount > sender.getBalance()) {
            return TransferResult.INSUFFICIENT_BALANCE;
        }

        String findReceiver =
                "SELECT user_id FROM users " +
                        "WHERE mobile_number = ?";

        String updateSender =
                "UPDATE users " +
                        "SET balance = balance - ? " +
                        "WHERE user_id = ?";

        String updateReceiver =
                "UPDATE users " +
                        "SET balance = balance + ? " +
                        "WHERE user_id = ?";

        String insertTxn =
                "INSERT INTO transactions " +
                        "(user_id, type, amount, details, date_time) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            // Find receiver
            long receiverId;

            try (PreparedStatement stmt =
                         conn.prepareStatement(findReceiver)) {

                stmt.setString(1, receiverMobile);

                try (ResultSet rs = stmt.executeQuery()) {

                    if (!rs.next()) {
                        conn.rollback();
                        return TransferResult.RECEIVER_NOT_FOUND;
                    }

                    receiverId = rs.getLong("user_id");
                }
            }


            // Update sender balance
            try (PreparedStatement stmt =
                         conn.prepareStatement(updateSender)) {

                stmt.setDouble(1, amount);
                stmt.setLong(2, sender.getUserId());

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated == 0) {
                    conn.rollback();
                    return TransferResult.ERROR;
                }
            }


            // Update receiver balance
            try (PreparedStatement stmt =
                         conn.prepareStatement(updateReceiver)) {

                stmt.setDouble(1, amount);
                stmt.setLong(2, receiverId);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated == 0) {
                    conn.rollback();
                    return TransferResult.ERROR;
                }
            }


            // Create transaction records
            LocalDateTime now = LocalDateTime.now();


            // Sender transaction
            try (PreparedStatement stmt =
                         conn.prepareStatement(insertTxn)) {

                stmt.setLong(1, sender.getUserId());

                stmt.setString(
                        2,
                        Transaction.Type.TRANSFER_SENT.name()
                );

                stmt.setDouble(3, amount);

                stmt.setString(
                        4,
                        "Sent to " + receiverMobile
                );

                stmt.setTimestamp(
                        5,
                        Timestamp.valueOf(now)
                );

                stmt.executeUpdate();
            }


            // Receiver transaction
            try (PreparedStatement stmt =
                         conn.prepareStatement(insertTxn)) {

                stmt.setLong(1, receiverId);

                stmt.setString(
                        2,
                        Transaction.Type.TRANSFER_RECEIVED.name()
                );

                stmt.setDouble(3, amount);

                stmt.setString(
                        4,
                        "Received from "
                                + sender.getMobileNumber()
                );

                stmt.setTimestamp(
                        5,
                        Timestamp.valueOf(now)
                );

                stmt.executeUpdate();
            }

            conn.commit();

            sender.setBalance(
                    sender.getBalance() - amount
            );

            sender.addTransaction(
                    new Transaction(
                            Transaction.Type.TRANSFER_SENT,
                            amount,
                            "Sent to " + receiverMobile,
                            now
                    )
            );

            return TransferResult.SUCCESS;

        } catch (SQLException e) {
            e.printStackTrace();
            return TransferResult.ERROR;
        }
    }
}

