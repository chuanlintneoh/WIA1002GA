import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.awt.Color.*;
public class HomePage extends JFrame implements ActionListener{
    private final JTextField txtSearch;
    private final JButton btnSearch, btnUser, btnViewAcc, btnEditAcc, btnLogOut, btnFriendReq, btnFriends;
    private final JLabel forestbook;
    private final Database database;
    private final int userID;
    private final String username;
//    private Stack<Object> tracebackFunction;
    public HomePage(int userID) {
        super("ForestBook Home Page");
        this.userID = userID;
        database = new Database();
        this.username = database.get("username",userID);

        forestbook = new JLabel("ForestBook");
        txtSearch = new JTextField(40);
        btnSearch = new JButton("Search");
        btnUser = new JButton(username);
        btnFriendReq = new JButton("Friend Requests");
        btnFriends = new JButton("Friends");
        btnViewAcc = new JButton("View Account");
        btnEditAcc = new JButton("Edit Account");
        btnLogOut = new JButton("Log Out");

        btnViewAcc.addActionListener(this);
        btnEditAcc.addActionListener(this);
        btnLogOut.addActionListener(this);
        btnSearch.addActionListener(this);

        JPopupMenu accountMenu = new JPopupMenu();
        accountMenu.add(btnViewAcc);
        accountMenu.add(btnEditAcc);
        accountMenu.add(btnLogOut);

        btnUser.addActionListener(e -> accountMenu.show(btnUser, 0, btnUser.getHeight()));

        forestbook.setFont(new Font("Arial",Font.BOLD,32));
        forestbook.setForeground(GREEN);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,0,0,0);
        GridBagConstraints componentsGBC = new GridBagConstraints();
        componentsGBC.insets = new Insets(10,10,10,10);

        JPanel panel1 = new JPanel();
        panel1.add(forestbook,componentsGBC);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 0.2;
        panel.add(panel1,gbc);

        JPanel panel2 = new JPanel();
        panel2.add(txtSearch,componentsGBC);
        panel2.add(btnSearch,componentsGBC);
        panel2.add(btnUser,componentsGBC);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 0.2;
        panel.add(panel2,gbc);

        JPanel panel3 = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.weighty = 0.8;
        panel.add(panel3,gbc);

        JPanel panel4 = new JPanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 0.8;
        panel.add(panel4,gbc);

        add(panel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnLogOut){
            //LoginPage
            new LoginPage();
            dispose();
        }
        else if (e.getSource() == btnViewAcc){
            //ViewAccountPage (own account)
            new ViewAccountPage(username,0);
            dispose();
        }
        else if (e.getSource() == btnEditAcc){
            //EditAccountPage
            new EditAccountPage(username);
            dispose();
        }
        else if (e.getSource() == btnSearch){
            //SearchResultsPage
        }
        else if (e.getSource() == btnFriendReq) {

        }
        else if (e.getSource() == btnFriends){

        }
    }
}
