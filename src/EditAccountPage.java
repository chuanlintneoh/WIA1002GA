import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.Color.*;
public class EditAccountPage extends JFrame implements Page,ActionListener {
    private final JLabel forestbook, lblFriendReq, lblFriend, lblUsername, txtUsername, lblName, lblEmail, lblContactNo, lblDOB, lblGender, lblJobHistory, lblHobbies, lblAddress, lblProfilePicture;
    private final JTextField txtName, txtEmail, txtContactNo, txtAddress;
    private final JButton btnNoti, btnUser, btnEditPic, btnEditPassword, btnEditBday, btnEditGender, btnAddJob, btnAddHobby, btnSaveChanges, btnCancel, btnAdmin, btnBack;
    private final Database database;
    private final int userID;
    private final String username;
    private final TracebackFunction tracebackFunction;
    private final int maxWidth = 150;
    private final int maxHeight = 180;
    public EditAccountPage(String username, TracebackFunction tracebackFunction) {
        this.username = username;
        this.database = new Database();
        this.userID = database.getUserId(username);
        this.tracebackFunction = tracebackFunction;

        // Initialize GUI components
        forestbook = new JLabel("ForestBook");
        lblFriendReq = new JLabel("Friend Requests: " + database.getNumberOfFriendRequests(userID));
        lblFriend = new JLabel("Friends: " + database.getNumberOfFriends(userID));
        lblUsername = new JLabel("Username:");
        txtUsername = new JLabel(database.get("username", userID));
        lblName = new JLabel("Name:");
        lblEmail = new JLabel("Email address:");
        lblContactNo = new JLabel("Contact No.:");
        lblDOB = new JLabel("Date Of Birth:");
        lblGender = new JLabel("Gender:");
        lblJobHistory = new JLabel("Job History:");
        lblHobbies = new JLabel("Hobbies:");
        lblAddress = new JLabel("Address:");
        lblProfilePicture = new JLabel(new ImageIcon("src/default_profile_pic.jpg"));
        Border border = LineBorder.createBlackLineBorder();
        lblProfilePicture.setBorder(border);
        byte[] profilePictureData = database.getProfilePicture(userID);
        ImageIcon profilePicture;
        Image resizedImage;
        if (profilePictureData != null){
            profilePicture = new ImageIcon(profilePictureData);
        }
        else {
            profilePicture = new ImageIcon("src/default_profile_pic.jpg");
        }
        resizedImage = resizeImage(profilePicture.getImage());
        lblProfilePicture.setIcon(new ImageIcon(resizedImage));

        txtName = new JTextField(database.get("name", userID),20);
        txtEmail = new JTextField(database.get("email_address", userID), 20);
        txtContactNo = new JTextField(database.get("contact_no", userID), 20);
        txtAddress = new JTextField(database.get("address",userID),20);

        btnEditPic = new JButton("<html><u>Edit Profile Picture</u></html>");
        btnEditPic.setBorderPainted(false);
        btnEditPic.setContentAreaFilled(false);
        btnEditPic.addActionListener(this);
        btnEditPic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEditPic.setForeground(new Color(144,20,24));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnEditPic.setForeground(black);
            }
        });

        btnEditBday = new JButton(String.format("<html><u>%s</u></html>",database.get("birthdate",userID)));
        btnEditBday.setBorderPainted(false);
        btnEditBday.setContentAreaFilled(false);
        btnEditBday.setFont(btnEditBday.getFont().deriveFont(Font.PLAIN));
        btnEditBday.setForeground(Color.BLUE);
        btnEditBday.addActionListener(this);
        btnEditBday.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        btnEditBday.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEditBday.setForeground(new Color(144,20,24));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnEditBday.setForeground(Color.BLUE);
            }
        });

        btnEditGender = new JButton(String.format("<html><u>%s</u></html>",database.get("gender", userID)));
        btnEditGender.setBorderPainted(false);
        btnEditGender.setContentAreaFilled(false);
        btnEditGender.setForeground(Color.BLUE);
        btnEditGender.setFont(btnEditGender.getFont().deriveFont(Font.PLAIN));
        btnEditGender.addActionListener(this);
        btnEditGender.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        btnEditGender.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEditGender.setForeground(new Color(144,20,24));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnEditGender.setForeground(Color.BLUE);
            }
        });

        btnAddJob = new JButton("<html><u>Add/Edit Job</u></html>");
        btnAddJob.setBorderPainted(false);
        btnAddJob.setContentAreaFilled(false);
        btnAddJob.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        btnAddJob.addActionListener(this);
        btnAddJob.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnAddJob.setForeground(new Color(144,20,24));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnAddJob.setForeground(black);
            }
        });

        btnAddHobby = new JButton("<html><u>Add/Edit Hobby</u></html>");
        btnAddHobby.setBorderPainted(false);
        btnAddHobby.setContentAreaFilled(false);
        btnAddHobby.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        btnAddHobby.addActionListener(this);
        btnAddHobby.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnAddHobby.setForeground(new Color(144,20,24));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnAddHobby.setForeground(black);
            }
        });

        btnEditPassword = new JButton("Edit Password");
        btnEditPassword.setBackground(Color.black);
        btnEditPassword.setForeground(Color.white);
        btnEditPassword.addActionListener(this);
        btnEditPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEditPassword.setForeground(black);
                btnEditPassword.setBackground(new Color(150,150,150));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnEditPassword.setBackground(Color.black);
                btnEditPassword.setForeground(Color.white);
            }
        });
        btnUser = new JButton(username);
        btnUser.setFont(new Font(btnUser.getFont().getName(), Font.BOLD, 16));
        btnUser.setBackground(new Color(180, 238, 156));
        btnUser.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 50));
        btnUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnUser.setForeground(WHITE); // Change to the desired color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnUser.setForeground(new Color(58,30,0)); // Change back to the default color
            }
        });
        btnUser.addActionListener(e -> new AccountMenu(username,tracebackFunction,this).show(btnUser, -56, btnUser.getHeight()));
        btnNoti = new JButton("Notifications");
        btnNoti.setBackground(new Color(46,138,87));
        btnNoti.setForeground(white);
        btnNoti.addActionListener(this);
        if (database.isAdmin(userID)){
            btnAdmin = new JButton("Administrator Control");
        }
        else {
            btnAdmin = new JButton("Verify as Admin");
        }
        btnAdmin.addActionListener(this);
        btnSaveChanges = new JButton("Save");
        btnSaveChanges.setForeground(Color.WHITE);
        btnSaveChanges.setBackground(new Color(46, 138, 87));
        btnSaveChanges.addActionListener(this);
        btnSaveChanges.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSaveChanges.setBackground(new Color(20,75,30));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnSaveChanges.setBackground(new Color(46, 138, 87));
            }
        });

        btnCancel = new JButton("Cancel");
        btnCancel.setForeground(white);
        btnCancel.setBackground(new Color(218,195,80));
        btnCancel.addActionListener(this);
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCancel.setForeground(black);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCancel.setForeground(white);
            }
        });
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);
        btnBack.setBackground(new Color(92, 94, 41));
        btnBack.setForeground(white);
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBack.setBackground(new Color(58,30,0));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnBack.setBackground(new Color(92, 94, 41));
            }
        });

        // Add components to the frame
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel();
        forestbook.setFont(new Font("Curlz MT", Font.BOLD, 42));
        forestbook.setForeground(new Color(0, 128, 0));
        forestbook.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                forestbook.setForeground(new Color(92, 94, 41));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                forestbook.setForeground(new Color(0, 128, 0));
            }
        });
        topPanel.add(forestbook);
        topPanel.setBackground(new Color(180, 238, 156));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        centerPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(110, 90, 50)));
        centerPanel.setBackground(new Color(255,240,211));
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(lblUsername, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(lblName, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(lblEmail, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        centerPanel.add(lblContactNo, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtContactNo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        centerPanel.add(lblAddress, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        centerPanel.add(lblDOB, gbc);
        gbc.gridx = 1;

        centerPanel.add(btnEditBday, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lblGender, gbc);
        gbc.gridx = 1;
        centerPanel.add(btnEditGender,gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lblJobHistory, gbc);
        gbc.gridx = 1;
        centerPanel.add(btnAddJob, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        centerPanel.add(lblHobbies, gbc);
        gbc.gridx = 1;
        centerPanel.add(btnAddHobby,gbc);

        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        centerPanel.add(btnEditPassword, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        centerPanel.add(btnAdmin, gbc);
        btnAdmin.setForeground(Color.white);
        btnAdmin.setBackground(new Color(200,0,127));
        btnAdmin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnAdmin.setBackground(new Color(150,30,130));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnAdmin.setBackground(new Color(200,0,127));
            }
        });

        JPanel closePanel = new JPanel();
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        closePanel.add(btnSaveChanges,gbc);
        closePanel.setBackground(new Color(255,240,211));
        closePanel.add(btnCancel);
        centerPanel.add(closePanel, gbc);

        lblProfilePicture.setPreferredSize(new Dimension(150,180));
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        centerPanel.add(lblProfilePicture, gbc);
        gbc.gridy = 5;
        centerPanel.add(btnEditPic, gbc);

        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints westGBC = new GridBagConstraints();
        westGBC.insets = new Insets(10,10,10,10);
        westGBC.anchor = GridBagConstraints.CENTER;
        westGBC.gridx = 0;
        westGBC.gridy = 0;
        westPanel.add(lblFriendReq,westGBC);
        westGBC.gridy = 1;
        westPanel.add(new FriendsProfilePicturePanel(this,userID,false,tracebackFunction),westGBC);
        westPanel.setBackground(new Color(180, 238, 156));

        JPanel eastPanel = new JPanel(new GridBagLayout());
        GridBagConstraints eastGBC = new GridBagConstraints();
        eastGBC.insets = new Insets(10,10,10,10);
        eastGBC.anchor = GridBagConstraints.CENTER;
        eastGBC.gridx = 0;
        eastGBC.gridy = 0;
        eastPanel.add(btnNoti,eastGBC);
        eastGBC.gridx = 1;
        eastPanel.add(btnUser,eastGBC);
        eastGBC.anchor = GridBagConstraints.CENTER;
        eastGBC.gridx = 0;
        eastGBC.gridy = 1;
        eastGBC.gridwidth = 2;
        eastPanel.add(lblFriend,eastGBC);
        eastGBC.gridy = 2;
        eastPanel.add(new FriendsProfilePicturePanel(this,userID,true,tracebackFunction),eastGBC);
        eastPanel.setBackground(new Color(180, 238, 156));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnBack);
        bottomPanel.setBackground(new Color(180, 238, 156));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(westPanel, BorderLayout.WEST);
        panel.add(eastPanel, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Set frame properties
        getContentPane().add(panel);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setVisible(true);
//        add(panel);
//        pack();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(550, 600);
//        setLocationRelativeTo(null);
//        setVisible(true);
    }
    private Image resizeImage(Image originalImage) {
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);
        double widthRatio = (double) maxWidth / width;
        double heightRatio = (double) maxHeight / height;
        double scaleRatio = Math.max(widthRatio, heightRatio);
        int newWidth = (int) (width * scaleRatio);
        int newHeight = (int) (height * scaleRatio);
        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }
    public void showPage(){
        new EditAccountPage(username,tracebackFunction);
    }
    public String getTitle(){
        return "Edit Account Page";
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditPassword) {
            EditPasswordDialog dialog = new EditPasswordDialog(this,userID,tracebackFunction);
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
            tracebackFunction.addHistory("Edited and saved changes on account.");
            // Save changes made in the EditAccountPage window
            JOptionPane.showMessageDialog(this, "*ALL CHANGES ARE SAVED*", "Selections", JOptionPane.INFORMATION_MESSAGE);
            tracebackFunction.pushPage(new ViewAccountPage(username,0,tracebackFunction));
            dispose();
        }
        else if (e.getSource() == btnEditBday) {
            EditBirthdayDialog dialog = new EditBirthdayDialog(this,userID,btnEditBday,tracebackFunction);
            dialog.setVisible(true);
        }
        else if(e.getSource() == btnEditGender){
            EditGenderDialog dialog = new EditGenderDialog(this,userID,btnEditGender,tracebackFunction);
            dialog.setVisible(true);
        }
        else if(e.getSource() == btnAddJob){
            AddJobDialog dialog = new AddJobDialog(this,userID,tracebackFunction);
            dialog.setVisible(true);
        }
        else if(e.getSource() == btnAddHobby){
            AddHobbyDialog dialog = new AddHobbyDialog(this,userID,tracebackFunction);
            dialog.setVisible(true);
        }
        else if (e.getSource() == btnAdmin){
            if (database.isAdmin(userID)){
                //Administrator Page
                tracebackFunction.pushPage(new AdministratorPage(username,tracebackFunction));
                dispose();
            }
            else {
                AdminVerificationDialog dialog = new AdminVerificationDialog(this,userID);
                dialog.setVisible(true);
                if (database.isAdmin(userID)){
                    tracebackFunction.addHistory("Verified as an admin.");
                    btnAdmin.setText("Administrator Control");
                }
            }
        }
        else if (e.getSource() == btnEditPic){
            EditPicDialog dialog = new EditPicDialog(this,userID,lblProfilePicture,tracebackFunction);
            dialog.setVisible(true);
            tracebackFunction.peek();
            dispose();
        }
        else if (e.getSource() == btnCancel){
            tracebackFunction.pushPage(new ViewAccountPage(username,0,tracebackFunction));
            dispose();
        }
        else if (e.getSource() == btnBack){
            tracebackFunction.popPeek();
            dispose();
        }
        else if(e.getSource() == btnNoti){
            NotificationScrollPane scrollPane = new NotificationScrollPane(userID);
            scrollPane.show(btnNoti, 0, btnNoti.getHeight());
        }
    }
}