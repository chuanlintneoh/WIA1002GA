import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddJobDialog extends JDialog implements ActionListener {
    private JLabel lblYourJobs, lblOther;
    private JTextField txtJob;
    private JButton btnStartDate, btnEndDate, btnAdd, btnRemove, btnSave, btnCancel;
    private DefaultListModel<String> jobListModel;
    private JList<String> jobJList;
    private JScrollPane jobScrollPane;
    public AddJobDialog(Frame parent, int userID){
        super(parent,"Jobs",true);
        lblYourJobs = new JLabel("Your Jobs: ");
        lblOther = new JLabel("Other jobs: ");

        txtJob = new JTextField();
        txtJob.setPreferredSize(new Dimension(150, 25));

        Database database = new Database();
        java.util.List<Job> oriJobs = database.viewUserJobs(userID);
        jobListModel = new DefaultListModel<>();
        jobJList = new JList<>(jobListModel);
        jobJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jobScrollPane = new JScrollPane(jobJList);
//            jobScrollPane.setPreferredSize(new Dimension(100, 25));
        for(Job oriJob : oriJobs){
            String jobName = oriJob.getJobName();
            String startDate = oriJob.getStartDate();
            String endDate = oriJob.getEndDate();
            String jobDetails = String.format("%s, Start Date: %s, End Date: %s",jobName,startDate,endDate);
            jobListModel.addElement(jobDetails);
        }
        jobScrollPane = new JScrollPane(jobJList);

        btnAdd = new JButton("Add Job");
        btnAdd.addActionListener(this);
        btnAdd.setBackground(Color.black);
        btnAdd.setForeground(Color.white);

        btnStartDate = new JButton("Start Date");
        btnStartDate.addActionListener(this);
        btnEndDate = new JButton("End Date");
        btnEndDate.addActionListener(this);

        btnRemove = new JButton("Remove Job");
        btnRemove.addActionListener(this);
        btnRemove.setBackground(Color.black);
        btnRemove.setForeground(Color.white);

        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        btnSave.setBackground(new Color(46, 138, 87));
        btnSave.setForeground(Color.white);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBackground(new Color(200, 7, 14));
        btnCancel.setForeground(Color.white);

        // Create mini panels to hold the content, then combine the panels into a larger panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10,10,10,10));
        contentPanel.add(lblYourJobs);
        contentPanel.add(jobScrollPane);

        JPanel buttonPanel = new JPanel();
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.add(lblOther);
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.add(txtJob);
        buttonPanel2.add(btnStartDate);
        buttonPanel2.add(btnEndDate);
        JPanel buttonPanel3 = new JPanel();
        buttonPanel3.add(btnAdd);
        buttonPanel3.add(btnRemove);
        buttonPanel.add(buttonPanel1,BorderLayout.NORTH);
        buttonPanel.add(buttonPanel2,BorderLayout.CENTER);
        buttonPanel.add(buttonPanel3,BorderLayout.SOUTH);

        JPanel closePanel = new JPanel();
        closePanel.add(btnSave);
        closePanel.add(btnCancel);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialogPanel.add(contentPanel,BorderLayout.NORTH);
        dialogPanel.add(buttonPanel,BorderLayout.CENTER);
        dialogPanel.add(closePanel,BorderLayout.SOUTH);
        setContentPane(dialogPanel);
        pack();
        setSize(500, 380);
        setLocationRelativeTo(parent);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd){
            String job = txtJob.getText().trim();
            if (!job.isEmpty() && !jobListModel.contains(job)){

            }
        }
        else if (e.getSource() == btnStartDate){
            SetDateDialog startDate = new SetDateDialog(this,btnStartDate);
            startDate.setVisible(true);
        }
        else if (e.getSource() == btnEndDate){
            SetDateDialog endDate = new SetDateDialog(this,btnEndDate);
            endDate.setVisible(true);
        }
        else if (e.getSource() == btnRemove){

        }
        else if (e.getSource() == btnSave){

        }
        else if (e.getSource() == btnCancel){

        }
    }
}
