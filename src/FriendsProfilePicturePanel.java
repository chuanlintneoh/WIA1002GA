import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FriendsProfilePicturePanel extends JPanel implements ActionListener {
    private final JFrame parent;
    private final JPanel mainPanel;
    private final JScrollPane scrollPane;
    private final int viewID;
    private final int myID;
    private final Database database;
    private final TracebackFunction tracebackFunction;
    private final int maxWidth = 105;
    private final int maxHeight = 140;
    Color panelBckgrd = new Color(220,230,190);
    public FriendsProfilePicturePanel(JFrame parent, int viewID, int myID, boolean friends, TracebackFunction tracebackFunction){
        this.parent = parent;
        this.viewID = viewID;
        this.myID = myID;
        this.tracebackFunction = tracebackFunction;
        this.database = new Database();

        mainPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        mainPanel.setBackground(panelBckgrd);
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setPreferredSize(new Dimension(260, 400));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100);
        Dimension preferredSize = mainPanel.getPreferredSize();

        // Compare the preferred size with the dimensions of the scroll pane
        if (preferredSize.height <= 400) {
            // If the preferred height is less than or equal to 400, disable scrolling
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        } else {
            // If the preferred height is greater than 400, enable scrolling
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        // Set the preferred size of the scroll pane
        scrollPane.setPreferredSize(new Dimension(260, Math.max(preferredSize.height, 400)));

        refreshFriendRequests(friends);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.NORTH);
    }// stranger's friends
    public FriendsProfilePicturePanel(JFrame parent, int myID, boolean friends, TracebackFunction tracebackFunction) {
        this(parent,myID,myID,friends,tracebackFunction);
    }// own account
    private void refreshFriendRequests(boolean friends) {
        mainPanel.removeAll();
        ArrayList<Friend> friendsRequests = (ArrayList<Friend>) database.viewFriendsRequests(viewID);
        ArrayList<Image> profilePictures = new ArrayList<>();
        ArrayList<Integer> correspondingIDs = new ArrayList<>();

        for (Friend user : friendsRequests) {
            if (friends && user.getStatus().equals("Friend") || !friends && user.getStatus().equals("Received friend request")) {
                byte[] profilePictureData = database.getProfilePicture(user.getUserId());
                Image resizedImage;
                if (profilePictureData != null) {
                    ImageIcon imageIcon = new ImageIcon(profilePictureData);
                    resizedImage = resizeImage(imageIcon.getImage());
                } else {
                    ImageIcon defaultIcon = new ImageIcon("src/default_profile_pic.jpg");
                    resizedImage = resizeImage(defaultIcon.getImage());
                }
                profilePictures.add(resizedImage);
                correspondingIDs.add(database.getUserId(user.getUsername()));
            }
        }

        for (int i = 0; i < profilePictures.size(); i++) {
            JPanel friendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton btnFriend = new JButton(new ImageIcon(profilePictures.get(i)));
            friendPanel.setBackground(panelBckgrd);

            if (friends) {
                JLabel lblFriendName = new JLabel(database.get("username",correspondingIDs.get(i)));
                lblFriendName.setHorizontalAlignment(SwingConstants.CENTER);
                JButton btnChat = new JButton("Chat");
                JPanel chatPanel = new JPanel (new BorderLayout());
                chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
                JPanel spacingPanel = new JPanel();
                spacingPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
                spacingPanel.setBackground(panelBckgrd);

                btnFriend.setBorder(LineBorder.createBlackLineBorder());
                btnFriend.setPreferredSize(new Dimension(maxWidth, maxHeight));
                btnFriend.setHorizontalAlignment(SwingConstants.CENTER);
                btnFriend.setVerticalAlignment(SwingConstants.CENTER);
                btnFriend.addActionListener(this);
                btnFriend.setActionCommand(String.valueOf(correspondingIDs.get(i)));
                btnFriend.setToolTipText("<html><body>" +
                        "Username: " + database.get("username", correspondingIDs.get(i)) + "<br>" +
                        "Name: " + database.get("name", correspondingIDs.get(i)) + "<br>" +
                        "User ID: " + correspondingIDs.get(i) +
                        "</body></html>");
                chatPanel.setBackground(panelBckgrd);
                chatPanel.add(lblFriendName);
                chatPanel.add(spacingPanel);
                chatPanel.add(btnChat);

                friendPanel.add(btnFriend, BorderLayout.WEST);
                friendPanel.add(chatPanel);

                final int index = i;
                ActionListener buttonActionListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == btnChat) {
                            ChatBoxFrame chatBoxFrame = new ChatBoxFrame(parent,myID,correspondingIDs.get(index));
                            tracebackFunction.addHistory("Entered conversation with " + database.get("username",correspondingIDs.get(index)) + ".");
                        }
                    }
                };

                btnChat.setBackground(new Color(166, 220, 156));
                Border lineBorder = BorderFactory.createLineBorder(new Color(0, 100, 0), 2);
                Insets spacingInsets = new Insets(5, 10, 5, 10);
                Border spacingBorder = BorderFactory.createEmptyBorder(spacingInsets.top, spacingInsets.left, spacingInsets.bottom, spacingInsets.right);
                Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, spacingBorder);
                btnChat.setBorder(compoundBorder);
                btnChat.addActionListener(buttonActionListener);
                btnChat.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnChat.setForeground(Color.WHITE); // Change to the desired color
                        btnChat.setBackground(new Color(0,100,0));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnChat.setForeground(new Color(0, 100, 0)); // Change back to the default color
                        btnChat.setBackground(new Color(166, 220, 156));
                    }
                });
            } else {
                JLabel lblReqName = new JLabel(database.get("username",correspondingIDs.get(i)));
                lblReqName.setHorizontalAlignment(SwingConstants.CENTER);
                JButton btnConfirm = new JButton("Confirm");
                btnConfirm.setBackground(new Color(46,138,87));
                btnConfirm.setForeground(Color.white);
                JButton btnDelete = new JButton("Delete");
                btnDelete.setBackground(new Color(200, 7, 14));
                btnDelete.setForeground(Color.white);

                Dimension buttonSize = new Dimension(100, 30);
                btnConfirm.setMaximumSize(buttonSize);
                btnDelete.setMaximumSize(buttonSize);

                JPanel respondPanel = new JPanel (new BorderLayout());
                respondPanel.setLayout(new BoxLayout(respondPanel, BoxLayout.Y_AXIS));
                JPanel spacingPanel1 = new JPanel();
                spacingPanel1.setBorder(new EmptyBorder(0, 10, 0, 10));
                spacingPanel1.setBackground(panelBckgrd);
                JPanel spacingPanel2 = new JPanel();
                spacingPanel2.setBackground(panelBckgrd);
                spacingPanel2.setBorder(new EmptyBorder(0, 10, 0, 10));

                respondPanel.setBackground(panelBckgrd);
                respondPanel.add(lblReqName);
                respondPanel.add(spacingPanel1);
                respondPanel.add(btnConfirm);
                respondPanel.add(spacingPanel2);
                respondPanel.add(btnDelete);

                btnFriend.setBorder(LineBorder.createBlackLineBorder());
                btnFriend.setPreferredSize(new Dimension(maxWidth, maxHeight));
                btnFriend.setHorizontalAlignment(SwingConstants.CENTER);
                btnFriend.setVerticalAlignment(SwingConstants.CENTER);
                btnFriend.addActionListener(this);
                btnFriend.setActionCommand(String.valueOf(correspondingIDs.get(i)));
                btnFriend.setToolTipText("<html><body>" +
                        "Username: " + database.get("username", correspondingIDs.get(i)) + "<br>" +
                        "Name: " + database.get("name", correspondingIDs.get(i)) + "<br>" +
                        "User ID: " + correspondingIDs.get(i) +
                        "</body></html>");

                friendPanel.add(btnFriend, BorderLayout.WEST);
                friendPanel.add(respondPanel);

                final int index = i;
                ActionListener buttonActionListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (e.getSource() == btnConfirm) {
                            int response = JOptionPane.showConfirmDialog(FriendsProfilePicturePanel.this, "Confirm friend request?", "Confirm", JOptionPane.YES_NO_OPTION);
                            if (response == JOptionPane.YES_OPTION) {
                                database.updateStatus(myID, correspondingIDs.get(index), 3);
                                tracebackFunction.addHistory("Became friends with " + database.get("username",correspondingIDs.get(index)) + ".");
                                JOptionPane.showMessageDialog(FriendsProfilePicturePanel.this, "You two are now friends!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                tracebackFunction.peek();// refresh page
                                parent.dispose();
                            }
                        } else if (e.getSource() == btnDelete) {
                            int response = JOptionPane.showConfirmDialog(FriendsProfilePicturePanel.this, "Delete Friend Request?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
                            if (response == JOptionPane.YES_OPTION) {
                                database.removeStatus(correspondingIDs.get(index), myID);
                                tracebackFunction.addHistory("Deleted friend request from " + database.get("username",correspondingIDs.get(index)) + ".");
                                tracebackFunction.peek();// refresh page
                                parent.dispose();
                            }
                        }
                    }
                };
                btnConfirm.addActionListener(buttonActionListener);
                btnDelete.addActionListener(buttonActionListener);
            }
            mainPanel.add(friendPanel);
        }

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.NORTH);
    }

    private Image resizeImage (Image originalImage){
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);
        double widthRatio = (double) maxWidth / width;
        double heightRatio = (double) maxHeight / height;
        double scaleRatio = Math.max(widthRatio, heightRatio);
        int newWidth = (int) (width * scaleRatio);
        int newHeight = (int) (height * scaleRatio);
        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    @Override
    public void actionPerformed (ActionEvent e){
        int correspondingID = Integer.parseInt(e.getActionCommand());
        if (myID == correspondingID){
            tracebackFunction.pushPage(new ViewAccountPage(database.get("username", myID), 0, tracebackFunction));
            parent.dispose();
        }
        else {
            tracebackFunction.pushPage(new ViewAccountPage(database.get("username", myID), correspondingID, tracebackFunction));
            parent.dispose();
        }
    }
}