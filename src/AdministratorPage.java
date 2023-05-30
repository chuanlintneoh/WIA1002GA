import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
public class AdministratorPage extends JFrame implements Page,ActionListener {
    private final Database database;
    private final JTable userTable;
    private final DefaultTableModel tableModel;
    private final JButton deleteButton, viewButton, closeButton;
    private final String username;
    private final TracebackFunction tracebackFunction;
    public AdministratorPage(String username, TracebackFunction tracebackFunction){
        super("Admin Page");
        this.database = new Database();
        this.username = username;
        this.tracebackFunction = tracebackFunction;
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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
        closeButton = new JButton("Close");
        closeButton.addActionListener(this);

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Fetch all users from the database and populate the table
        fetchUsers();

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
        try {
            // Delete the user from the database
            database.deleteUser(userId);
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
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

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
            panel.add(profilePictureLabel, BorderLayout.WEST);
            panel.add(userDetailsTextArea, BorderLayout.CENTER);

            // Show the panel with the user details and profile picture
            JOptionPane.showMessageDialog(this, panel, "User Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "User not found.");
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewButton){
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                Integer userId = (Integer) userTable.getValueAt(selectedRow, 0);
                viewUser(userId);
            } else {
                JOptionPane.showMessageDialog(AdministratorPage.this, "Please select a user to view.");
            }
        }
        else if (e.getSource() == deleteButton){
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) userTable.getValueAt(selectedRow, 0);
                database.deleteUser(userId);
            } else {
                JOptionPane.showMessageDialog(AdministratorPage.this, "Please select a user to delete.");
            }
        }
        else if (e.getSource() == closeButton){
            tracebackFunction.pushPage(new EditAccountPage(username,tracebackFunction));
            dispose();
        }
    }
}
