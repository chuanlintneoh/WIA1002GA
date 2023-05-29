import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminPage extends JFrame {
    private Database database;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton viewButton;

    public AdminPage(Database database) {
        this.database = database;

        // Set up the frame
        setTitle("Admin Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create components
        userTable = new JTable();
        tableModel = new DefaultTableModel();
        tableModel.addColumn("User ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Username");
        tableModel.addColumn("Email");

        JScrollPane scrollPane = new JScrollPane(userTable);
        deleteButton = new JButton("Delete User");
        viewButton = new JButton("View User");

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Register action listeners for buttons
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) userTable.getValueAt(selectedRow, 0);
                    deleteUser(userId);
                } else {
                    JOptionPane.showMessageDialog(AdminPage.this, "Please select a user to delete.");
                }
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    Integer userId = (Integer) userTable.getValueAt(selectedRow, 0);
                    viewUser(userId);
                } else {
                    JOptionPane.showMessageDialog(AdminPage.this, "Please select a user to view.");
                }
            }
        });

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
            Object[] rowData = {user.getUserId(), user.getName(), user.getUsername(), user.getemailAddress()};
            tableModel.addRow(rowData);
        }

        // Set the table model
        userTable.setModel(tableModel);
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
            if (user.getUserId() == userId) {
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

            // Add the profile picture and user details to the panel
            panel.add(profilePictureLabel, BorderLayout.WEST);
            panel.add(userDetailsTextArea, BorderLayout.CENTER);

            // Show the panel with the user details and profile picture
            JOptionPane.showMessageDialog(this, panel, "User Details", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "User not found.");
        }
    }
    public static void main(String[] args) {
        // Create an instance of the Database class
        Database database = new Database();

        // Create an instance of the AdminPage
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminPage(database);
            }
        });
    }
}


