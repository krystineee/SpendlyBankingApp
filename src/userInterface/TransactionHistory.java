package userInterface;

import model.Transaction;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransactionHistory {
    private final JFrame frame;
    private final User currentUser;
    private JPanel panel1;
    private JTable table1;
    private JButton returnButton;
    private JLabel balanceLabel;

    public TransactionHistory(User currentUser) {
        this.currentUser = currentUser;

        frame = new JFrame("Transaction History");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 575));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        balanceLabel.setText("₱" + currentUser.getBalance());

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Type", "Amount", "Details", "Date"},
                0
        );

        for (Transaction transaction : currentUser.getTransactions()) {
            model.addRow(new Object[]{
                    transaction.getType(),
                    "₱" + transaction.getAmount(),
                    transaction.getDetails(),
                    transaction.getDateTime()
            });
        }

        table1.setModel(model);

        returnButton.addActionListener(e -> {
            new UserDash(currentUser);
            frame.dispose();
        });
    }
}

