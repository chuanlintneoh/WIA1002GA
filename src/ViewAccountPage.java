import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Stack;

public class ViewAccountPage extends JFrame implements ActionListener{
    public static void main(String[] args) {
        new ViewAccountPage("vinnieying");
    }
    private final JLabel lblName, txtName, lblUsername, txtUsername,lblEmail, txtEmail, lblContactNo, txtContactNo, lblDOB, txtDOB, lblGender, txtGender, lblHobbies, txtHobbies, lblJobHistory, lblAddress;
    private final JTextArea txtJobHistory,txtAddress;
    private final JButton btnEditAcc;
    private final Database database;
    private final int userID;
    private final String username;
    public ViewAccountPage(String username) {
        super("View Account Page");
        this.username = username;
        database = new Database();
        this.userID = database.getUserId(username);

        // Initialize GUI components
        lblUsername = new JLabel("Username:");
        txtUsername = new JLabel(database.get("username", userID));
        txtName = new JLabel(database.get("name", userID));
        lblName = new JLabel("Name:");
        lblEmail = new JLabel("Email address:");
        txtEmail = new JLabel(database.get("email_address", userID));
        lblContactNo = new JLabel("Contact No.:");
        txtContactNo = new JLabel(database.get("contact_no", userID));
        lblAddress = new JLabel("Address:");

        txtAddress = new JTextArea(database.get("address",userID));
        txtAddress.setEditable(false);
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        txtAddress.setPreferredSize(new Dimension(200, 50));

        lblDOB = new JLabel("Date Of Birth:");
        txtDOB = new JLabel(database.get("birthdate", userID));
        lblGender = new JLabel("Gender:");
        txtGender = new JLabel(database.get("gender",userID));

        lblJobHistory = new JLabel("Job History:");
        Stack<Job> jobs=database.viewUserJobs(userID);
        StringBuilder jobList = new StringBuilder();
        for (int i = 0; i <jobs.size(); i++) {
            jobList.append(jobs.get(i).getJobName()+" (");
            jobList.append(jobs.get(i).getStartDate().substring(0,4)+" - ");
            jobList.append(jobs.get(i).getEndDate().substring(0,4)+")");
            if (i != jobs.size() - 1) {
                jobList.append("\n");
            }
        }
        txtJobHistory = new JTextArea(jobList.toString());
        txtJobHistory.setEditable(false);
        txtJobHistory.setBackground(SystemColor.window);

        lblHobbies = new JLabel("Hobbies:");
        List<String> hobbies = database.viewUserHobbies(userID);
        StringBuilder hobbyList = new StringBuilder();
        int hobbiesCount = Math.min(hobbies.size(), 3);
        for (int i = 0; i < hobbiesCount; i++) {
            hobbyList.append(hobbies.get(i));
            if (i != hobbiesCount - 1) {
                hobbyList.append(", ");
            }
        }
        if (hobbies.size() >3) {
            hobbyList.append("...");
        }
        txtHobbies = new JLabel(hobbyList.toString());

        btnEditAcc = new JButton("Edit Account");
        btnEditAcc.addActionListener(this);

        // Add components to the frame
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblName, gbc);
        gbc.gridx = 1;
        panel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblContactNo, gbc);
        gbc.gridx = 1;
        panel.add(txtContactNo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblDOB, gbc);
        gbc.gridx = 1;
        panel.add(txtDOB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lblGender, gbc);
        gbc.gridx = 1;
        panel.add(txtGender,gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(lblAddress, gbc);
        gbc.gridx = 1;
        panel.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(lblJobHistory, gbc);
        gbc.gridx = 1;
        panel.add(txtJobHistory,gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(lblHobbies, gbc);
        gbc.gridx = 1;
        panel.add(txtHobbies,gbc);

        gbc.gridx = 2;
        gbc.gridy = 9;
        panel.add(btnEditAcc,gbc);

        // Set frame properties
        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEditAcc) {
            new EditAccountPage(username);
            dispose();
        }
    }
}
