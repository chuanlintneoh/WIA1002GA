import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class LoginPage extends JFrame implements ActionListener {
    private final RegisterPage registerPage;
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnLogin, btnRegister;
    private final JLabel forestbook;
    private final Database database;
    private int userID;
    private final TracebackFunction tracebackFunction;
    public LoginPage(TracebackFunction tracebackFunction){
        super("User Login Page");
        this.registerPage = null;
        this.database = new Database();
        this.tracebackFunction = tracebackFunction;

        // Initialize GUI components
        txtUsername = new JTextField("Username",20);
        txtUsername.setToolTipText("Enter your username/ email address/ contact no. here.");
        txtPassword = new JPasswordField("Password",20);
        txtPassword.setToolTipText("Enter your password here.");
        btnLogin = new JButton("Log In");
        btnLogin.setBackground(new Color(46,138,87));
        btnLogin.setForeground(Color.WHITE);
        btnRegister = new JButton("<html><u>Don't have an account</u></html>");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFont(btnLogin.getFont().deriveFont(Font.PLAIN));
        btnRegister.setForeground(Color.BLUE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(180, 238, 156));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        forestbook = new JLabel("ForestBook");
        forestbook.setFont(new Font("Curlz MT", Font.BOLD, 42));
        forestbook.setForeground(new Color(0, 128, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(forestbook,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(txtUsername, gbc);

        gbc.gridy = 2;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        panel.add(btnRegister, gbc);

        // Add action listeners to buttons
        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        // Set frame properties
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);
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
            this.userID = database.authenticateUser(username,password);
            if (userID > 0){
                JOptionPane.showMessageDialog(this, "Log in successful.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                tracebackFunction.pushPage(new HomePage(database.get("username",userID),tracebackFunction));
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "Username and password does not match! Please log in again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getSource() == btnRegister){
            if (registerPage == null){
                new RegisterPage(tracebackFunction);
            }
            else {
                registerPage.setVisible(true);
            }
            LoginPage.this.setVisible(false);
        }
    }
}