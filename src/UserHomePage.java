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

    public UserHomePage() {
        super("Home Page");
        loginPage = null;

        txtSearchFriend = new JTextField("Search Friend", 50);
        btnSearch = new JButton("Search");
        btnFriendReq = new JButton("View Friend Request");
        btnViewAcc = new JButton("View Account");
        btnEditAcc = new JButton("Edit Account");
        btnLogOut = new JButton("Log Out");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.blue);
        panel.setPreferredSize(new Dimension(100,40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.weightx = 1.0 ;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnLogOut,gbc);

        btnLogOut.addActionListener(this);

        add(panel, BorderLayout.NORTH);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()== btnSearch){

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

