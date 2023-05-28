import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import static java.awt.Color.*;

public class UserHomePage extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new UserHomePage();
    }
    private final LoginPage loginPage;
    private final JTextField txtSearchFriend;
    private final JButton btnSearch, btnFriendReq, btnUser, btnFriends, btnPages, btnHistory;
    private final JLabel forestbook;

    public UserHomePage() {
        super("Home Page");
        loginPage = null;

        txtSearchFriend = new JTextField("Search Friend", 20);
        btnSearch = new JButton("Search");
        btnFriendReq = new JButton("View Friend Request");
        btnUser = new JButton("user name");
        btnFriends = new JButton("Friends");
        btnPages = new JButton("Pages");
        btnHistory = new JButton("History");
        forestbook = new JLabel("forestbook");

        JPopupMenu userDropDown = new JPopupMenu();
        JMenuItem btnViewAcc = new JMenuItem("View Account");
        JMenuItem btnEditAcc = new JMenuItem("Edit Account");
        JMenuItem btnLogOut = new JMenuItem("Log Out");

        btnViewAcc.setPreferredSize(new Dimension(120, 30));
        userDropDown.add(btnViewAcc);
        btnEditAcc.setPreferredSize(new Dimension(120, 30));
        userDropDown.add(btnEditAcc);
        btnLogOut.setPreferredSize(new Dimension(120, 30));
        userDropDown.add(btnLogOut);

        ActionListener menuItemListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                String optionText = source.getText();

                if(optionText.equals("View Account")){

                }

                else if(optionText.equals("Edit Account")){

                }

                else if(optionText.equals("Log Out")){
                    LoginPage loginPage = new LoginPage();
                    loginPage.setVisible(true);
                    dispose();
                }
            }
        };

        btnViewAcc.addActionListener(menuItemListener);
        btnEditAcc.addActionListener(menuItemListener);
        btnLogOut.addActionListener(menuItemListener);

        btnUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userDropDown.show(btnUser,0,userDropDown.getHeight());
            }
        });


/*        ImageIcon imageicon1 = new ImageIcon(getClass().getResource("facebook.png"));
        Image image1 = imageicon1.getImage();
        Image scalingimage1 = image1.getScaledInstance(350,50,Image.SCALE_SMOOTH);
        ImageIcon scaledimage1 = new ImageIcon(scalingimage1);

        facebook= new JLabel(scaledimage1);
*/

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.blue);
        panel.setPreferredSize(new Dimension(1500,80));

        GridBagConstraints gbc = new GridBagConstraints();



        gbc.insets = new Insets(10,20,10,300);
        gbc.gridx = 0;
        gbc.gridy = 0;
        forestbook.setFont(new Font("Arial",Font.BOLD,32));
        forestbook.setForeground(WHITE);
        panel.add(forestbook, gbc);

        gbc.insets = new Insets(10,100,10,10);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(txtSearchFriend, gbc);

        gbc.gridx = 4;
        gbc.insets = new Insets(10,10,10,200);
        panel.add(btnSearch,gbc);

        gbc.gridx = 6;
        gbc.insets = new Insets(10,200,10,100);
        btnUser.setPreferredSize(new Dimension(100, 30));
        panel.add(btnUser,gbc);

        JPanel panelLeft = new JPanel(new GridBagLayout());
        panelLeft.setBackground(white);
        panelLeft.setPreferredSize(new Dimension(250,1000));

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0,10,0,10);
        btnFriends.setPreferredSize(new Dimension(200, 60));
        panelLeft.add(btnFriends,gbc);

        gbc.gridy = 1;
        btnPages.setPreferredSize(new Dimension(200, 60));
        panelLeft.add(btnPages,gbc);

        gbc.gridy=2;
        gbc.insets = new Insets(0,10,500,10);
        btnHistory.setPreferredSize(new Dimension(200, 60));
        panelLeft.add(btnHistory,gbc);

        btnLogOut.addActionListener(this);
        btnSearch.addActionListener(this);
        btnUser.addActionListener(this);

        add(panel, BorderLayout.NORTH);
        add(panelLeft, BorderLayout.WEST);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()== btnSearch){
            System.out.println("Searching user: " + txtSearchFriend.getText());
        }

        else if(e.getSource()== btnFriendReq){

        }

        else if(e.getSource()== btnFriends){

        }

        else if(e.getSource()== btnPages){

        }

        else if(e.getSource()== btnHistory){

        }

/*        else if(e.getSource()== btnViewAcc){

        }

        else if(e.getSource()== btnEditAcc){

        } */

 /*       else if(e.getSource()== btnLogOut){
            if(loginPage == null){
                new LoginPage();
                dispose();
            }
        }*/
    }
}
