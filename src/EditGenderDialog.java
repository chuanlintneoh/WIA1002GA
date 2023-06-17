import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class EditGenderDialog extends JDialog implements ActionListener {
    private final JRadioButton radioMale, radioFemale, radioNotSet;
    private final JButton btnSave, btnCancel;
    private final JButton btnGender;
    private final int userID;
    private final Database database;
    private final TracebackFunction tracebackFunction;
    public EditGenderDialog(Frame parent, int userID, JButton btnGender, TracebackFunction tracebackFunction) {
        super(parent, "Edit Gender", true);
        this.userID = userID;
        this.btnGender = btnGender;
        this.database = new Database();
        this.tracebackFunction = tracebackFunction;

        radioMale = new JRadioButton("Male");
        radioFemale = new JRadioButton("Female");
        radioNotSet = new JRadioButton("Prefer not to tell");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(radioMale);
        genderGroup.add(radioFemale);
        genderGroup.add(radioNotSet);

        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        btnSave.setBackground(new Color(46, 138, 87));
        btnSave.setForeground(Color.white);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBackground(new Color(200, 7, 14));
        btnCancel.setForeground(Color.white);

        // Create layout and add components
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(5,5,10,10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        dialogPanel.add(radioMale);
        dialogPanel.add(radioFemale);
        dialogPanel.add(radioNotSet);
        dialogPanel.add(btnSave);
        dialogPanel.add(btnCancel);

        setContentPane(dialogPanel);
        pack();
        setLocationRelativeTo(parent);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Save")) {
            String gender;
            if (radioMale.isSelected()) {
                gender = "M";
            } else if (radioFemale.isSelected()) {
                gender = "F";
            } else if(radioNotSet.isSelected()){
                gender = "-";
            }else{
                JOptionPane.showMessageDialog(this,"Please choose an option.");
                return;
            }
            database.set("gender",gender, userID);
            tracebackFunction.addHistory("Changed gender.");
            btnGender.setText(gender);
            JOptionPane.showMessageDialog(this, "Your gender is changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            JOptionPane.showMessageDialog(this, "Your gender is NOT changed", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        dispose(); // Close the dialog
    }
}