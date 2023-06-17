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
    private final JTextArea chatArea;
    private final JTextField txtmessage;
    private final JButton sendButton;
    private final Timer timer;
    public ChatBoxFrame(Frame parent, int userID, int friendID) {
        this.userID = userID;
        this.friendID = friendID;
        this.database = new Database();

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        chatArea.setBackground(new Color(238,232,182));
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

        JScrollPane scrollPane = new JScrollPane(chatArea);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(txtmessage,BorderLayout.CENTER);
        messagePanel.add(sendButton,BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane,BorderLayout.CENTER);
        panel.add(messagePanel,BorderLayout.SOUTH);

        setContentPane(panel);
        setTitle("Conversation with " + database.get("username",friendID));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setVisible(true);

        timer = new Timer(30000, this);// refresh page every 30 seconds
        timer.setInitialDelay(0);
        timer.start();

        refreshMessages();
    }
    private void refreshMessages(){
        LinkedList<Message> messages = (LinkedList<Message>) database.retrieveMessage(userID,friendID);
        chatArea.setText("");
        for (Message message : messages) {
            String sender = database.get("username", message.getFrom());
            String timeStamp = message.getTimeStamp();
            String text = message.getText();
            String formattedMessage = "";

            if (message.getFrom() == friendID) {
//                formattedMessage = sender + ":\n" + text + "\n[" + timeStamp + "]\n";
                formattedMessage = sender + " : " + text;
                formattedMessage = alignLeft(formattedMessage); // Align left
            } else if (message.getTo() == friendID) {
                formattedMessage = text;
                formattedMessage = alignRight(formattedMessage); // Align right
            }

            chatArea.append(formattedMessage + "\n");
        }

    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == sendButton|| e.getSource() == txtmessage){
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
    private String alignLeft(String message) {
        int lineWidth = 80; // Adjust this value based on your desired line width
        StringBuilder builder = new StringBuilder();

        String[] lines = message.split("\n");
        for (String line : lines) {
            int padding = lineWidth - line.length();
            String paddedLine = line + " ".repeat(padding);
            builder.append(paddedLine).append("\n");
        }

        return builder.toString();
    }

    private String alignRight(String message) {
        int lineWidth = 90; // Adjust this value based on your desired line width
        StringBuilder builder = new StringBuilder();

        String[] lines = message.split("\n");
        for (String line : lines) {
            int padding = lineWidth - line.length();
            String paddedLine = " ".repeat(padding) + line;
            builder.append(paddedLine).append("\n");
        }

        return builder.toString();
    }

}
