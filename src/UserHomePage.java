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
    private final JButton btnSearch, btnFriendReq, btnViewAcc, btnEditAcc, btnLogOut;
    private final JLabel facebook;

    public UserHomePage() {
        super("Home Page");
        loginPage = null;

        txtSearchFriend = new JTextField("Search Friend", 20);
        btnSearch = new JButton("Search");
        btnFriendReq = new JButton("View Friend Request");
        btnViewAcc = new JButton("View Account");
        btnEditAcc = new JButton("Edit Account");
        btnLogOut = new JButton("Log Out");

        ImageIcon imageicon1 = new ImageIcon(getClass().getResource("facebook.png"));
        Image image1 = imageicon1.getImage();
        Image scalingimage1 = image1.getScaledInstance(350,50,Image.SCALE_SMOOTH);
        ImageIcon scaledimage1 = new ImageIcon(scalingimage1);

        facebook= new JLabel(scaledimage1);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.blue);
        panel.setPreferredSize(new Dimension(1500,80));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10,10,10,80);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(facebook, gbc);

        gbc.insets = new Insets(10,130,10,10);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(txtSearchFriend, gbc);

        gbc.gridx = 4;
        gbc.insets = new Insets(10,10,10,10);
        panel.add(btnSearch,gbc);

        gbc.gridx = 6;
        gbc.insets = new Insets(10,500,10,10);
        panel.add(btnLogOut,gbc);

        btnLogOut.addActionListener(this);
        btnSearch.addActionListener(this);

        add(panel, BorderLayout.NORTH);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()== btnSearch){
            System.out.println("Searching user: " + txtSearchFriend.getText());
        }

        else if(e.getSource()== btnFriendReq){

        }

        else if(e.getSource()== btnViewAcc){

        }

        else if(e.getSource()== btnEditAcc){

        }

        else if(e.getSource()== btnLogOut){
            if(loginPage == null){
                new LoginPage();
                dispose();
            }
        }
    }
}
