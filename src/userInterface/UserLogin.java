package userInterface;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class UserLogin {
    private final JFrame frame;
    private JPanel panel1;
    private JButton logInButton;
    private JButton signUpButton;
    private JTextField mobileNumberTextField;
    private JPasswordField pinPasswordField;

    private int loginAttempts = 0;

    public UserLogin() {
        frame = new JFrame("Log In");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        logInButton.addActionListener(e -> {
            String mobileNumber = mobileNumberTextField.getText().trim();
            String pin = new String(pinPasswordField.getPassword()).trim();

            if (mobileNumber.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter both mobile number and PIN.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            User loggedInUser = new UserService().login(mobileNumber, pin);

            if (loggedInUser != null) {
                frame.dispose();
                new UserDash(loggedInUser);
            } else {
                loginAttempts++;
                if (loginAttempts >= 3) {
                    logInButton.setEnabled(false);

                    JOptionPane.showMessageDialog(
                            frame,
                            "You have exceeded the maximum number of login attempts.\n" +
                                    "Please contact Customer Service for assistance.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                    );

                    loginAttempts = 0;
                    mobileNumberTextField.setText("");
                    pinPasswordField.setText("");
                    logInButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Invalid mobile number or PIN.\nAttempts remaining: "
                                    + (3 - loginAttempts),
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        signUpButton.addActionListener(e -> {
            new UserRegister();
            frame.dispose();
        });
    }
}
