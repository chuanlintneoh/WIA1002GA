import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserManagementApp extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JTextArea outputArea;
    private Database userManagementApp;

    public UserManagementApp() {
        userManagementApp = new Database();

        // Set up the frame
        setTitle("User Management App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create input components
        userIdField = new JTextField();
        passwordField = new JPasswordField();
        JButton verifyButton = new JButton("Verify Admin");
        JButton viewUsersButton = new JButton("View All Users");
        JButton deleteUserButton = new JButton("Delete User");

        // Create output component
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        // Add components to the frame
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("User ID:"));
        inputPanel.add(userIdField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(verifyButton);
        inputPanel.add(new JLabel()); // Placeholder for layout

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(viewUsersButton);
        buttonPanel.add(deleteUserButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(viewUsersButton, BorderLayout.WEST);
        add(deleteUserButton, BorderLayout.EAST);

        // Register action listeners
        verifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verifyAdmin();
            }
        });

        viewUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAllUsers();
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });

        setVisible(true);
    }

    private void verifyAdmin() {
        int userId;
        try {
            userId = Integer.parseInt(userIdField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid user ID format.");
            return;
        }
        String password = new String(passwordField.getPassword());

        boolean isAdmin = userManagementApp.verifyAdmin(userId, password);
        if (isAdmin) {
            outputArea.setText("Admin verified for User ID: " + userId);
        } else {
            outputArea.setText("Incorrect password!");
        }
    }

    private void viewAllUsers() {
        outputArea.setText(""); // Clear previous output

        StringBuilder sb = new StringBuilder();
        sb.append("User ID\tName\tUsername\tEmail\tContact No\n");

        for (User user : userManagementApp.viewAllUsers()) {
            sb.append(user.getUserId()).append("\t")
                    .append(user.getName()).append("\t")
                    .append(user.getUsername()).append("\t")
                    .append(user.getemailAddress()).append("\t")
                    .append(user.getContactNo()).append("\n");
        }

        outputArea.setText(sb.toString());
    }

    private void deleteUser() {
        String userId = userIdField.getText();

        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide a valid user ID.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid user ID format.");
            return;
        }

        JOptionPane.showMessageDialog(this, "User deleted successfully.");
    }

        public static void main(String[] args) {
            // Create an instance of the UserManagementApp
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new UserManagementApp();
                }
            });
        }
    }




