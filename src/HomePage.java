import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.awt.Color.*;
public class HomePage extends JFrame implements Page,ActionListener{
    private final JTextField txtSearch;
    private final JButton btnSearch, btnUser, btnViewAcc, btnEditAcc, btnLogOut, btnBack;
    private final JLabel forestbook;
    private final Database database;
    private final int userID;
    private final String username;
    private final TracebackFunction tracebackFunction;
    public HomePage(String username, TracebackFunction tracebackFunction) {
        super("ForestBook Home Page");
        this.username = username;
        database = new Database();
        this.userID = database.getUserId(username);
        this.tracebackFunction = tracebackFunction;

        forestbook = new JLabel("ForestBook");
        txtSearch = new JTextField(40);
        btnSearch = new JButton("Search");
        btnUser = new JButton(username);
        btnViewAcc = new JButton("View Account");
        btnEditAcc = new JButton("Edit Account");
        btnLogOut = new JButton("Log Out");
        btnBack = new JButton("Back");

        btnViewAcc.addActionListener(this);
        btnEditAcc.addActionListener(this);
        btnLogOut.addActionListener(this);
        btnSearch.addActionListener(this);
        btnBack.addActionListener(this);

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
        panel3.add(btnBack,componentsGBC);
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
    public void showPage(){
        new HomePage(username,tracebackFunction);
    }
    @Override
    public void actionPerformed(ActionEvent e){
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
//            tracebackFunction.pushPage(new SearchResultsPage(username,keyword,tracebackFunction));
            dispose();
        }
        else if (e.getSource() == btnBack){
            tracebackFunction.popPeek();
            dispose();
        }
    }
}
