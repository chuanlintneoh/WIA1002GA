import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
public class EditBirthdayDialog extends JDialog implements ActionListener {
    private final JComboBox<Integer> birthDay;
    private final JComboBox<String> birthMonth;
    private final JComboBox<Integer> birthYear;
    private final Database database;
    private final int userID;
    private final JButton btnDOB;
    public EditBirthdayDialog(Frame parent, int userID, JButton btnDOB) {
        super(parent, "Edit Birthday", true);
        this.database = new Database();
        this.userID = userID;
        this.btnDOB = btnDOB;

        // Initialize components
        birthDay = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            birthDay.addItem(i);
        }

        birthMonth = new JComboBox<>(new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"});

        birthYear = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 100; i <= currentYear; i++) {
            birthYear.addItem(i);
        }

        // Create layout and add components
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(4, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialogPanel.add(new JLabel("Birth Day:"));
        dialogPanel.add(birthDay);

        dialogPanel.add(new JLabel("Birth Month:"));
        dialogPanel.add(birthMonth);

        dialogPanel.add(new JLabel("Birth Year:"));
        dialogPanel.add(birthYear);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        dialogPanel.add(btnSave);
        btnSave.setBackground(new Color(46, 138, 87));
        btnSave.setForeground(Color.white);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        dialogPanel.add(btnCancel);
        btnCancel.setBackground(new Color(200, 7, 14));
        btnCancel.setForeground(Color.white);

        // Set dialog properties
        setContentPane(dialogPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(parent);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Save")) {
            int selectedDay = (int) birthDay.getSelectedItem();
            String selectedMonth = (String) birthMonth.getSelectedItem();
            int selectedYear = (int) birthYear.getSelectedItem();

            if (!isValidDate(selectedDay, selectedMonth, selectedYear)) {
                JOptionPane.showMessageDialog(this, "Invalid date selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String formattedDate = String.format("%04d-%02d-%02d", selectedYear, getMonthNumber(selectedMonth), selectedDay);
            JOptionPane.showMessageDialog(this, "Your birthdate has been changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            btnDOB.setText(formattedDate);
            database.set("birthdate", formattedDate, userID);
        } else if (e.getActionCommand().equals("Cancel")) {
            JOptionPane.showMessageDialog(this, "Your birthdate has not been changed.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        dispose();
    }

    public boolean isValidDate(int day, String month, int year) {
        int monthNumber = getMonthNumber(month);

        if (monthNumber == 2) {
            if (isLeapYear(year)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        } else if (monthNumber == 4 || monthNumber == 6 || monthNumber == 9 || monthNumber == 11) {
            return day <= 30;
        } else {
            return true;
        }
    }
    public boolean isLeapYear(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 100 != 0) {
            return true;
        } else {
            return year % 400 == 0;
        }
    }
    private int getMonthNumber(String month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(month)) {
                return i + 1; // Month numbers start from 1
            }
        }
        return -1; // Invalid month

    }
}
