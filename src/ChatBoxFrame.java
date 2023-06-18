import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class ChatBoxFrame extends JFrame implements ActionListener{
    private final int userID;
    private final int friendID;
    private final Database database;
    private final JPanel messagesPanel;
    private final JTextField txtmessage;
    private final JButton sendButton;
    private final Timer timer;
    public ChatBoxFrame(Frame parent, int userID, int friendID) {
        this.userID = userID;
        this.friendID = friendID;
        this.database = new Database();

        messagesPanel = new JPanel(new GridBagLayout());
        messagesPanel.setBackground(new Color(200,240,190));
        JScrollPane scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setPreferredSize(new Dimension(375,250));
        scrollPane.getVerticalScrollBar().setBlockIncrement(100);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        txtmessage = new JTextField();
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(46,138,87));
        sendButton.setForeground(Color.white);
        sendButton.addActionListener(this);
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sendButton.setBackground(new Color(20,75,30));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                sendButton.setBackground(new Color(46, 138, 87));
            }
        });

        JPanel sendPanel = new JPanel(new BorderLayout());
        sendPanel.add(txtmessage,BorderLayout.CENTER);
        sendPanel.add(sendButton,BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane,BorderLayout.CENTER);
        panel.add(sendPanel,BorderLayout.SOUTH);

        timer = new Timer(30000, this);// refresh page every 30 seconds
        timer.setInitialDelay(0);
        timer.start();

        refreshMessages();

        setContentPane(panel);
        setTitle("Conversation with " + database.get("username",friendID));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    private void refreshMessages(){
        LinkedList<Message> messages = (LinkedList<Message>) database.retrieveMessage(userID,friendID);
        messagesPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridy = 0;

        for (Message message : messages) {
            String sender = database.get("username", message.getFrom());
            String timeStamp = message.getTimeStamp();
            String text = message.getText();

            JTextArea messageLabel = new JTextArea(sender + ": \n" + text + "\n" + timeStamp);
            messageLabel.setLineWrap(true);
            messageLabel.setWrapStyleWord(true);
            messageLabel.setEditable(false);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            if (message.getFrom() == friendID) {
                messageLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.white,2), // Outer border
                        BorderFactory.createEmptyBorder(5, 10, 5, 10) // Inner padding
                ));
                messageLabel.setBackground(new Color(200,200,160));
                messageLabel.setOpaque(true);
                gbc.gridx = 0;
                gbc.anchor = GridBagConstraints.LINE_START;
            }// received
            else if (message.getTo() == friendID) {
                messageLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.white,2), // Outer border
                        BorderFactory.createEmptyBorder(5, 10, 5, 10) // Inner padding
                ));
                messageLabel.setBackground(new Color(140,200,160));
                messageLabel.setOpaque(true);
                gbc.gridx = 1;
                gbc.anchor = GridBagConstraints.LINE_END;
            }// sent

            gbc.weightx = 0.5;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            messagesPanel.add(messageLabel,gbc);
            gbc.gridy++;
        }
        messagesPanel.revalidate();
        messagesPanel.repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == sendButton){
            String text = txtmessage.getText().trim();
            if (text.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter a message.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                database.sendMessage(new Message(userID,friendID,text));
                txtmessage.setText("");
                refreshMessages();
            }
        }
        else if (e.getSource() == timer){
            refreshMessages();
        }
    }
}
