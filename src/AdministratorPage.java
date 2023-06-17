import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import static java.awt.Color.*;
public class AdministratorPage extends JFrame implements Page,ActionListener {
    private final Database database;
    private final JLabel forestbook;
    private JPanel centerPanel;
    private final JTable userTable;
    private final DefaultTableModel tableModel;
    private final JButton btnNoti, btnUser, deleteButton, viewButton, sendButton, closeButton, btnBack;
    private final String username;
    private final TracebackFunction tracebackFunction;
    public AdministratorPage(String username, TracebackFunction tracebackFunction){
        this.database = new Database();
        this.username = username;
        this.tracebackFunction = tracebackFunction;
        setLayout(new BorderLayout());

        forestbook = new JLabel("ForestBook");
        forestbook.setFont(new Font("Curlz MT", Font.BOLD, 42));
        forestbook.setForeground(new Color(0, 128, 0));
        btnNoti = new JButton("Notifications");
        btnNoti.addActionListener(this);
        btnUser = new JButton(username);
//        btnUser.setFont(new Font(btnUser.getFont().getName(), Font.BOLD, 16));
//        btnUser.setBackground(new Color(180, 238, 156));
//        btnUser.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 50));
//        btnUser.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                btnUser.setForeground(WHITE); // Change to the desired color
//            }
//            @Override
//            public void mouseExited(MouseEvent e) {
//                btnUser.setForeground(new Color(58,30,0)); // Change back to the default color
//            }
//        });
        btnUser.addActionListener(e -> new AccountMenu(username,tracebackFunction,this).show(btnUser, -56, btnUser.getHeight()));

        // Create components
        userTable = new JTable();
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells not editable
            }
        };
        tableModel.addColumn("User ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Username");
        tableModel.addColumn("Email");
        userTable.setModel(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(userTable);
        deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(this);
        viewButton = new JButton("View User");
        viewButton.addActionListener(this);
        sendButton = new JButton("Send Message");
        sendButton.addActionListener(this);
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        btnBack = new JButton("Back");
        btnBack.addActionListener(this);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(forestbook,BorderLayout.CENTER);
        JPanel eastPanel = new JPanel(new GridBagLayout());
        GridBagConstraints eastGBC = new GridBagConstraints();
        eastGBC.insets = new Insets(10,10,10,10);
        eastGBC.anchor = GridBagConstraints.CENTER;
        eastGBC.gridx = 0;
        eastGBC.gridy = 0;
        eastPanel.add(btnNoti,eastGBC);
        eastGBC.gridx = 1;
        eastPanel.add(btnUser,eastGBC);
        topPanel.add(eastPanel,BorderLayout.EAST);

        // Add components to the frame
        add(topPanel,BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
        add(deleteButton,BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(btnBack);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Fetch all users from the database and populate the table
        fetchUsers();

        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        setVisible(true);
    }
    private void fetchUsers() {
        // Clear the table
        tableModel.setRowCount(0);

        // Fetch all users from the database
        List<User> users = database.viewAllUsers();

        // Populate the table with user data
        for (User user : users) {
            Object[] rowData = {user.getUserId(), user.getName(), user.getUsername(), user.getEmailAddress()};
            tableModel.addRow(rowData);
        }
    }
    private void deleteUser(int userId) {
        int adminUserID = database.getUserId(username);
        if (adminUserID == userId){
            JOptionPane.showMessageDialog(this,"Deleting own account is not allowed.");
            return;
        }
        try {
            // Delete the user from the database
            database.deleteUser(userId);
            tracebackFunction.addHistory("<Admin Feature> Deleted user: " + database.get("username",userId));
            JOptionPane.showMessageDialog(this, "User deleted successfully.");
            fetchUsers(); // Refresh the table
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to delete the user.");
            e.printStackTrace();
        }
    }
    private void viewUser(int userId) {
        List<User> users = database.viewAllUsers();
        User selectedUser = null;

        // Find the user with the specified userId
        for (User user : users) {
            if ((user.getUserId() == userId)) {
                selectedUser = user;
                break;
            }
        }
        if (selectedUser != null) {
            // Create a panel to hold the user details and profile picture
            centerPanel.setLayout(new BorderLayout());

            // Create a label for the profile picture
            JLabel profilePictureLabel = new JLabel();

            // Get the profile picture byte array from the database
            byte[] profilePictureData = database.getProfilePicture(userId);
            if (profilePictureData != null) {
                // Create an ImageIcon from the profile picture data
                ImageIcon profilePicture = new ImageIcon(profilePictureData);
                profilePictureLabel.setIcon(profilePicture);
            }

            // Create a text area for the user details
            JTextArea userDetailsTextArea = new JTextArea();
            userDetailsTextArea.setEditable(false);
            userDetailsTextArea.append("User ID: " + selectedUser.getUserId() + "\n");
            userDetailsTextArea.append("Name: " + selectedUser.getName() + "\n");
            userDetailsTextArea.append("Username: " + selectedUser.getUsername() + "\n");
            userDetailsTextArea.append("Email: " + selectedUser.getEmailAddress() + "\n");
            userDetailsTextArea.append("Gender: " + selectedUser.getGender() + "\n");
            userDetailsTextArea.append("Birthdate: " + selectedUser.getBirthDate() + "\n");

            // Add the profile picture and user details to the panel
            centerPanel.add(profilePictureLabel, BorderLayout.WEST);
            centerPanel.add(userDetailsTextArea, BorderLayout.CENTER);

            // Show the panel with the user details and profile picture
            JOptionPane.showMessageDialog(this, centerPanel, "User Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "User not found.");
        }
    }
    public void showPage(){
        new AdministratorPage(username,tracebackFunction);
    }
    public String getTitle(){
        return "Administrator Page";
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnNoti){
            NotificationScrollPane scrollPane = new NotificationScrollPane(database.getUserId(username));
            scrollPane.show(btnNoti, 0, btnNoti.getHeight());
        }
        else if (e.getSource() == viewButton){
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                Integer userId = (Integer) userTable.getValueAt(selectedRow, 0);
                centerPanel = new JPanel();
                viewUser(userId);
            } else {
                JOptionPane.showMessageDialog(AdministratorPage.this, "Please select a user to view.");
            }
        }
        else if (e.getSource() == deleteButton){
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) userTable.getValueAt(selectedRow, 0);
                deleteUser(userId);
            } else {
                JOptionPane.showMessageDialog(AdministratorPage.this, "Please select a user to delete.");
            }
        }
        else if (e.getSource() == sendButton){
            SendMessageDialog dialog = new SendMessageDialog(this,database.getUserId(username),tracebackFunction);
            dialog.setVisible(true);
        }
        else if (e.getSource() == closeButton){
            tracebackFunction.pushPage(new EditAccountPage(username,tracebackFunction));
            dispose();
        }
        else if (e.getSource() == btnBack){
            tracebackFunction.popPeek();
            dispose();
        }
    }
}