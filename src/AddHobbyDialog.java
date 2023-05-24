import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddHobbyDialog extends JDialog implements ActionListener {
    private final JLabel lblSelect,lblOther,lblYourHobby;
    private final JCheckBox[] checkBoxes;
    private final JButton btnSave,btnCancel,btnAdd,btnRemove,btnConfirm;
    private final String[] hobbies = {"Reading", "Gardening", "Cooking", "Painting", "Photography"};
    private JScrollPane hobbyScrollPane;
    private final JList<String> hobbyJList;
    private final DefaultListModel<String> hobbyListModel;
    private final JTextField hobbyTextField;
    private final int userID;
    private final Database database;
    public AddHobbyDialog(Frame parent, int userID) {
        super(parent,"Hobbies",true);
        this.userID = userID;
        database = new Database();

        lblYourHobby = new JLabel("Your Hobbies:");
        lblSelect = new JLabel("Select your hobbies:");
        lblOther = new JLabel("Other hobbies:");

        // Create hobby text field
        hobbyTextField = new JTextField(20);

        java.util.List<String> oriHobbies = database.viewUserHobbies(userID);
        hobbyListModel = new DefaultListModel<>();
        hobbyJList = new JList<>(hobbyListModel);
        hobbyJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        hobbyScrollPane = new JScrollPane(hobbyJList);
        hobbyScrollPane.setPreferredSize(new Dimension(150, 25));
        for(String oriHobby : oriHobbies){
            hobbyListModel.addElement(oriHobby);
        }
        hobbyScrollPane = new JScrollPane(hobbyJList);
        checkBoxes = new JCheckBox[hobbies.length];

        btnConfirm = new JButton("Confirm Selection");
        btnConfirm.addActionListener(this);
        btnConfirm.setBackground(new Color(200,140,10));
        btnConfirm.setForeground(Color.white);

        btnAdd = new JButton("Add Hobby");
        btnAdd.addActionListener(this);
        btnAdd.setBackground(Color.black);
        btnAdd.setForeground(Color.white);

        btnRemove = new JButton("Remove Hobby");
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

        // Create a panel to hold the content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(10,10,10,10));
        contentPanel.add(lblYourHobby);
        contentPanel.add(hobbyScrollPane);
        contentPanel.add(lblSelect);
        for (int i = 0; i < hobbies.length; i++) {
            checkBoxes[i] = new JCheckBox(hobbies[i]);
            contentPanel.add(checkBoxes[i]);
        }
        contentPanel.add(btnConfirm);

        JPanel buttonPanel = new JPanel();
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.add(lblOther);
        buttonPanel1.add(hobbyTextField);
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.add(btnAdd);
        buttonPanel2.add(btnRemove);
        buttonPanel.add(buttonPanel1);
        buttonPanel.add(buttonPanel2);
        buttonPanel.add(buttonPanel1,BorderLayout.NORTH);
        buttonPanel.add(buttonPanel2,BorderLayout.CENTER);

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
        setSize(400, 550);
        setLocationRelativeTo(parent);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnAdd) {
            String hobby = hobbyTextField.getText().trim();
            if (!hobby.isEmpty() && !hobbyListModel.contains(hobby)) {
                hobbyListModel.addElement(hobby);
            }hobbyTextField.setText("");
        }if (e.getSource() == btnRemove) {
            int[] selectedIndices = hobbyJList.getSelectedIndices();
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                hobbyListModel.remove(selectedIndices[i]);
            }
        }if(e.getSource() == btnConfirm){
            List<String> selectedHobbies = new ArrayList<>();
            // Check if the selected hobbies are not already in the hobbyListModel
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    String hobby = checkBox.getText();
                    if (!hobbyListModel.contains(hobby)) {
                        selectedHobbies.add(hobby);
                    }
                } else {
                    String hobby = checkBox.getText();
                    if (hobbyListModel.contains(hobby)) {
                        hobbyListModel.removeElement(hobby);
                    }
                }
            }
            // Add the selected hobbies to the hobbyListModel
            for (String hobby : selectedHobbies) {
                hobbyListModel.addElement(hobby);
            }
        }if (e.getSource() == btnCancel) {
            JOptionPane.showMessageDialog(this, "Your hobbies are NOT changed.");
            dispose();
        }
        if (e.getSource() == btnSave) {
            if (hobbyListModel.isEmpty()) {
                int choice = JOptionPane.showOptionDialog(this, "Nothing is saved as your hobby. \nAre you sure you want to quit?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
                if (choice == JOptionPane.YES_OPTION) {
                    java.util.List<String> hobbies= new ArrayList<>();
                    database.editUserHobbies(userID,hobbies);      //send null set
                    dispose();
                } else {
                    return;
                }
            } else {
                List<String> hobbies= new ArrayList<>();
                for(int i=0;i<hobbyListModel.getSize();i++){
                    hobbies.add(hobbyListModel.getElementAt(i));
                }
                database.editUserHobbies(userID,hobbies);
                JOptionPane.showMessageDialog(this, "Your hobbies are SUCCESSFULLY saved!","Success",JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        }
    }
}
