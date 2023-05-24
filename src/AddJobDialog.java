import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

public class AddJobDialog extends JDialog implements ActionListener {
    private JLabel lblYourJobs, lblOther;
    private JComboBox<String> jobComboBox;
    private JButton btnStartDate, btnEndDate, btnPush, btnPop, btnClose;
    private DefaultListModel<String> jobListModel;
    private JList<String> jobJList;
    private JScrollPane jobScrollPane;
    private Stack<Job> oriJobs;
    private final int userId;
    private final Database database;
    private final Frame parent;
    public AddJobDialog(Frame parent, int userId){
        super(parent,"Jobs",true);
        this.parent = parent;
        this.userId = userId;
        lblYourJobs = new JLabel("Your Jobs: ");
        lblOther = new JLabel("Other jobs: ");

        database = new Database();
        ArrayList<Job> jobs = (ArrayList<Job>) database.getJobs();
        jobComboBox = new JComboBox<>();
        for (Job job: jobs){
            jobComboBox.addItem(job.getJobName());
        }

        oriJobs = database.viewUserJobs(userId);
        jobListModel = new DefaultListModel<>();
        jobJList = new JList<>(jobListModel);
        jobScrollPane = new JScrollPane(jobJList);
//            jobScrollPane.setPreferredSize(new Dimension(100, 25));
        for(Job oriJob : oriJobs){
            String jobName = oriJob.getJobName();
            String startDate = oriJob.getStartDate();
            String endDate = oriJob.getEndDate();
            String jobDetails = String.format("\t %s: \t\t | Start Date: %s | End Date: %s",jobName,startDate,endDate);
            jobListModel.addElement(jobDetails);
        }
        jobScrollPane = new JScrollPane(jobJList);

        btnPush = new JButton("Push");
        btnPush.addActionListener(this);
        btnPush.setBackground(new Color(46, 138, 87));
        btnPush.setForeground(Color.white);

        btnStartDate = new JButton("Start Date");
        btnStartDate.addActionListener(this);
        btnEndDate = new JButton("End Date");
        btnEndDate.addActionListener(this);

        btnPop = new JButton("Pop");
        btnPop.addActionListener(this);
        btnPop.setBackground(new Color(200, 7, 14));
        btnPop.setForeground(Color.white);

        btnClose = new JButton("Close");
        btnClose.addActionListener(this);
        btnClose.setBackground(Color.black);
        btnClose.setForeground(Color.white);

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
        buttonPanel2.add(jobComboBox);
        buttonPanel2.add(btnStartDate);
        buttonPanel2.add(btnEndDate);
        JPanel buttonPanel3 = new JPanel();
        buttonPanel3.add(btnPush);
        buttonPanel3.add(btnPop);
        buttonPanel.add(buttonPanel1,BorderLayout.NORTH);
        buttonPanel.add(buttonPanel2,BorderLayout.CENTER);
        buttonPanel.add(buttonPanel3,BorderLayout.SOUTH);

        JPanel closePanel = new JPanel();
        closePanel.add(btnClose);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialogPanel.add(contentPanel,BorderLayout.NORTH);
        dialogPanel.add(buttonPanel,BorderLayout.CENTER);
        dialogPanel.add(closePanel,BorderLayout.SOUTH);
        setContentPane(dialogPanel);
        pack();
        setSize(500, 450);
        setLocationRelativeTo(parent);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnStartDate){
            SetDateDialog startDate = new SetDateDialog(this,btnStartDate);
            startDate.setVisible(true);
        }
        else if (e.getSource() == btnEndDate){
            SetDateDialog endDate = new SetDateDialog(this,btnEndDate);
            endDate.setVisible(true);
        }
        else if (e.getSource() == btnPush){
            String job = (String) jobComboBox.getSelectedItem();
            if (job != null){
                int jobId = database.getJobId(job);
                String startDate = btnStartDate.getText();
                String endDate = btnEndDate.getText();
                if (startDate.equals("Start Date")){
                    startDate = null;
                }
                if (endDate.equals("End Date")){
                    endDate = null;
                }
                Job newJob = new Job(jobId,startDate,endDate);
                database.pushUserJob(userId,newJob);
                jobListModel.addElement(String.format("\t %s \t\t | Start Date: %s | End Date: %s",job,startDate,endDate));
                btnStartDate.setText("Start Date");
                btnEndDate.setText("End Date");
            }
        }
        else if (e.getSource() == btnPop){
            database.popUserJob(userId);
            if (!jobListModel.isEmpty()){
                jobListModel.removeElement(jobListModel.lastElement());
            }
        }
        else if (e.getSource() == btnClose){
            dispose();
        }
    }
}
