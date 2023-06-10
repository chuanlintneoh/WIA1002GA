import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.border.*;
import java.time.*;
public class ViewAccountPage extends JFrame implements Page,ActionListener{
    public static void main(String[] args) {
        new ViewAccountPage("vinnieying",1,new TracebackFunction());//view friend account
//        new ViewAccountPage("vinnieying",0);//view self account
    }
    private final JLabel lblName, txtName, lblUsername, txtUsername,lblEmail, txtEmail, lblContactNo, txtContactNo, lblDOB, txtDOB, lblGender, txtGender, lblHobbies, txtHobbies, lblJobHistory, lblAddress, txtAge, lblProfilePicture, txtNoOfFriends;
    private final JTextArea txtJobHistory,txtAddress;
    private JButton btnHome, btnEditAcc, btnStatus, btnBack;
    private final Database database;
    private final int userID;// viewing account's ID
    private final int friendID;// friend's account ID
    private final String username;// current user's username
    private final TracebackFunction tracebackFunction;
    public ViewAccountPage(String username, int friendID, TracebackFunction tracebackFunction) {
        super("View Account Page");
        this.username = username;
        database = new Database();
        this.tracebackFunction = tracebackFunction;
        this.friendID = friendID;
        if (friendID == 0) {// viewing own account
            this.userID = database.getUserId(username);
        }
        else {// viewing friend's account
            this.userID = friendID;
        }

        // Initialize GUI components
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
        if (profilePictureData != null){
            ImageIcon profilePicture = new ImageIcon(profilePictureData);
            lblProfilePicture.setIcon(profilePicture);
        }

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
        btnHome.addActionListener(this);
        btnEditAcc = new JButton("Edit Account");
        btnEditAcc.addActionListener(this);
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);

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
        panel.add(lblDOB, gbc);
        gbc.gridx = 1;
        panel.add(txtDOB, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(txtAge, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblGender, gbc);
        gbc.gridx = 1;
        panel.add(txtGender,gbc);
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(txtNoOfFriends, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(lblAddress, gbc);
        gbc.gridx = 1;
        panel.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(lblJobHistory, gbc);
        gbc.gridx = 1;
        panel.add(txtJobHistory,gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(lblHobbies, gbc);
        gbc.gridx = 1;
        panel.add(txtHobbies,gbc);

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
                    } else if(btnStatus.getText().equals("Friend")){
                        btnStatus.setBackground(new Color(0,204,0));
                        btnStatus.setForeground(Color.WHITE);
                    }
                    btnStatus.addActionListener(this);
                    panel.add(btnStatus,gbc);
                    friendFound = true;
                    break;
                }
            }
            if (!friendFound) {
                btnStatus = new JButton("Add Friend");
                btnStatus.setForeground(Color.WHITE);
                btnStatus.setBackground(new Color(0,102,204));
                btnStatus.addActionListener(this);
                panel.add(btnStatus, gbc);
            }
        }
        else {
            panel.add(btnEditAcc,gbc);
        }

        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnHome,gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnBack,gbc);

        lblProfilePicture.setPreferredSize(new Dimension(150,180));
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 6;
        panel.add(lblProfilePicture, gbc);

        // Set frame properties
        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
    public void showPage(){
        new ViewAccountPage(username,friendID,tracebackFunction);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditAcc){
            tracebackFunction.pushPage(new EditAccountPage(username,tracebackFunction));
            dispose();
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
                JOptionPane.showMessageDialog(this, "Your friend request is sent!.", "Success", JOptionPane.INFORMATION_MESSAGE);
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