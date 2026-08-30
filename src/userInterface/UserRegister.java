package userInterface;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class UserRegister {
    private final JFrame frame;
    private JPanel panel1;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField mobileNumberTextField;
    private JButton registerButton;
    private JButton signInButton;
    private JPasswordField pinPasswordField;
    private JPasswordField confirmPinPasswordField;

    public UserRegister() {
        frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(350, 550));
        frame.setResizable(false);
        frame.add(panel1);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        registerButton.addActionListener(e -> {
            String firstName = firstNameTextField.getText().trim();
            String lastName = lastNameTextField.getText().trim();
            String mobileNumber = mobileNumberTextField.getText().trim();
            String pin = new String(pinPasswordField.getPassword()).trim();
            String confirmPin = new String(confirmPinPasswordField.getPassword()).trim();

            if (firstName.isEmpty() || lastName.isEmpty() || mobileNumber.isEmpty()
                    || pin.isEmpty() || confirmPin.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please fill in all fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!firstName.matches("[a-zA-Z\\s'-]+")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "First name should only contain letters.",
                        "Invalid Name",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!lastName.matches("[a-zA-Z\\s'-]+")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Last name should only contain letters.",
                        "Invalid Name",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!mobileNumber.matches("09\\d{9}")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Mobile number must start with 09 and be exactly 11 digits.",
                        "Invalid Mobile Number",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!pin.matches("\\d{4}")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "PIN must be exactly 4 digits.",
                        "Invalid PIN",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!pin.equals(confirmPin)) {
                JOptionPane.showMessageDialog(
                        frame,
                        "PIN and Confirm PIN do not match.",
                        "PIN Mismatch",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String fullName = firstName + " " + lastName;
            User newUser = new UserService().register(mobileNumber, pin, fullName);

            if (newUser != null) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Registration successful! Welcome, " + fullName + ".",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                frame.dispose();
                new UserDash(newUser);
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "That mobile number is already registered.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        signInButton.addActionListener(e -> {
            new UserLogin();
            frame.dispose();
        });
    }
}
