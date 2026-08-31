package userInterface;

import model.User;
import service.TransactionService;

import javax.swing.*;
import java.awt.*;

public class Transfer {
    private final JFrame frame;
    private final User currentUser;
    private JButton returnButton;
    private JButton submitButton;
    private JTextField recipientTextField;
    private JTextField amountTextField;
    private JButton cancelButton;
    private JPanel panel1;

    public Transfer(User currentUser) {
        this.currentUser = currentUser;

        frame = new JFrame("Transfer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        submitButton.addActionListener(e -> {
            String recipientMobile = recipientTextField.getText().trim();
            String amountText = amountTextField.getText().trim();

            try {
                double amount = Double.parseDouble(amountText);

                TransactionService.TransferResult result =
                        new TransactionService().transfer(
                                currentUser,
                                recipientMobile,
                                amount
                        );

                if (result == TransactionService.TransferResult.SUCCESS) {
                    JOptionPane.showMessageDialog(frame, "Transfer successful!");
                    frame.dispose();
                    new UserDash(currentUser);
                } else {
                    JOptionPane.showMessageDialog(frame, result);
                    recipientTextField.setText("");
                    amountTextField.setText("");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.");
                amountTextField.setText("");
            }
        });

        cancelButton.addActionListener(e -> {
            recipientTextField.setText("");
            amountTextField.setText("");
        });

        returnButton.addActionListener(e -> {
            new UserDash(currentUser);
            frame.dispose();
        });
    }
}