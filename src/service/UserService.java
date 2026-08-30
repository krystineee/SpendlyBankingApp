package service;

import model.User;
import util.DBConnection;

import java.sql.*;

public class UserService {

    public User login(String mobileNumber, String pin) {

        //if mobile number exists
        String sql = "SELECT * FROM users WHERE mobile_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mobileNumber);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                //if PIN is correct
                String storedPin = rs.getString("pin");

                if (!storedPin.equals(pin)) {
                    return null;
                }

                return new User(
                        rs.getLong("user_id"),
                        rs.getString("mobile_number"),
                        rs.getString("pin"),
                        rs.getString("full_name"),
                        rs.getDouble("balance"),
                        new TransactionService()
                                .getHistory(rs.getLong("user_id"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean mobileExists(String mobileNumber) {

        String sql =
                "SELECT 1 FROM users WHERE mobile_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mobileNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //register new user
    public User register(
            String mobileNumber,
            String pin,
            String fullName) {

        if (mobileExists(mobileNumber)) {
            return null;
        }

        String sql =
                "INSERT INTO users " +
                        "(mobile_number, pin, full_name, balance) " +
                        "VALUES (?, ?, ?, 0)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            stmt.setString(1, mobileNumber);
            stmt.setString(2, pin);
            stmt.setString(3, fullName);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {

                if (keys.next()) {
                    long newUserId = keys.getLong(1);
                    User newUser =
                            new User(
                                    mobileNumber,
                                    pin,
                                    fullName
                            );
                    newUser.setUserId(newUserId);
                    return newUser;
                }
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

