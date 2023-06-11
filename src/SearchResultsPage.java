import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import static java.awt.Color.*;
public class SearchResultsPage extends JFrame implements Page, ActionListener {
    private final JTextField txtSearch;
    private final JButton btnUser, btnSearch, btnViewAcc, btnEditAcc, btnLogOut, btnBack;
    private final JLabel forestbook;
    private final Database database;
    private final String username;
    private final int userID;
    private final String keyword;
    private final TracebackFunction tracebackFunction;
    public SearchResultsPage(int userId, String keyword, TracebackFunction tracebackFunction){
        super(String.format("Search Results of \"%s\"",keyword));
        this.database = new Database();
        this.userID = userId;
        this.username = database.get("username",userId);
        this.keyword = keyword;
        this.tracebackFunction = tracebackFunction;

        forestbook = new JLabel("ForestBook");
        txtSearch = new JTextField(40);
        btnSearch = new JButton("Search");
        btnUser = new JButton(username);
        btnViewAcc = new JButton("View Account");
        btnEditAcc = new JButton("Edit Account");
        btnLogOut = new JButton("Log Out");
        btnBack = new JButton("Back");

        btnViewAcc.setForeground(new Color(58,30,0));
        btnViewAcc.setBackground(new Color(196, 164, 132));
        btnViewAcc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnViewAcc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnViewAcc.setForeground(WHITE); // Change to the desired color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnViewAcc.setForeground(new Color(58,30,0)); // Change back to the default color
            }
        });
        btnEditAcc.setForeground(new Color(58,30,0));
        btnEditAcc.setBackground(new Color(196, 164, 132));
        btnEditAcc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnEditAcc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEditAcc.setForeground(WHITE); // Change to the desired color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnEditAcc.setForeground(new Color(58,30,0)); // Change back to the default color
            }

        });
        btnLogOut.setForeground(new Color(70,13,13));
        btnLogOut.setBackground(new Color(196, 164, 132));
        btnLogOut.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnLogOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogOut.setForeground(WHITE); // Change to the desired color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogOut.setForeground(new Color(70,13,13)); // Change back to the default color
            }
        });

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

        btnViewAcc.addActionListener(this);
        btnEditAcc.addActionListener(this);
        btnLogOut.addActionListener(this);
        btnSearch.addActionListener(this);
        btnBack.addActionListener(this);

        JPopupMenu accountMenu = new JPopupMenu();
        accountMenu.setBackground(new Color(196,164,132));
        accountMenu.add(btnViewAcc);
        accountMenu.add(btnEditAcc);
        accountMenu.add(btnLogOut);

        btnUser.addActionListener(e -> accountMenu.show(btnUser, -56, btnUser.getHeight()));

        forestbook.setFont(new Font("Curlz MT", Font.BOLD, 42));
        forestbook.setForeground(new Color(0, 128, 0));

        JPanel searchResultsPanel = new JPanel();
        searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(searchResultsPanel);
        scrollPane.setPreferredSize(new Dimension(655,500));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100);

        ArrayList<Friend> searchResults = (ArrayList<Friend>) database.searchUser(userId,keyword);
        for (Friend searchResult : searchResults){
            JPanel framePanel = new JPanel(new BorderLayout());
            framePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JPanel resultPanel = new JPanel(new GridBagLayout());
            resultPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            GridBagConstraints resultGBC = new GridBagConstraints();

            byte[] profilePictureData = database.getProfilePicture(searchResult.getUserId());
            JLabel lblProfilePicture = new JLabel();
            if (profilePictureData != null){
                lblProfilePicture.setIcon(new ImageIcon(profilePictureData));
            }
            else {
                lblProfilePicture.setIcon(new ImageIcon("src/default_profile_pic.jpg"));
            }
            JLabel lblUsername = new JLabel(String.format("Username:   %-26s",searchResult.getUsername()));
            JLabel lblName = new JLabel(String.format("Name:   %-30s", searchResult.getName()));
            JLabel lblUserId = new JLabel(String.format("User ID:   %-100d", searchResult.getUserId()));
            JButton btnViewAcc = new JButton("View Account");
            JButton btnStatus = new JButton();
            if (searchResult.getStatus() == null){
                btnStatus.setText("Add Friend");
            }
            else {
                btnStatus.setText(searchResult.getStatus());
            }
            btnStatus.setForeground(Color.WHITE);
            btnStatus.setBackground(new Color(0, 102, 204));

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
            searchResultsPanel.add(framePanel);

            ActionListener buttonActionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == btnStatus){
                        if (btnStatus.getText().equals("Add Friend")){
                            database.insertStatus(userID, searchResult.getUserId(), 1);// send request
                            database.insertStatus(searchResult.getUserId(), userID, 2);// receive request
                            JOptionPane.showMessageDialog(scrollPane, "Your friend request is sent!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            btnStatus.setText("Friend request sent");
                        }
                        else if (btnStatus.getText().equals("Friend request sent")) {
                            int response = JOptionPane.showConfirmDialog(scrollPane, "Are you sure you want to cancel your friend request?", "Confirm", JOptionPane.YES_NO_OPTION);
                            if (response == JOptionPane.YES_OPTION) {
                                database.removeStatus(userID, searchResult.getUserId());// delete request
                                JOptionPane.showMessageDialog(scrollPane, "Friend request cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                btnStatus.setText("Add Friend");
                            }
                        }
                        else if (btnStatus.getText().equals("Received friend request")){
                            Object[] options = {"Confirm", "Delete"};
                            int response = JOptionPane.showOptionDialog(scrollPane, "Confirm friend request?", "Confirm",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            if (response == 0){//confirm
                                database.updateStatus(userID, searchResult.getUserId(),3);// Update status to friends
                                JOptionPane.showMessageDialog(scrollPane, "You two are now friends!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                btnStatus.setText("Friend");
                            }
                            else if (response == 1){//delete
                                database.removeStatus(userID, searchResult.getUserId());
                                JOptionPane.showMessageDialog(scrollPane, "You deleted the friend request...", "Success", JOptionPane.INFORMATION_MESSAGE);
                                btnStatus.setText("Add Friend");
                            }
                        }
                        else if (btnStatus.getText().equals("Friend")){
                            int response = JOptionPane.showConfirmDialog(scrollPane, "UNFRIEND?", "Confirm", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null);
                            if(response == JOptionPane.YES_OPTION) {
                                database.removeStatus(userID,searchResult.getUserId());
                                btnStatus.setText("Add Friend");
                            }
                        }
                    }
                    else if (e.getSource() == btnViewAcc){
                        tracebackFunction.pushPage(new ViewAccountPage(username,searchResult.getUserId(),tracebackFunction));
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
        topPanel.add(btnUser);
        topPanel.setBackground(new Color(180, 238, 156));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints centerGBC = new GridBagConstraints();
        centerGBC.gridx = 0;
        centerGBC.gridy = 0;
        centerPanel.add(txtSearch, centerGBC);
        centerGBC.gridx = 1;
        centerPanel.add(btnSearch, centerGBC);
        centerGBC.gridx = 0;
        centerGBC.gridy = 1;
        centerGBC.weightx = 1.0;
        centerGBC.anchor = GridBagConstraints.CENTER;
        centerPanel.add(scrollPane, centerGBC);
        centerPanel.setBackground(new Color(180, 238, 156));

        JPanel eastPanel = new JPanel();
        eastPanel.add(btnUser);
        eastPanel.setBackground(new Color(180, 238, 156));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnBack);
        bottomPanel.setBackground(new Color(180, 238, 156));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(eastPanel, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }
    @Override
    public void showPage() {
        new SearchResultsPage(userID,keyword,tracebackFunction);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogOut){
            //LoginPage
            tracebackFunction.clear();
            new LoginPage(tracebackFunction);
            dispose();
        }
        else if (e.getSource() == btnViewAcc){
            //ViewAccountPage (own account)
            tracebackFunction.pushPage(new ViewAccountPage(username,0,tracebackFunction));
            dispose();
        }
        else if (e.getSource() == btnEditAcc){
            //EditAccountPage
            tracebackFunction.pushPage(new EditAccountPage(username,tracebackFunction));
            dispose();
        }
        else if (e.getSource() == btnSearch){
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
        else if (e.getSource() == btnBack){
            tracebackFunction.popPeek();
            dispose();
        }
    }
}
