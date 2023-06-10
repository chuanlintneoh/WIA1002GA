import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import static java.awt.Color.*;
public class SearchResultsPage extends JFrame implements Page, ActionListener {
    private final JTextField txtSearch;
    private final JButton btnUser, btnSearch, btnViewAcc, btnEditAcc, btnLogOut, btnBack, btnStatus;
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
        btnStatus = new JButton("Add Friend");

        btnViewAcc.addActionListener(this);
        btnEditAcc.addActionListener(this);
        btnLogOut.addActionListener(this);
        btnSearch.addActionListener(this);
        btnBack.addActionListener(this);
        btnStatus.addActionListener(this);

        JPopupMenu accountMenu = new JPopupMenu();
        accountMenu.add(btnViewAcc);
        accountMenu.add(btnEditAcc);
        accountMenu.add(btnLogOut);

        btnUser.addActionListener(e -> accountMenu.show(btnUser, 0, btnUser.getHeight()));

        forestbook.setFont(new Font("Curlz MT", Font.BOLD, 42));
        forestbook.setForeground(new Color(0, 128, 0));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        GridBagConstraints componentsGBC = new GridBagConstraints();
        componentsGBC.insets = new Insets(10,10,10,10);

        JPanel panel1 = new JPanel();
        panel1.add(forestbook,componentsGBC);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 0.2;
        panel.add(panel1, gbc);

        JPanel panel2 = new JPanel();
        panel2.add(txtSearch,componentsGBC);
        panel2.add(btnSearch,componentsGBC);
        panel2.add(btnUser,componentsGBC);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 0.2;
        panel.add(panel2, gbc);

        JPanel searchResultsPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(searchResultsPanel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.7;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);

        ArrayList<Friend> searchResults = (ArrayList<Friend>) database.searchUser(userId,keyword);
        for (Friend searchResult : searchResults){
            JPanel resultPanel = new JPanel(new GridBagLayout());
            GridBagConstraints resultGBC = new GridBagConstraints();

            byte[] profilePictureData = database.getProfilePicture(searchResult.getUserId());
            JLabel lblProfilePicture = new JLabel();
            if (profilePictureData != null){
                lblProfilePicture.setIcon(new ImageIcon(profilePictureData));
            }
            else {
                lblProfilePicture.setIcon(new ImageIcon("src/default_profile_pic.jpg"));
            }
            JLabel lblUsername = new JLabel(searchResult.getUsername());
            JLabel lblName = new JLabel(searchResult.getName());
            JLabel lblUserId = new JLabel(String.valueOf(searchResult.getUserId()));
            JButton btnViewAcc = new JButton("View Account");
            btnViewAcc.addActionListener(this);

            resultGBC.gridx = 1;
            resultGBC.gridy = 0;
            resultPanel.add(lblUsername,resultGBC);
            resultGBC.gridy = 1;
            resultPanel.add(lblName,resultGBC);
            resultGBC.gridy = 2;
            resultPanel.add(lblUserId,resultGBC);
            resultGBC.gridx = 2;
            resultGBC.gridy = 0;
            resultGBC.anchor = GridBagConstraints.EAST;
            resultPanel.add(btnViewAcc,resultGBC);
            resultGBC.gridx = 0;
            resultGBC.gridy = 0;
            resultGBC.gridheight = 3;
            resultPanel.add(lblProfilePicture,resultGBC);

            resultPanel.setBackground(new Color(180, 238, 156));
            gbc.weightx = 1.0;
            gbc.weighty = 0.3;
            gbc.insets = new Insets(10,10,10,10);
            searchResultsPanel.add(resultPanel,gbc);
        }

        JPanel panel4 = new JPanel();
        componentsGBC.anchor = GridBagConstraints.WEST;
        panel4.add(btnBack,componentsGBC);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        panel.add(panel4, gbc);

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
