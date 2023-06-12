import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Arrays;
public class RegisterPage extends JFrame implements ActionListener {
    private final LoginPage loginPage;
    private final JLabel forestbook,lblName, lblUsername, lblPassword, lblConfirmPassword, lblEmail, lblContactNo, lblDOB, lblGender;
    private final JTextField txtName, txtUsername, txtEmail, txtContactNo;
    private final JPasswordField txtPassword, txtConfirmPassword;
    private final JComboBox<Integer> birthDay, birthYear;
    private final JComboBox<String> birthMonth;
    private final JRadioButton radioMale, radioFemale, radioNotSet;
    private final JButton btnRegister, btnLogin;
    private final TracebackFunction tracebackFunction;
    private final Database database;
    public RegisterPage(TracebackFunction tracebackFunction){
        super("User Registration Page");
        this.loginPage = null;
        this.tracebackFunction = tracebackFunction;
        this.database = new Database();
        // Initialize GUI components
        forestbook = new JLabel("ForestBook");
        forestbook.setFont(new Font("Curlz MT", Font.BOLD, 30));
        forestbook.setForeground(new Color(0, 128, 0));

        lblName = new JLabel("Name:");
        txtName = new JTextField(20);
        lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(20);
        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField("Password",20);
        lblConfirmPassword = new JLabel("Confirm Password:");
        txtConfirmPassword = new JPasswordField("Password",20);
        lblEmail = new JLabel("Email address:");
        txtEmail = new JTextField(20);
        lblContactNo = new JLabel("Contact No.:");
        txtContactNo = new JTextField(20);
        lblDOB = new JLabel("Date Of Birth:");
        Integer[] days = new Integer[31];
        for (int i = 1; i <= days.length; i++){
            days[i-1] = i;
        }
        birthDay = new JComboBox<>(days);
        birthMonth = new JComboBox<>(new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"});
        Integer[] years = new Integer[100];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1; i <= years.length; i++){
            years[i-1] = currentYear-100+i;
        }
        birthYear = new JComboBox<>(years);
        lblGender = new JLabel("Gender:");
        radioMale = new JRadioButton("Male");
        radioFemale = new JRadioButton("Female");
        radioNotSet = new JRadioButton("Prefer not to tell");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(radioMale);
        genderGroup.add(radioFemale);
        genderGroup.add(radioNotSet);

        btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(46,138,87));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(this);
        btnLogin = new JButton("<html><u>Already have an Account</u></html>");
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setFont(btnLogin.getFont().deriveFont(Font.PLAIN));
        btnLogin.setForeground(Color.BLUE);
        btnLogin.addActionListener(this);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(180, 238, 156));
        headerPanel.add(forestbook);
        // Add components to the frame
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(180, 238, 156));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(headerPanel, gbc);

        gbc.gridy = 1;
        panel.add(lblName, gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblPassword, gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblConfirmPassword, gbc);
        gbc.gridx = 1;
        panel.add(txtConfirmPassword, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(lblContactNo, gbc);
        gbc.gridx = 1;
        panel.add(txtContactNo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(lblDOB, gbc);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(birthDay, gbc);
        gbc.gridx = 1;
        panel.add(birthMonth, gbc);
        gbc.gridx = 2;
        panel.add(birthYear, gbc);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblGender, gbc);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(radioMale, gbc);
        gbc.gridx = 1;
        panel.add(radioFemale, gbc);
        gbc.gridx = 2;
        panel.add(radioNotSet, gbc);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 4;
        panel.add(btnRegister, gbc);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 4;
        panel.add(btnLogin, gbc);

        // Set frame properties
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister){
            String name = txtName.getText();
            String username = txtUsername.getText();
            String password = PasswordHashing.hashPassword(new String(txtPassword.getPassword()));
            String confirmPassword = PasswordHashing.hashPassword(new String(txtConfirmPassword.getPassword()));
            String emailAddress = txtEmail.getText();
            String contactNo = txtContactNo.getText();
            int day = (int) birthDay.getSelectedItem();
            int year = (int) birthYear.getSelectedItem();
            String monthString = (String) birthMonth.getSelectedItem();
            int month = Arrays.asList(new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"}).indexOf(monthString);
            if (!isValidDate(day, month + 1, year)) {
                JOptionPane.showMessageDialog(this, "Invalid date of birth selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String birthDate = String.format("%04d-%02d-%02d", year, month + 1, day);
            char gender;
            if (radioMale.isSelected()) {
                gender = 'M';
            } else if (radioFemale.isSelected()) {
                gender = 'F';
            } else {
                gender = '-';
            }
            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match! Please re-enter the password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!database.isUsernameAvailable(username)){
                JOptionPane.showMessageDialog(this, "Username not available! Please re-enter the username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new User(name,username,password,emailAddress,contactNo,birthDate,gender);
            JOptionPane.showMessageDialog(this, "Account successfully registered.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            int userID = database.authenticateUser(username,password);
            if (userID > 0){
                JOptionPane.showMessageDialog(this, "Log in successful.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                tracebackFunction.pushPage(new HomePage(database.get("username",userID),tracebackFunction));
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "Username and password does not match! Please log in again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getSource() == btnLogin){
            if (loginPage == null){
                new LoginPage(tracebackFunction);
            }
            else {
                loginPage.setVisible(true);
            }
            RegisterPage.this.setVisible(false);
        }
    }
    private boolean isValidDate(int day, int month, int year) {
        int[] daysInMonth = {31, isLeapYear(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return day >= 1 && day <= daysInMonth[month - 1];
    }
    private boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
}