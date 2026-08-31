# Spendly Banking App
A Java Swing banking app with login, registration, cash-in, transfer, and transaction history, backed by a MariaDB database via JDBC.


### Tech Stack
**Language:** Java

**GUI:** Java Swing (built with IntelliJ's GUI Designer)

**Database:** MariaDB via JDBC (MySQL Connector/J)

### Database Setup
```bash
CREATE DATABASE IF NOT EXISTS spendly_bank;
USE spendly_bank;

CREATE TABLE users (
    user_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    mobile_number VARCHAR(11) NOT NULL,
    pin VARCHAR(4) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    balance DOUBLE NOT NULL DEFAULT '0',
    PRIMARY KEY (user_id) USING BTREE,
    UNIQUE INDEX mobile_number (mobile_number) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

CREATE TABLE transactions (
    transaction_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    user_id BIGINT(20) NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount DOUBLE NOT NULL,
    details VARCHAR(255) NULL DEFAULT NULL,
    date_time DATETIME NOT NULL,
    PRIMARY KEY (transaction_id) USING BTREE,
    INDEX user_id (user_id) USING BTREE,
    CONSTRAINT transactions_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (user_id) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

CREATE USER 'spendly_app'@'localhost' IDENTIFIED BY 'your_chosen_password';
GRANT ALL PRIVILEGES ON spendly_bank.* TO 'spendly_app'@'localhost';
FLUSH PRIVILEGES;
```

### Running the App
The database password reads from an environment variable.

1. In IntelliJ: Run → Edit Configurations → "Main" → Environment variables, add: ```SPENDLY_DB_PASSWORD=your_chosen_password ```
2. Add the MySQL Connector/J library (Project Structure → Libraries → From Maven → ```mysql:mysql-connector-java:8.0.33```)

3. Run Main.java
