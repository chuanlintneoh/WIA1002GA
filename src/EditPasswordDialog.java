import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class EditPasswordDialog extends JDialog implements ActionListener {
    private final JPasswordField txtPassword, txtNewPassword, txtConfirmNewPassword;
    private final JButton btnSave, btnCancel;
    private final JLabel lblErrorMessage;
    private final int userID;
    private final Database database;
    private final TracebackFunction tracebackFunction;
    public EditPasswordDialog(Frame parent, int userID, TracebackFunction tracebackFunction) {
        super(parent, "Edit Password", true);
        this.userID = userID;
        this.database = new Database();
        this.tracebackFunction = tracebackFunction;

        // Initialize components
        txtPassword = new JPasswordField(15);
        txtNewPassword = new JPasswordField(15);
        txtConfirmNewPassword = new JPasswordField(15);
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");

        // Create layout and add components
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialogPanel.setBackground(new Color(216, 191, 216));

        // Current Password label and text field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(new JLabel("Current Password:"));
        passwordPanel.add(txtPassword);
        dialogPanel.add(passwordPanel);

        // New Password label and text field
        JPanel newPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        newPasswordPanel.add(new JLabel("New Password:"));
        newPasswordPanel.add(txtNewPassword);
        dialogPanel.add(newPasswordPanel);

        // Confirm New Password label and text field
        JPanel confirmNewPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirmNewPasswordPanel.add(new JLabel("Confirm New Password:"));
        confirmNewPasswordPanel.add(txtConfirmNewPassword);
        dialogPanel.add(confirmNewPasswordPanel);

        // Save button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialogPanel.add(buttonPanel);

        btnCancel.setBackground(new Color(200, 7, 14));
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add error message label
        lblErrorMessage = new JLabel();
        lblErrorMessage.setForeground(Color.RED);
        dialogPanel.add(lblErrorMessage);

        // Set dialog properties
        setContentPane(dialogPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        applyDialogStyles();

        // Add action listeners to buttons
        btnSave.addActionListener(this);
        btnCancel.addActionListener(this);

        // Add tooltips
        txtPassword.setToolTipText("Enter your current password");
        txtNewPassword.setToolTipText("Enter your new password. Your password should contains at least one special character.");
        txtConfirmNewPassword.setToolTipText("Confirm your new password");
        // Enable immediate display of tooltips
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
    }
    private boolean isPasswordValid(char[] password) {
        String passwordString = new String(password);
        String currentPassword = new String(txtPassword.getPassword());
        String specialCharacters = "!@#$%^&*()-=_+[]{}|;:,.<>?";
        // Check if new password is different from the current password
        if (passwordString.equals(currentPassword)) {
            return false;
        }
        // Check if new password contains at least one special character
        boolean hasSpecialCharacter = false;
        for (char c : password) {
            if (specialCharacters.contains(String.valueOf(c))) {
                hasSpecialCharacter = true;
                break;
            }
        }
        return hasSpecialCharacter;
    }
    private void applyDialogStyles() {
        // Apply styling
        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font buttonFont = new Font("Arial", Font.PLAIN, 12);
        Color buttonColor = new Color(46, 138, 87);

        // Set fonts
        txtPassword.setFont(labelFont);
        txtNewPassword.setFont(labelFont);
        txtConfirmNewPassword.setFont(labelFont);
        btnSave.setFont(buttonFont);
        // Set button color
        btnSave.setBackground(buttonColor);
        btnSave.setForeground(Color.WHITE);
        btnCancel.setForeground(Color.WHITE);
    }
    private boolean isEmpty(char[] password) {
        return password.length == 0;
    }
    private boolean isPasswordMatch(char[] password1, char[] password2) {
        return new String(password1).equals(new String(password2));
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCancel) {
            JOptionPane.showMessageDialog(this, "Your password is NOT changed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else if (e.getSource() == btnSave) {
            char[] password = txtPassword.getPassword();
            char[] newPassword = txtNewPassword.getPassword();
            char[] confirmNewPassword = txtConfirmNewPassword.getPassword();
            String hashpassword = PasswordHashing.hashPassword(new String(txtPassword.getPassword()));
            if (!hashpassword.equals(database.get("password",userID))){
                lblErrorMessage.setText("Your current password is wrong!");
            } else if (isEmpty(password) || isEmpty(newPassword) || isEmpty(confirmNewPassword)) {
                lblErrorMessage.setText("Please fill in all fields.");
            } else if (!isPasswordValid(newPassword)) {
                lblErrorMessage.setText("Your new password is not valid.");
            } else if (isPasswordMatch(newPassword, confirmNewPassword)) {
                JOptionPane.showMessageDialog(this, "Your password is changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                database.set("password",hashpassword,userID);
                tracebackFunction.addHistory("Changed account password.");
                dispose();
            } else {
                // Passwords don't match, display error message
                lblErrorMessage.setText("Passwords do not match.");
            }
        }
    }
}