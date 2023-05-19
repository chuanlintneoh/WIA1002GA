import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class LoginPage extends JFrame implements ActionListener {
    private final RegisterPage registerPage;
    public static void main(String[] args) {
        new LoginPage();
    }
    //private JLabel lblRememberMe;
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    //private JCheckBox chkRememberMe;
    private final JButton btnLogin, btnRegister;// btnForgotPassword
    public LoginPage(){
        super("User Login Page");
        registerPage = null;
        // Initialize GUI components
        txtUsername = new JTextField("Username",20);
        txtPassword = new JPasswordField("Password",20);
//        lblRememberMe = new JLabel("Remember Me?");
//        chkRememberMe = new JCheckBox();
        btnLogin = new JButton("Log In");
        btnRegister = new JButton("Don't have an account");
//        btnForgotPassword = new JButton("Forgot password");
        // Add components to the frame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(txtUsername, gbc);
        gbc.gridy = 1;
        panel.add(txtPassword, gbc);
//        gbc.gridy = 2;
//        gbc.gridwidth = 1;
//        panel.add(lblRememberMe, gbc);
//        gbc.gridx = 1;
//        panel.add(chkRememberMe, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);
//        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
//        panel.add(btnForgotPassword, gbc);
        gbc.gridx = 1;
        panel.add(btnRegister, gbc);
        // Add action listeners to buttons
        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);
//        btnForgotPassword.addActionListener(this);
        // Set frame properties
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin){
            String username = txtUsername.getText();
            String password = PasswordHashing.hashPassword(new String(txtPassword.getPassword()));
            if (username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Database database = new Database();
            if (database.authenticateUser(username,password)){
                JOptionPane.showMessageDialog(this, "Log in successful.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this, "Username and password does not match! Please log in again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getSource() == btnRegister){
            if (registerPage == null){
                new RegisterPage();
            }
            else {
                registerPage.setVisible(true);
            }
            LoginPage.this.setVisible(false);
        }
    }
}
