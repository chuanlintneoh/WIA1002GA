import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class EditAccountPage extends JFrame implements ActionListener {
    private final JLabel lblUsername, txtUsername, lblName, lblEmail, lblContactNo, lblDOB, txtDOB, lblGender, txtGender, lblJobHistory, lblHobbies, lblAddress, lblProfilePicture;
    private final JTextField txtName, txtEmail, txtContactNo, txtAddress;
    private final JButton btnEditPic, btnEditPassword, btnEditBday, btnEditGender, btnAddJob, btnAddHobby, btnSaveChanges, btnCancel, btnAdmin;
    private final Database database;
    private final int userID;
    private final String username;
    public EditAccountPage(String username) {
        super("Edit Account Page");
        this.username = username;
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
        lblProfilePicture = new JLabel(new ImageIcon("src/default_profile_pic.jpg"));
        Border border = LineBorder.createBlackLineBorder();
        lblProfilePicture.setBorder(border);
        byte[] profilePictureData = database.getProfilePicture(userID);
        if (profilePictureData != null){
            ImageIcon profilePicture = new ImageIcon(profilePictureData);
            lblProfilePicture.setIcon(profilePicture);
        }

        txtName = new JTextField(database.get("name", userID),20);
        txtEmail = new JTextField(database.get("email_address", userID), 20);
        txtContactNo = new JTextField(database.get("contact_no", userID), 20);
        txtAddress = new JTextField(database.get("address",userID),20);

        btnEditPic = new JButton("Edit Profile Picture");
        btnEditPic.addActionListener(this);
        btnEditBday = new JButton(database.get("birthdate",userID));
//        btnEditBday.setBorderPainted(false);
//        btnEditBday.setContentAreaFilled(false);
//        btnEditBday.setFont(btnEditBday.getFont().deriveFont(Font.PLAIN));
//        btnEditBday.setForeground(Color.BLUE);
        btnEditBday.addActionListener(this);
        btnEditGender = new JButton(database.get("gender",userID));
//        btnEditGender.setBorderPainted(false);
//        btnEditGender.setContentAreaFilled(false);
//        btnEditGender.setForeground(Color.BLUE);
//        btnEditGender.setFont(btnEditGender.getFont().deriveFont(Font.PLAIN));
        btnEditGender.addActionListener(this);
        btnAddJob = new JButton("Add/Edit Job");
        btnAddJob.addActionListener(this);
        btnAddHobby = new JButton("Add/Edit Hobby");
        btnAddHobby.addActionListener(this);
        btnEditPassword = new JButton("Edit Password");
        btnEditPassword.setBackground(Color.black);
        btnEditPassword.setForeground(Color.white);
        btnEditPassword.addActionListener(this);
        if (database.isAdmin(userID)){
            btnAdmin = new JButton("Administrator Control");
        }
        else {
            btnAdmin = new JButton("Verify as Admin");
        }
        btnAdmin.addActionListener(this);
        btnSaveChanges = new JButton("Save Changes");
        btnSaveChanges.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);

        // Add components to the frame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

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

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblContactNo, gbc);
        gbc.gridx = 1;
        panel.add(txtContactNo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblAddress, gbc);
        gbc.gridx = 1;
        panel.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lblDOB, gbc);
        gbc.gridx = 1;
//        panel.add(txtDOB, gbc);
//        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnEditBday, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblGender, gbc);
        gbc.gridx = 1;
//        panel.add(txtGender,gbc);
//        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnEditGender,gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblJobHistory, gbc);
        gbc.gridx = 1;
        panel.add(btnAddJob, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(lblHobbies, gbc);
        gbc.gridx = 1;
        panel.add(btnAddHobby,gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        panel.add(btnEditPassword, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        panel.add(btnAdmin, gbc);
        btnAdmin.setForeground(Color.white);
        btnAdmin.setBackground(new Color(200,0,127));

        JPanel closePanel = new JPanel();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        closePanel.add(btnSaveChanges);
        btnSaveChanges.setForeground(Color.WHITE);
        btnSaveChanges.setBackground(new Color(46, 138, 87));
        closePanel.add(btnCancel);
        panel.add(closePanel, gbc);

        lblProfilePicture.setPreferredSize(new Dimension(150,200));
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        panel.add(lblProfilePicture, gbc);
        gbc.gridy = 5;
        panel.add(btnEditPic, gbc);

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
            new ViewAccountPage(username);
            dispose();
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
        else if (e.getSource() == btnAdmin){
            if (database.isAdmin(userID)){
                //Administrator Page
            }
            else {
                AdminVerificationDialog dialog = new AdminVerificationDialog(this,userID);
                dialog.setVisible(true);
                if (database.isAdmin(userID)){
                    btnAdmin.setText("Administrator Control");
                }
            }
        }
        else if (e.getSource() == btnEditPic){
            EditPicDialog dialog = new EditPicDialog(this,userID,lblProfilePicture);
            dialog.setVisible(true);
            byte[] profilePictureData = database.getProfilePicture(userID);
            if (profilePictureData != null){
                lblProfilePicture.setIcon(new ImageIcon(profilePictureData));
            }
            else {
                lblProfilePicture.setIcon(new ImageIcon("src/default_profile_pic.jpg"));
            }
        }
        else if (e.getSource() == btnCancel){
            new ViewAccountPage(username);
            dispose();
        }
    }
}