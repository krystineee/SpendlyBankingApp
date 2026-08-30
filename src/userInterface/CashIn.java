package userInterface;

import model.User;
import service.TransactionService;

import javax.swing.*;
import java.awt.*;

public class CashIn {
    private final JFrame frame;
    private final User currentUser;
    private JButton submitButton;
    private JPanel panel1;
    private JButton returnButton;
    private JTextField amountTextField;
    private JButton cancelButton;

    public CashIn(User currentUser) {
        this.currentUser = currentUser;

        frame = new JFrame("Cash In");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        submitButton.addActionListener(e -> {

            String amountText = amountTextField.getText().trim();

            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter an amount."
                );
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);

                boolean success =
                        new TransactionService().cashIn(currentUser, amount);

                if (success) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Cash in successful!"
                    );
                    frame.dispose();
                    new UserDash(currentUser);

                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Cash in failed."
                    );
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter a valid amount."
                );
                amountTextField.setText("");
            }
        });

        cancelButton.addActionListener(e -> {
            amountTextField.setText("");
        });

        returnButton.addActionListener(e -> {
            new UserDash(currentUser);
            frame.dispose();
        });
    }
}
