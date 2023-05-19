import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class UserRegistrationGUI extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new UserRegistrationGUI();
    }
    // Declare GUI components
    private JLabel lblUsername, lblEmail, lblPhone, lblPassword, lblConfirmPassword;
    private JTextField txtUsername, txtEmail, txtPhone;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegister, btnLogin;
    // Constructor
    public UserRegistrationGUI() {
        super("User Registration");
        // Initialize GUI components
        lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(20);
        lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);
        lblPhone = new JLabel("Phone:");
        txtPhone = new JTextField(20);
        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);
        lblConfirmPassword = new JLabel("Confirm Password:");
        txtConfirmPassword = new JPasswordField(20);
        btnRegister = new JButton("Register");
        btnLogin = new JButton("Login");

        // Add components to the frame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(txtUsername, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtEmail, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPhone, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtPhone, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblPassword, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(txtPassword, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblConfirmPassword, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(txtConfirmPassword, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(btnRegister, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        // Add action listeners to buttons
        btnRegister.addActionListener(this);
        btnLogin.addActionListener(this);

        // Set frame properties
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Handle button clicks
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            // Get user input
            String username = txtUsername.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            // Validate input
            if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Create user object and store in database
            User user = new User(username, email, phone,password);
            // Save user object to database or file
            // ...

            JOptionPane.showMessageDialog(this, "User registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == btnLogin) {
            // Get user input
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            // Validate input
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if user exists in database and password is correct
            boolean isAuthenticated = false;
            // Load user object from database or file
            // ...

            if (isAuthenticated) {
                JOptionPane.showMessageDialog(this, "Login successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
