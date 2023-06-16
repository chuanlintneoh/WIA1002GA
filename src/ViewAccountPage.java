import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.border.*;
import java.time.*;

import static java.awt.Color.WHITE;
import static java.awt.Color.white;

public class ViewAccountPage extends JFrame implements Page,ActionListener {
    private final JLabel forestbook,lblFriendReq, lblFriend,lblName, txtName, lblUsername, txtUsername,lblEmail, txtEmail, lblContactNo, txtContactNo, lblDOB, txtDOB, lblGender, txtGender, lblHobbies, txtHobbies, lblJobHistory, lblAddress, txtAge, lblProfilePicture, txtNoOfFriends;
    private final JTextArea txtJobHistory, txtAddress;
    private JButton btnHome, btnNoti, btnUser,btnEditAcc, btnStatus, btnBack;
    private final Database database;
    private final int userID;// viewing account's ID
    private final int friendID;// friend's account ID
    private final String username;// current user's username
    private final TracebackFunction tracebackFunction;
    private final int maxWidth = 150;
    private final int maxHeight = 180;
    public ViewAccountPage(String username, int friendID, TracebackFunction tracebackFunction) {
        this.username = username;
        this.database = new Database();
        this.tracebackFunction = tracebackFunction;
        this.friendID = friendID;
        if (friendID == 0) {// viewing own account
            this.userID = database.getUserId(username);
        }
        else {// viewing friend's account
            this.userID = friendID;
        }

        // Initialize GUI components
        forestbook = new JLabel("ForestBook");
        lblFriendReq = new JLabel("My Friend Request(s): "+database.getNumberOfFriendRequests(database.getUserId(username)));
        lblFriend = new JLabel(database.get("name",userID)+"'s Friend(s): "+database.getNumberOfFriends(userID));
        lblUsername = new JLabel("Username:");
        txtUsername = new JLabel(database.get("username", userID));
        txtName = new JLabel(database.get("name", userID));
        lblName = new JLabel("Name:");
        lblEmail = new JLabel("Email address:");
        txtEmail = new JLabel(database.get("email_address", userID));
        lblContactNo = new JLabel("Contact No.:");
        txtContactNo = new JLabel(database.get("contact_no", userID));
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

        txtAddress = new JTextArea(database.get("address",userID));
        txtAddress.setEditable(false);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setPreferredSize(new Dimension(200, 50));

        lblDOB = new JLabel("Date Of Birth:");
        txtDOB = new JLabel(database.get("birthdate", userID));
        LocalDate birthDate = LocalDate.parse(database.get("birthdate",userID));
        int age = calculateAge(birthDate);
        txtAge = new JLabel("Age:    " + age + "          ");
        lblGender = new JLabel("Gender:");
        txtGender = new JLabel(database.get("gender",userID));
        txtNoOfFriends = new JLabel("No. of friends:    " + database.getNumberOfFriends(userID) + "          ");

        lblJobHistory = new JLabel("Job History:");
        Stack<Job> jobs = database.viewUserJobs(userID);
        StringBuilder jobList = new StringBuilder();
        for (int i = 0; i < jobs.size(); i++) {
            jobList.append(jobs.get(i).getJobName()).append(" (");
            if (jobs.get(i).getStartDate() != null) {
                jobList.append(jobs.get(i).getStartDate().substring(0, 4));
            } else {
                jobList.append("not set");
            }
            jobList.append(" - ");
            if (jobs.get(i).getEndDate() != null) {
                jobList.append(jobs.get(i).getEndDate().substring(0, 4));
            } else {
                jobList.append("not set");
            }
            jobList.append(")");
            if (i != jobs.size() - 1) {
                jobList.append("\n");
            }
        }
        txtJobHistory = new JTextArea(jobList.toString());
        txtJobHistory.setEditable(false);
        txtJobHistory.setBackground(SystemColor.window);

        lblHobbies = new JLabel("Hobbies:");
        List<String> hobbies = database.viewUserHobbies(userID);
        StringBuilder hobbyList = new StringBuilder();
        int hobbiesCount = Math.min(hobbies.size(), 3);
        for (int i = 0; i < hobbiesCount; i++) {
            hobbyList.append(hobbies.get(i));
            if (i != hobbiesCount - 1) {
                hobbyList.append(", ");
            }
        }
        if (hobbies.size() >3) {
            hobbyList.append("...");
        }
        txtHobbies = new JLabel(hobbyList.toString());

        btnHome = new JButton("Home");
        btnHome.setBackground(new Color(0, 128, 0));
        btnHome.setForeground(Color.white);
        btnHome.addActionListener(this);

        btnNoti = new JButton("Notifications");
        btnNoti.setBackground(new Color(46,138,87));
        btnNoti.setForeground(white);
        btnNoti.addActionListener(this);

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
        btnUser.addActionListener(e -> new AccountMenu(username,tracebackFunction,this).show(btnUser, -5, btnUser.getHeight()));

        btnEditAcc = new JButton("Edit Account");
        btnEditAcc.setBackground(new Color(58,30,0));
        btnEditAcc.setForeground(white);
        btnEditAcc.addActionListener(this);
        btnBack = new JButton("Back");
        btnBack.setBackground(new Color(92, 94, 41));
        btnBack.setForeground(white);
        btnBack.addActionListener(this);

        // Add components to the frame
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel();
        forestbook.setFont(new Font("Curlz MT", Font.BOLD, 42));
        forestbook.setForeground(new Color(0, 128, 0));
        topPanel.add(forestbook);
        topPanel.setBackground(new Color(180, 238, 156));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        centerPanel.setBackground(new Color(200, 238, 156));
        centerPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(110, 90, 50)));
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
        centerPanel.add(lblDOB, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtDOB, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(txtAge, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lblGender, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtGender,gbc);
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(txtNoOfFriends, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(lblAddress, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        centerPanel.add(lblJobHistory, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtJobHistory,gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        centerPanel.add(lblHobbies, gbc);
        gbc.gridx = 1;
        centerPanel.add(txtHobbies,gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 2;
        gbc.gridy = 9;
        if (friendID != 0){
            ArrayList<Friend> friendList = (ArrayList<Friend>) database.viewFriendsRequests(database.getUserId(username));
            boolean friendFound = false;
            for (Friend friend : friendList){
                if(friend.getUserId() == friendID){
                    btnStatus = new JButton(friend.getStatus());
                    if(btnStatus.getText().equals("Friend request sent")){
                        btnStatus.setBackground(new Color(0,0,102));
                        btnStatus.setForeground(Color.WHITE);
                    } else if (btnStatus.getText().equals("Received friend request")) {
                        btnStatus.setBackground(new Color(255,255,153));
                        btnStatus.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                btnStatus.setForeground(Color.WHITE); // Change to the desired color
                                btnStatus.setBackground(new Color(155, 155, 53));
                            }
                            @Override
                            public void mouseExited(MouseEvent e) {
                                btnStatus.setForeground(Color.black); // Change back to the default color
                                btnStatus.setBackground(new Color(255, 255, 153));
                            }
                        });
                    } else if(btnStatus.getText().equals("Friend")){
                        btnStatus.setBackground(new Color(0,204,0));
                        btnStatus.setForeground(Color.WHITE);
                    }
                    btnStatus.addActionListener(this);
                    centerPanel.add(btnStatus,gbc);
                    friendFound = true;
                    break;
                }
            }
            if (!friendFound) {
                btnStatus = new JButton("Add Friend");
                btnStatus.setForeground(Color.WHITE);
                btnStatus.setBackground(new Color(0,102,204));
                btnStatus.addActionListener(this);
                centerPanel.add(btnStatus, gbc);
            }
        }
        else {
            centerPanel.add(btnEditAcc,gbc);
        }

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(btnHome,gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(btnBack,gbc);

        lblProfilePicture.setPreferredSize(new Dimension(150,180));
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        centerPanel.add(lblProfilePicture, gbc);

        if (friendID != 0){
            gbc.gridy = 7;
            gbc.gridheight = 1;
            JButton btnSend = new JButton("Send Message");
            Border lineBorder = BorderFactory.createLineBorder(new Color(0, 100, 0), 2);
            Insets spacingInsets = new Insets(5, 10, 5, 10);
            Border spacingBorder = BorderFactory.createEmptyBorder(spacingInsets.top, spacingInsets.left, spacingInsets.bottom, spacingInsets.right);
            Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, spacingBorder);
            btnSend.setBackground(new Color(166, 220, 156));
            btnSend.setForeground(new Color(0,100,0));
            btnSend.setBorder(compoundBorder);

            centerPanel.add(btnSend,gbc);
            ActionListener buttonActionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == btnSend){
                        SendMessageDialog dialog = new SendMessageDialog(ViewAccountPage.this,database.getUserId(username),friendID);
                        dialog.setVisible(true);
                    }
                }
            };
            btnSend.addActionListener(buttonActionListener);
            btnSend.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btnSend.setForeground(WHITE);
                    btnSend.setBackground(new Color(0,100,0));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    btnSend.setForeground(new Color(0,100,0));
                    btnSend.setBackground(new Color(166, 220, 156));
                }
            });
        }
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints westGBC = new GridBagConstraints();
        westGBC.insets = new Insets(10,10,10,10);
        westGBC.anchor = GridBagConstraints.CENTER;
        westGBC.gridx = 0;
        westGBC.gridy = 0;
        westPanel.add(lblFriendReq,westGBC);
        westGBC.gridy = 1;
        westPanel.add(new FriendsProfilePicturePanel(this,database.getUserId(username),false,tracebackFunction),westGBC);
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
        bottomPanel.add(btnHome);
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
    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
    public void showPage(){
        new ViewAccountPage(username,friendID,tracebackFunction);
    }

    public String getTitle(){
        return "View Account Page (" + database.get("username",userID) + ")";
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditAcc){
            tracebackFunction.pushPage(new EditAccountPage(username,tracebackFunction));
            dispose();
        }
        else if (e.getSource() == btnNoti){
            NotificationScrollPane scrollPane = new NotificationScrollPane(userID);
            scrollPane.show(btnNoti, 0, btnNoti.getHeight());
        }
        else if (e.getSource() == btnHome){
            tracebackFunction.pushPage(new HomePage(username,tracebackFunction));
            dispose();
        }
        else if (e.getSource() == btnBack){
            tracebackFunction.popPeek();
            dispose();
        }
        else if (e.getSource() == btnStatus){
            if (btnStatus.getText().equals("Add Friend")){
                database.insertStatus(database.getUserId(username), userID, 1);// send request
                database.insertStatus(userID, database.getUserId(username), 2);// receive request
                JOptionPane.showMessageDialog(this, "Your friend request is sent!", "Success", JOptionPane.INFORMATION_MESSAGE);
                btnStatus.setText("Friend request sent");
                btnStatus.setBackground(new Color(0,0,102));
                btnStatus.setForeground(Color.white);
            }
            else if (btnStatus.getText().equals("Friend request sent")) {
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel your friend request?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    database.removeStatus(database.getUserId(username),userID);// delete request
                    JOptionPane.showMessageDialog(this, "Friend request cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    btnStatus.setText("Add Friend");
                    btnStatus.setBackground(new Color(0,102,204));
                    btnStatus.setForeground(Color.white);
                }
            }
            else if (btnStatus.getText().equals("Received friend request")){
                Object[] options = {"Confirm", "Delete"};
                int response = JOptionPane.showOptionDialog(this, "Confirm friend request?", "Confirm",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (response == 0){//confirm
                    database.updateStatus(database.getUserId(username),userID,3);// Update status to friends
                    JOptionPane.showMessageDialog(this, "You two are now friends!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    btnStatus.setText("Friend");
                    btnStatus.setBackground(new Color(0,204,0));
                    btnStatus.setForeground(Color.WHITE);
                }
                else if (response == 1){//delete
                    database.removeStatus(database.getUserId(username),userID);
                    JOptionPane.showMessageDialog(this, "You deleted the friend request...", "Success", JOptionPane.INFORMATION_MESSAGE);
                    btnStatus.setText("Add Friend");
                    btnStatus.setBackground(new Color(0,102,204));
                    btnStatus.setForeground(Color.WHITE);
                }
            }
            else if (btnStatus.getText().equals("Friend")){
                int response = JOptionPane.showConfirmDialog(this, "UNFRIEND?", "Confirm", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null);
                if(response == JOptionPane.YES_OPTION) {
                    database.removeStatus(database.getUserId(username),userID);
                    btnStatus.setText("Add Friend");
                    btnStatus.setBackground(new Color(0,102,204));
                    btnStatus.setForeground(Color.white);
                }
            }
        }
    }
}