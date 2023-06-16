import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import static java.awt.Color.*;
public class HomePage extends JFrame implements Page,ActionListener {
    private final JTextField txtSearch;
    private final JButton btnSearch, btnNoti, btnUser, btnBack;
    private final JLabel forestbook, lblFriendReq, lblFriend, lblSuggestedFriend;
    private final Database database;
    private final int userID;
    private final String username;
    private final TracebackFunction tracebackFunction;
    private final int maxWidth = 150;
    private final int maxHeight = 180;
    public HomePage(String username, TracebackFunction tracebackFunction) {
        this.username = username;
        this.database = new Database();
        this.userID = database.getUserId(username);
        this.tracebackFunction = tracebackFunction;

        forestbook = new JLabel("ForestBook");
        txtSearch = new JTextField(40);
        btnSearch = new JButton("Search");
        btnUser = new JButton(username);
        btnNoti = new JButton("Notifications");
        lblFriendReq = new JLabel("Friend Request(s): " + database.getNumberOfFriendRequests(userID));
        lblFriend = new JLabel("Friend(s): " + database.getNumberOfFriends(userID));
        lblSuggestedFriend = new JLabel("Suggested Friend(s): 0");
        btnBack = new JButton("Back");

        btnBack.setBackground(new Color(92, 94, 41));
        btnBack.setForeground(white);
        btnNoti.setBackground(new Color(46,138,87));
        btnNoti.setForeground(white);
        btnSearch.setBackground(new Color(46,138,87));
        btnSearch.setForeground(white);

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

        btnSearch.addActionListener(this);
        btnNoti.addActionListener(this);
        btnBack.addActionListener(this);

        btnUser.addActionListener(e ->new AccountMenu(username,tracebackFunction,this).show(btnUser,-5,btnUser.getHeight()));
        forestbook.setFont(new Font("Curlz MT", Font.BOLD, 42));
        forestbook.setForeground(new Color(0, 128, 0));

        JPanel mutualFriendsPanel = new JPanel();
        mutualFriendsPanel.setBackground(new Color(180, 238, 156));
        mutualFriendsPanel.setLayout(new BoxLayout(mutualFriendsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(mutualFriendsPanel);
        scrollPane.setPreferredSize(new Dimension(630,460));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100);

        ArrayList<Friend> mutualFriends = (ArrayList<Friend>) database.findMutualFriends(userID);
        int suggestedFriend = mutualFriends.size();
        for (Friend mutualFriend: mutualFriends){
            JPanel framePanel = new JPanel(new BorderLayout());
            framePanel.setBorder(BorderFactory.createLineBorder(white));

            JPanel resultPanel = new JPanel(new GridBagLayout());
            resultPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(110, 90, 50)));
            GridBagConstraints resultGBC = new GridBagConstraints();
            resultPanel.setBackground(new Color(255,240,211));
            byte[] profilePictureData = database.getProfilePicture(mutualFriend.getUserId());
            JLabel lblProfilePicture = new JLabel();
            Border border = LineBorder.createBlackLineBorder();
            lblProfilePicture.setBorder(border);

            if (profilePictureData != null) {
                ImageIcon imageIcon = new ImageIcon(profilePictureData);
                Image image = imageIcon.getImage();
                Image resizedImage = resizeImage(image);
                lblProfilePicture.setIcon(new ImageIcon(resizedImage));
                lblProfilePicture.setPreferredSize(new Dimension(maxWidth, maxHeight));
                lblProfilePicture.setHorizontalAlignment(SwingConstants.CENTER);
                lblProfilePicture.setVerticalAlignment(SwingConstants.CENTER);
            } else {
                ImageIcon defaultIcon = new ImageIcon("src/default_profile_pic.jpg");
                Image defaultImage = defaultIcon.getImage();
                Image resizedImage = resizeImage(defaultImage);
                lblProfilePicture.setIcon(new ImageIcon(resizedImage));
                lblProfilePicture.setPreferredSize(new Dimension(maxWidth, maxHeight));
                lblProfilePicture.setHorizontalAlignment(SwingConstants.CENTER);
                lblProfilePicture.setVerticalAlignment(SwingConstants.CENTER);
            }

            JLabel lblUsername = new JLabel(String.format("Username:   %-26s",mutualFriend.getUsername()));
            JLabel lblName = new JLabel(String.format("Name:   %-30s", mutualFriend.getName()));
            JLabel lblUserId = new JLabel(String.format("User ID:   %-100d", mutualFriend.getUserId()));
            JButton btnViewAcc = new JButton("View Account");
            btnViewAcc.setBackground(new Color(200, 170, 105));
            btnViewAcc.setForeground(new Color(58,30,0));

            JButton btnStatus = new JButton();
            if (mutualFriend.getStatus() == null){
                btnStatus.setText("Add Friend");
                btnStatus.setBackground(new Color(0,102,204));
                btnStatus.setForeground(white);
                btnStatus.addActionListener(this);
            }
            else {
                btnStatus.setText(mutualFriend.getStatus());
                if(btnStatus.getText().equals("Friend request sent")) {
                    btnStatus.setBackground(new Color(0, 0, 102));
                    btnStatus.setForeground(Color.WHITE);
                }else if (btnStatus.getText().equals("Received friend request")) {
                    btnStatus.setBackground(new Color(255,255,153));
                }else if(btnStatus.getText().equals("Friend")){
                    btnStatus.setBackground(new Color(0,204,0));
                    btnStatus.setForeground(Color.WHITE);
                }else{
                    btnStatus.setForeground(Color.WHITE);
                    btnStatus.setBackground(new Color(0,102,204));
                }
            }

            resultGBC.gridx = 0;
            resultGBC.gridy = 0;
            resultGBC.gridheight = 10;
            resultPanel.add(lblProfilePicture,resultGBC);
            resultGBC.gridx = 1;
            resultGBC.gridheight = 1;
            resultGBC.insets = new Insets(10,10,10,10);
            resultGBC.anchor = GridBagConstraints.WEST;
            resultPanel.add(lblName,resultGBC);
            resultGBC.gridy = 1;
            resultPanel.add(lblUsername,resultGBC);
            resultGBC.gridy = 2;
            resultPanel.add(lblUserId,resultGBC);
            resultGBC.gridy = 3;
            resultPanel.add(btnStatus,resultGBC);
            resultGBC.gridy = 4;
            resultPanel.add(btnViewAcc,resultGBC);

            framePanel.add(resultPanel,BorderLayout.CENTER);
            mutualFriendsPanel.add(framePanel);

            ActionListener buttonActionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == btnStatus){
                        if (btnStatus.getText().equals("Add Friend")){
                            database.insertStatus(userID, mutualFriend.getUserId(), 1);// send request
                            database.insertStatus(mutualFriend.getUserId(), userID, 2);// receive request
                            JOptionPane.showMessageDialog(scrollPane, "Your friend request is sent!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            btnStatus.setText("Friend request sent");
                            btnStatus.setBackground(new Color(0,0,102));
                            btnStatus.setForeground(Color.white);
                        }
                        else if (btnStatus.getText().equals("Friend request sent")) {
                            int response = JOptionPane.showConfirmDialog(scrollPane, "Are you sure you want to cancel your friend request?", "Confirm", JOptionPane.YES_NO_OPTION);
                            if (response == JOptionPane.YES_OPTION) {
                                database.removeStatus(userID, mutualFriend.getUserId());// delete request
                                JOptionPane.showMessageDialog(scrollPane, "Friend request cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                btnStatus.setText("Add Friend");
                                btnStatus.setBackground(new Color(0,102,204));
                                btnStatus.setForeground(Color.white);
                            }
                        }
                        else if (btnStatus.getText().equals("Received friend request")){
                            Object[] options = {"Confirm", "Delete"};
                            int response = JOptionPane.showOptionDialog(scrollPane, "Confirm friend request?", "Confirm",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            if (response == 0){//confirm
                                database.updateStatus(userID, mutualFriend.getUserId(),3);// Update status to friends
                                JOptionPane.showMessageDialog(scrollPane, "You two are now friends!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                tracebackFunction.peek();
                                dispose();
                            }
                            else if (response == 1){//delete
                                database.removeStatus(userID, mutualFriend.getUserId());
                                JOptionPane.showMessageDialog(scrollPane, "You deleted the friend request...", "Success", JOptionPane.INFORMATION_MESSAGE);
                                tracebackFunction.peek();
                                dispose();
                            }
                        }
                        else if (btnStatus.getText().equals("Friend")){
                            int response = JOptionPane.showConfirmDialog(scrollPane, "UNFRIEND?", "Confirm", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null);
                            if(response == JOptionPane.YES_OPTION) {
                                database.removeStatus(userID,mutualFriend.getUserId());
                                tracebackFunction.peek();
                                dispose();
                            }
                        }
                    }
                    else if (e.getSource() == btnViewAcc){
                        tracebackFunction.pushPage(new ViewAccountPage(username,mutualFriend.getUserId(),tracebackFunction));
                        dispose();
                    }
                }
            };
            btnStatus.addActionListener(buttonActionListener);
            btnViewAcc.addActionListener(buttonActionListener);
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel();
        topPanel.add(forestbook);
        topPanel.setBackground(new Color(180, 238, 156));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(new Color(180, 238, 156));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints centerGBC = new GridBagConstraints();
        centerGBC.gridx = 0;
        centerGBC.gridy = 0;
        centerPanel.add(searchPanel,centerGBC);
        centerGBC.gridy = 1;
        lblSuggestedFriend.setText("Suggested Friend(s): " + suggestedFriend);
        centerPanel.add(lblSuggestedFriend,centerGBC);
        centerGBC.gridy = 2;
        centerGBC.weightx = 1;
        centerGBC.anchor = GridBagConstraints.CENTER;
        centerPanel.add(scrollPane,centerGBC);
        centerPanel.setBackground(new Color(180, 238, 156));

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

        getContentPane().add(panel);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
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
        new HomePage(username,tracebackFunction);
    }
    public String getTitle(){
        return "Home Page";
    }
    @Override
    public void actionPerformed(ActionEvent e){
         if (e.getSource() == btnSearch){
            String keyword = txtSearch.getText();
            //SearchResultsPage
            if (!keyword.isEmpty()){
                tracebackFunction.pushPage(new SearchResultsPage(userID,keyword,tracebackFunction));
                dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "Please enter search query.", "Empty Search", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (e.getSource() == btnNoti){
            NotificationScrollPane scrollPane = new NotificationScrollPane(userID);
            scrollPane.show(btnNoti, -15, btnNoti.getHeight());
        }
        else if (e.getSource() == btnBack){
            tracebackFunction.popPeek();
            dispose();
        }
    }
}