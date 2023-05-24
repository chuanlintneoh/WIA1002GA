import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditAccountPage extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new EditAccountPage("chuanlin.tn");
    }
    private final JLabel lblUsername, txtUsername, lblName, lblEmail, lblContactNo, lblDOB, txtDOB, lblGender, txtGender, lblJobHistory, lblHobbies, lblAddress;
    private final JTextField txtName, txtEmail, txtContactNo, txtAddress;
    private final JButton btnEditPassword, btnSaveChanges, btnEditBday, btnEditGender,btnAddHobby, btnAddJob;
    private final Database database;
    private final int userID;
    public EditAccountPage(String username) {
        super("Edit Account Page");
        database = new Database();
        this.userID = database.getUserId(username);

        // Initialize GUI components
        lblUsername = new JLabel("Username:");
        txtUsername = new JLabel(database.get("username", userID));
        lblName = new JLabel("Name:");
        lblEmail = new JLabel("Email address:");
        lblContactNo = new JLabel("Contact No.:");
        lblDOB = new JLabel("Date Of Birth:");
        txtDOB = new JLabel(database.get("birthdate", userID));
        lblGender = new JLabel("Gender:");
        txtGender = new JLabel(database.get("gender",userID));
        lblJobHistory = new JLabel("Job History:");
        lblHobbies = new JLabel("Hobbies:");
        lblAddress = new JLabel("Address:");

        txtName = new JTextField(database.get("name", userID),20);
        txtEmail = new JTextField(database.get("email_address", userID), 20);
        txtContactNo = new JTextField(database.get("contact_no", userID), 20);
        txtAddress = new JTextField(database.get("address",userID),20);

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

        // Add components to the frame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblName, gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);

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
        panel.add(lblAddress, gbc);

        gbc.gridx = 1;
        panel.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        panel.add(btnEditPassword, gbc);

        gbc.gridy = 11;
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
            String name = txtName.getText();
            String contactNo = txtContactNo.getText();
            String address = txtAddress.getText();
            database.set("email_address",email,userID);
            database.set("name",name,userID);
            database.set("contact_no",contactNo,userID);
            database.set("address",address,userID);
            // Save changes made in the EditAccountPage window
            JOptionPane.showMessageDialog(this, "*ALL CHANGES ARE SAVED*", "Selections", JOptionPane.INFORMATION_MESSAGE);
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
}