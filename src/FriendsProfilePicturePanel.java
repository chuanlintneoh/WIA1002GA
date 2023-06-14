import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class FriendsProfilePicturePanel extends JPanel implements ActionListener {
    private final JFrame parent;
    private final JPanel mainPanel;
    private final JScrollPane scrollPane;
    private final int userID;
    private final Database database;
    private final TracebackFunction tracebackFunction;
    private final int maxWidth = 135;
    private final int maxHeight = 180;
    public FriendsProfilePicturePanel(JFrame parent, int userID, boolean friends, TracebackFunction tracebackFunction){
        this.parent = parent;
        this.userID = userID;
        this.tracebackFunction = tracebackFunction;
        this.database = new Database();

        mainPanel = new JPanel(new GridLayout(0,2,10,10));
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setPreferredSize(new Dimension(300,450));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100);

        ArrayList<Friend> friendsRequests = (ArrayList<Friend>) database.viewFriendsRequests(userID);
        ArrayList<Image> profilePictures = new ArrayList<>();
        ArrayList<Integer> correspondingIDs = new ArrayList<>();
        for (Friend user: friendsRequests){
            if (friends && user.getStatus().equals("Friend")){
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
            else if (!friends && user.getStatus().equals("Received friend request")){
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
        }// Retrieve all profile pictures for friends/ friend requests
        for (int i = 0; i < profilePictures.size(); i++){
            JButton btnFriend = new JButton(new ImageIcon(profilePictures.get(i)));
            btnFriend.setBorder(LineBorder.createBlackLineBorder());
            btnFriend.setPreferredSize(new Dimension(maxWidth, maxHeight));
            btnFriend.setHorizontalAlignment(SwingConstants.CENTER);
            btnFriend.setVerticalAlignment(SwingConstants.CENTER);
            btnFriend.addActionListener(this);
            btnFriend.setActionCommand(String.valueOf(correspondingIDs.get(i)));
            btnFriend.setToolTipText("<html><body>" +
                    "Username: " + database.get("username",correspondingIDs.get(i)) + "<br>" +
                    "Name: " + database.get("name",correspondingIDs.get(i)) + "<br>" +
                    "User ID: " + correspondingIDs.get(i) +
                    "</body></html>");
            mainPanel.add(btnFriend);
        }// create buttons, insert profile pictures as icon for each respective buttons, add actionListener to view account page
        setLayout(new BorderLayout());
        add(scrollPane,BorderLayout.CENTER);
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
    @Override
    public void actionPerformed(ActionEvent e){
        int correspondingID = Integer.parseInt(e.getActionCommand());
        tracebackFunction.pushPage(new ViewAccountPage(database.get("username",userID),correspondingID,tracebackFunction));
        parent.dispose();
    }
}