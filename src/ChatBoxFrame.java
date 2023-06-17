import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        txtmessage = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

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
        for (Message message: messages){
            String sender = database.get("username",message.getFrom());
            String timeStamp = message.getTimeStamp();
            String text = message.getText();
            String formattedMessage = "";
            if (message.getFrom() == friendID){
                formattedMessage = sender + ":\n" + text + "\n[" + timeStamp + "]\n";
            }// align left
            else if (message.getTo() == friendID){
                formattedMessage = sender + ":\n" + text + "\n[" + timeStamp + "]\n";
            }// align right
            chatArea.append(formattedMessage + "\n");
        }
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
