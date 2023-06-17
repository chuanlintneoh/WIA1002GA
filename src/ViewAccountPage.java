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
import static java.awt.Color.*;
public class ViewAccountPage extends JFrame implements Page,ActionListener {
    private final JLabel forestbook, lblFriendReq, lblFriend, lblName, txtName, lblUsername, txtUsername, lblEmail, txtEmail, lblContactNo, txtContactNo, lblDOB, txtDOB, lblGender, txtGender, lblHobbies, txtHobbies, lblJobHistory, lblAddress, txtAge, lblProfilePicture, txtNoOfFriends;
    private final JTextArea txtJobHistory, txtAddress;
    private JButton btnHome, btnNoti, btnUser, btnEditAcc, btnStatus, btnBack;
    private final Database database;
    private final int userID;// current account's ID
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
        this.userID = database.getUserId(username);

        // Initialize GUI components
        forestbook = new JLabel("ForestBook");
        lblFriendReq = new JLabel("My Friend Requests: " + database.getNumberOfFriendRequests(userID));
        lblUsername = new JLabel("Username:");
        lblName = new JLabel("Name:");
        lblEmail = new JLabel("Email address:");
        lblContactNo = new JLabel("Contact No.:");
        lblAddress = new JLabel("Address:");
        lblProfilePicture = new JLabel(new ImageIcon("src/default_profile_pic.jpg"));
        Border border = LineBorder.createBlackLineBorder();
        lblProfilePicture.setBorder(border);
        byte[] profilePictureData;
        LocalDate birthDate;
        Stack<Job> jobs;
        List<String> hobbies;
        if (friendID == 0){
            lblFriend = new JLabel(database.get("name",userID) + "'s Friends: " + database.getNumberOfFriends(userID));
            txtUsername = new JLabel(database.get("username", userID));
            txtName = new JLabel(database.get("name", userID));
            txtEmail = new JLabel(database.get("email_address", userID));
            txtContactNo = new JLabel(database.get("contact_no", userID));
            profilePictureData = database.getProfilePicture(userID);
            txtAddress = new JTextArea(database.get("address",userID));
            txtDOB = new JLabel(database.get("birthdate", userID));
            birthDate = LocalDate.parse(database.get("birthdate",userID));
            txtGender = new JLabel(database.get("gender",userID));
            txtNoOfFriends = new JLabel("No. of friends:    " + database.getNumberOfFriends(userID) + "          ");
            jobs = database.viewUserJobs(userID);
            hobbies = database.viewUserHobbies(userID);
        }// view own account
        else {
            lblFriend = new JLabel(database.get("name",friendID) + "'s Friends: " + database.getNumberOfFriends(friendID));
            txtUsername = new JLabel(database.get("username", friendID));
            txtName = new JLabel(database.get("name", friendID));
            txtEmail = new JLabel(database.get("email_address", friendID));
            txtContactNo = new JLabel(database.get("contact_no", friendID));
            profilePictureData = database.getProfilePicture(friendID);
            txtAddress = new JTextArea(database.get("address",friendID));
            txtDOB = new JLabel(database.get("birthdate", friendID));
            birthDate = LocalDate.parse(database.get("birthdate",friendID));
            txtGender = new JLabel(database.get("gender",friendID));
            txtNoOfFriends = new JLabel("No. of friends:    " + database.getNumberOfFriends(friendID) + "          ");
            jobs = database.viewUserJobs(friendID);
            hobbies = database.viewUserHobbies(friendID);
        }// view friend's account
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

        txtAddress.setEditable(false);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setPreferredSize(new Dimension(200, 50));

        lblDOB = new JLabel("Date Of Birth:");
        int age = calculateAge(birthDate);
        txtAge = new JLabel("Age:    " + age + "          ");
        lblGender = new JLabel("Gender:");

        lblJobHistory = new JLabel("Job History:");
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
        btnEditAcc.addActionListener(this);
        btnEditAcc.setBackground(new Color(58,30,0));
        btnEditAcc.setForeground(white);
        btnBack = new JButton("Back");
        btnBack.setBackground(new Color(196, 164, 132));
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
        centerPanel.setBackground(new Color(255,240,211));
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
            ArrayList<Friend> friendList = (ArrayList<Friend>) database.viewFriendsRequests(userID);
            boolean friendFound = false;
            for (Friend friend : friendList){
                if(friend.getUserId() == friendID){
                    btnStatus = new JButton(friend.getStatus());
                    if(btnStatus.getText().equals("Friend request sent")){
                        btnStatus.setBackground(new Color(0,0,102));
                        btnStatus.setForeground(Color.WHITE);
                    } else if (btnStatus.getText().equals("Received friend request")) {
                        btnStatus.setBackground(new Color(255,255,153));
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
        }// other user's account - btnStatus
        else {
            centerPanel.add(btnEditAcc,gbc);
        }// own account - enable edit account feature

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
            centerPanel.add(btnSend,gbc);
            ActionListener buttonActionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == btnSend){
                        SendMessageDialog dialog = new SendMessageDialog(ViewAccountPage.this,userID,friendID);
                        dialog.setVisible(true);
                    }
                }
            };
            btnSend.addActionListener(buttonActionListener);
        }

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
        if (friendID == 0){
            eastPanel.add(new FriendsProfilePicturePanel(this,userID,true,tracebackFunction),eastGBC);
        }// own account
        else {
            eastPanel.add(new FriendsProfilePicturePanel(this,friendID,userID,true,tracebackFunction),eastGBC);
        }// stranger account
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
    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
    public void showPage(){
        new ViewAccountPage(username,friendID,tracebackFunction);
    }
    public String getTitle(){
        if (friendID == 0){
            return "View Account Page (" + database.get("username",userID) + ")";
        }
        else {
            return "View Account Page (" + database.get("username",friendID) + ")";
        }
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
                database.insertStatus(userID, friendID, 1);// send request
                database.insertStatus(friendID, userID, 2);// receive request
                JOptionPane.showMessageDialog(this, "Your friend request is sent!", "Success", JOptionPane.INFORMATION_MESSAGE);
                btnStatus.setText("Friend request sent");
                btnStatus.setBackground(new Color(0,0,102));
                btnStatus.setForeground(Color.white);
            }
            else if (btnStatus.getText().equals("Friend request sent")) {
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel your friend request?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    database.removeStatus(userID,friendID);// delete request
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
                    database.updateStatus(userID,friendID,3);// Update status to friends
                    JOptionPane.showMessageDialog(this, "You two are now friends!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    btnStatus.setText("Friend");
                    btnStatus.setBackground(new Color(0,204,0));
                    btnStatus.setForeground(Color.WHITE);
                }
                else if (response == 1){//delete
                    database.removeStatus(userID,friendID);
                    JOptionPane.showMessageDialog(this, "You deleted the friend request...", "Success", JOptionPane.INFORMATION_MESSAGE);
                    btnStatus.setText("Add Friend");
                    btnStatus.setBackground(new Color(0,102,204));
                    btnStatus.setForeground(Color.WHITE);
                }
            }
            else if (btnStatus.getText().equals("Friend")){
                int response = JOptionPane.showConfirmDialog(this, "UNFRIEND?", "Confirm", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null);
                if(response == JOptionPane.YES_OPTION) {
                    database.removeStatus(userID,friendID);
                    btnStatus.setText("Add Friend");
                    btnStatus.setBackground(new Color(0,102,204));
                    btnStatus.setForeground(Color.white);
                }
            }
        }
    }
}