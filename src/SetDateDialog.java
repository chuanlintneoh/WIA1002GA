import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;
public class SetDateDialog extends JDialog implements ActionListener {
    private final JLabel lblDay, lblMonth, lblYear;
    private final JComboBox<Integer> dateDay, dateYear;
    private final JComboBox<String> dateMonth;
    private final JButton btnSet, btnCancel;
    private boolean isDateSelected;
    private String selectedDate;
    private final JButton btn;
    public SetDateDialog(AddJobDialog parent, JButton btn){
        super(parent,"Set Date",true);
        this.btn = btn;
        lblDay = new JLabel("Day:");
        lblMonth = new JLabel("Month:");
        lblYear = new JLabel("Year:");

        Integer[] days = new Integer[31];
        for (int i = 1; i <= days.length; i++){
            days[i-1] = i;
        }
        dateDay = new JComboBox<>(days);
        dateMonth = new JComboBox<>(new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"});
        Integer[] years = new Integer[100];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 1; i <= years.length; i++){
            years[i-1] = currentYear-100+i;
        }
        dateYear = new JComboBox<>(years);

        btnSet = new JButton("Set");
        btnSet.addActionListener(this);
        btnSet.setBackground(new Color(46, 138, 87));
        btnSet.setForeground(Color.white);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnCancel.setBackground(new Color(200, 7, 14));
        btnCancel.setForeground(Color.white);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(4, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialogPanel.add(lblDay);
        dialogPanel.add(dateDay);
        dialogPanel.add(lblMonth);
        dialogPanel.add(dateMonth);
        dialogPanel.add(lblYear);
        dialogPanel.add(dateYear);
        dialogPanel.add(btnSet);
        dialogPanel.add(btnCancel);

        setContentPane(dialogPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(parent);
    }
    public boolean isDateSelected(){
        return isDateSelected;
    }
    public String getSelectedDate(){
        return selectedDate;
    }
    private boolean isValidDate(int day, int month, int year) {

        int[] daysInMonth = {31, isLeapYear(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return day >= 1 && day <= daysInMonth[month - 1];
    }
    private boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSet){
            int day = (int) dateDay.getSelectedItem();
            int year = (int) dateYear.getSelectedItem();
            String monthString = (String) dateMonth.getSelectedItem();
            int month = Arrays.asList(new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec"}).indexOf(monthString);

            if (!isValidDate(day, month + 1, year)) {
                JOptionPane.showMessageDialog(this, "Invalid date selected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
            isDateSelected = true;
            btn.setText(selectedDate);
        }
        else if (e.getSource() == btnCancel){
            isDateSelected = false;
        }
        dispose();
    }
}