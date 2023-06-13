import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class SendMessageDialog extends JDialog implements ActionListener {
    private final JLabel lblFrom, lblTo, lblMessage;
    private final JTextField txtTo;
    private final JTextArea txtMessage;
    private final JButton btnSend;
    private final int userId;
    private final Database database;
    public SendMessageDialog(Frame parent, int userId, int friendId){
        super(parent,"Send Message",true);
        this.userId = userId;
        this.database = new Database();

        lblFrom = new JLabel("From User ID: " + userId);
        lblTo = new JLabel("To User ID: ");
        lblMessage = new JLabel("Message: ");
        if (friendId != -1){
            txtTo = new JTextField(String.valueOf(friendId),20);
        }
        else {
            txtTo = new JTextField(20);
        }
        txtMessage = new JTextArea(5,20);
        txtMessage.setLineWrap(true);
        txtMessage.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(txtMessage);
        messageScrollPane.setPreferredSize(new Dimension(220,100));
        btnSend = new JButton("Send");
        btnSend.addActionListener(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridy = 0;
        panel.add(lblFrom,gbc);
        gbc.gridy = 1;
        panel.add(lblTo,gbc);
        gbc.gridy = 2;
        panel.add(txtTo,gbc);
        gbc.gridy = 3;
        panel.add(lblMessage,gbc);
        gbc.gridy = 4;
        panel.add(messageScrollPane,gbc);
        gbc.gridy = 5;
        panel.add(btnSend,gbc);

        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(parent);
    }
    public SendMessageDialog(Frame parent, int userId){
        this(parent,userId,-1);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnSend){
            String toText = txtTo.getText().trim();
            if (toText.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter a User ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int to = Integer.parseInt(toText);
                if (database.userExists(to)){
                    String message = txtMessage.getText();
                    if (message.length() == 0){
                        JOptionPane.showMessageDialog(this, "Please enter message.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (message.length() <= 200){
                        Notification notification = new Notification(userId,to,message);
                        database.createNotification(notification);
                        JOptionPane.showMessageDialog(this, "Message sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "Message is too long! It must be less than or equal to 200 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(this, "User does not exist! Please reenter User ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    txtTo.setText("");
                }
            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Invalid User ID! Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
                txtTo.setText("");
            }
        }
    }
}
