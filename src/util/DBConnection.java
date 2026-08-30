package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/spendly_bank";
    private static final String USER = "spendly_app";
    private static final String PASSWORD = System.getenv("SPENDLY_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null) {
            throw new SQLException(
                    "SPENDLY_DB_PASSWORD environment variable is not set. " +
                            "See project README for setup instructions."
            );
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
