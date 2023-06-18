import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminVerificationDialog extends JDialog implements ActionListener {
    private final JPasswordField txtAdminPassword;
    private final JButton btnVerify;
    private final int userId;
    private final Database database;
    public AdminVerificationDialog(Frame parent, int userId) {
        super(parent, "Admin Verification", true);
        this.userId = userId;
        this.database = new Database();

        // Initialize components
        txtAdminPassword = new JPasswordField(15);
        btnVerify = new JButton("Verify");
        btnVerify.setBackground(new Color(150,75,140));
        btnVerify.setForeground(Color.white);
        btnVerify.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnVerify.setBackground(new Color(80,50,80));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnVerify.setBackground(new Color(150,75,140));
            }
        });

        // Create layout and add components
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialogPanel.setBackground(new Color(216,191,216));

        dialogPanel.add(new JLabel("Enter Admin Password:"));
        dialogPanel.add(txtAdminPassword);
        dialogPanel.add(btnVerify);

        // Set dialog properties
        setContentPane(dialogPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(parent);

        // Add action listener to verify button
        btnVerify.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnVerify) {
            String adminPassword = new String(txtAdminPassword.getPassword());
            String hashedPassword = PasswordHashing.hashPassword(adminPassword);
            if (database.verifyAdmin(userId, hashedPassword)) {
                JOptionPane.showMessageDialog(this, "Admin verification successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                // Proceed with the desired action after successful admin verification
                // For example, you can open another dialog or perform some administrative task.
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect admin password.", "Error", JOptionPane.ERROR_MESSAGE);
                txtAdminPassword.setText(""); // Clear the password field
            }
        }
    }
}
