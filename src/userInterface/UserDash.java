package userInterface;

import model.User;

import javax.swing.*;
import java.awt.*;

public class UserDash {
    private final JFrame frame;
    private final User currentUser;
    private JPanel panel1;
    private JButton transactionButton;
    private JButton transferButton;
    private JButton cashInButton;
    private JButton logOutButton;
    private JLabel balanceLabel;
    private JLabel nameLabel;

    public UserDash(User currentUser) {
        this.currentUser = currentUser;

        frame = new JFrame("Dashboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 400));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        nameLabel.setText(currentUser.getFullName());

        balanceLabel.setText("₱" + currentUser.getBalance());

        transactionButton.addActionListener(e -> {
            new TransactionHistory(currentUser);
            frame.dispose();
        });

        transferButton.addActionListener(e -> {
            new Transfer(currentUser);
            frame.dispose();
        });

        cashInButton.addActionListener(e -> {
            new CashIn(currentUser);
            frame.dispose();
        });

        logOutButton.addActionListener(e -> {
            frame.dispose();
            new UserLogin();
        });
    }
}
