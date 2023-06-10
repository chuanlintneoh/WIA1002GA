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

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel();
        topPanel.add(forestbook);
        topPanel.setBackground(new Color(180, 238, 156));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(txtSearch);
        centerPanel.add(btnSearch);

        centerPanel.setBackground(new Color(180, 238, 156));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnBack);
        bottomPanel.setBackground(new Color(180, 238, 156));

        JPanel userPanel = new JPanel();
        userPanel.add(btnUser);
        userPanel.setBackground(new Color(180, 238, 156));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        panel.add(userPanel, BorderLayout.EAST);

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
