import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class EditAccountPage extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new EditAccountPage();
    }
    private final JLabel lblName, txtName, lblUsername, lblEmail, lblContactNo, lblDOB, txtDOB, lblGender, lblHobbies, lblJobHistory, txtGender;
    private final JTextField txtUsername, txtEmail, txtContactNo;
    private final JButton btnEditPassword, btnSaveChanges, btnEditBday, btnEditGender,btnAddHobby, btnAddJob;
    // Data structures for storing selections
    private ArrayList<String> hobbies; // ArrayList to store selected hobbies
    Database database = new Database();
    private final int userID = database.getUserId("vinnieying");
    public EditAccountPage() {
        super("Edit Account Page");

        System.out.println(userID);

        // Initialize GUI components
        lblName = new JLabel("Name:");
        txtName = new JLabel(database.get("name", userID));
        lblUsername = new JLabel("Username:");
        lblEmail = new JLabel("Email address:");
        lblContactNo = new JLabel("Contact No.:");
        lblDOB = new JLabel("Date Of Birth:");
        txtDOB = new JLabel(database.get("birthdate", userID));
        lblGender = new JLabel("Gender:");
        txtGender = new JLabel(database.get("gender",userID));
        lblJobHistory = new JLabel("Job History:");
        lblHobbies = new JLabel("Hobbies:");

        txtUsername = new JTextField(database.get("username", userID), 20);
        txtEmail = new JTextField(database.get("email_address", userID), 20);
        txtContactNo = new JTextField(database.get("contact_no", userID), 20);

        btnEditBday = new JButton("Edit Birthday");
        btnEditBday.addActionListener(this);
        btnEditGender = new JButton("Edit Gender");
        btnEditGender.addActionListener(this);
        btnAddJob = new JButton("Add/Edit Job");
        btnAddJob.addActionListener(this);
        btnAddHobby = new JButton("Add/Edit Hobby");
        btnAddHobby.addActionListener(this);
        btnEditPassword = new JButton("Edit Password");
        btnEditPassword.addActionListener(this);
        btnSaveChanges = new JButton("Save Changes");
        btnSaveChanges.addActionListener(this);

        // Initialize data structures for storing selections
        hobbies = new ArrayList<>(); // ArrayList to store selected hobbies

        // Add components to the frame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblName, gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);
        txtUsername.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);
        txtEmail.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblContactNo, gbc);
        gbc.gridx = 1;
        panel.add(txtContactNo, gbc);
        txtContactNo.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblDOB, gbc);
        gbc.gridx = 1;
        panel.add(txtDOB, gbc);
        gbc.gridx = 2;
        panel.add(btnEditBday, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lblGender, gbc);
        gbc.gridx = 1;
        panel.add(txtGender,gbc);
        gbc.gridx = 2;
        panel.add(btnEditGender,gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(lblJobHistory, gbc);
        gbc.gridx = 1;
        panel.add(btnAddJob, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(lblHobbies, gbc);

        gbc.gridx = 1;
        panel.add(btnAddHobby,gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        panel.add(btnEditPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        panel.add(btnSaveChanges, gbc);
        btnSaveChanges.setForeground(Color.WHITE);
        btnSaveChanges.setBackground(new Color(46, 138, 87));

        // Set frame properties
        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditPassword) {
            EditPasswordDialog dialog = new EditPasswordDialog(this,userID);
            dialog.setVisible(true);
        }
        else if (e.getSource() == btnSaveChanges) {
            String email = txtEmail.getText();
            String username = txtUsername.getText();
            String contactNo = txtContactNo.getText();
            database.set("email_address",email,userID);
            database.set("username",username,userID);
            database.set("contact_no",contactNo,userID);
            // Save changes made in the EditAccountPage window
            saveChanges();
        }
        else if (e.getSource() == btnEditBday) {
            EditBirthdayDialog dialog = new EditBirthdayDialog(this,userID,txtDOB);
            dialog.setVisible(true);
        }
        else if(e.getSource() == btnEditGender){
            EditGenderDialog dialog = new EditGenderDialog(this,userID,txtGender);
            dialog.setVisible(true);
        }
        else if(e.getSource() == btnAddJob){
            AddJobDialog dialog = new AddJobDialog(this,userID);
            dialog.setVisible(true);
        }
        else if(e.getSource() == btnAddHobby){
            AddHobbyDialog dialog = new AddHobbyDialog(this,userID);
            dialog.setVisible(true);
        }
    }
    private void saveChanges() {
        hobbies.clear();
        // Display the selected hobbies and job history
        StringBuilder sb = new StringBuilder();
        sb.append("Selected Hobbies:\n");
        for (String hobby : hobbies) {
            sb.append(hobby).append("\n");
        }
        sb.append("*ALL CHANGES ARE SAVED*");
        JOptionPane.showMessageDialog(this, sb.toString(), "Selections", JOptionPane.INFORMATION_MESSAGE);
        // Update user account details with edited information
        String newUsername = txtUsername.getText();
        String newEmail = txtEmail.getText();
        String newContactNo = txtContactNo.getText();

        // Example of updating the user account with new information
        // Existing code...

        // Print updated details to console (you can replace this with your logic)
        System.out.println("Updated Account Details:");
        System.out.println("Username: " + newUsername);
        System.out.println("Email: " + newEmail);
        System.out.println("Contact No.: " + newContactNo);

        // Example of displaying selected hobbies (replace with your logic)
        System.out.println("Selected Hobbies:");
        for (String hobby : hobbies) {
            System.out.println(hobby);
        }
    }
}