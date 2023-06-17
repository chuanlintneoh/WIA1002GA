import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultListModel;

public class ChatBoxFrame extends JFrame implements ActionListener{
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private DefaultListModel<String> chatListModel;

    private int userID;
    private int friendID;
    private Database database;

    public ChatBoxFrame(Frame parent, int userID, int friendID) {
        this.userID = userID;
        this.friendID = friendID;
        this.database = new Database();

        chatListModel = new DefaultListModel<>();

        initializeUI();
        loadMessages();

        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        setTitle("Chat Room with " + database.get("username", friendID));
        setPreferredSize(new Dimension(400, 300));

        chatArea = new JTextArea(1, 20);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        messageField = new JTextField();
        sendButton = new JButton("Send");

        sendButton.addActionListener(this);
//        sendButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String text = messageField.getText();
//                if (!text.isEmpty()) {
//                    Message message = new Message(userID, friendID, text);
//                    database.sendMessage(message);
//
//                    String displayMessage = "[" + message.getTimeStamp() + "] You: " + message.getText();
//                    chatListModel.addElement(displayMessage);
//                    chatArea.append(displayMessage + "\n"); // Append the message directly to the chatArea
//
//                    messageField.setText("");
//                }
//            }
//        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void loadMessages() {
        List<Message> messages = database.retrieveMessage(userID, friendID);

        for (Message message : messages) {
            String displayMessage = "[" + message.getTimeStamp() + "] " +
                    getUserName(message.getFrom()) + ": " +
                    message.getText();
            chatListModel.addElement(displayMessage);
        }
        chatArea.setText(chatListModel.toString());
    }

    private String getUserName(int userId) {
        return database.get("username", userId);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == sendButton){
            String toText = messageField.getText().trim();
            database.sendMessage(new Message(userID,friendID,toText));
            messageField.setText("");

            loadMessages();
        }
    }
}
