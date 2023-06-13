import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class NotificationScrollPane extends JPopupMenu {
    private final JPanel notificationsPanel;
    private final JScrollPane scrollPane;
    private final int userID;
    private final Database database;
    public NotificationScrollPane(int userID){
        this.userID = userID;
        this.database = new Database();
        notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel,BoxLayout.PAGE_AXIS));
        scrollPane = new JScrollPane(notificationsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100);

        ArrayList<Notification> notifications = (ArrayList<Notification>) database.getNotifications(userID);
        for (Notification notification: notifications){
            JButton noti = new JButton("<html><body>" +
                    "From: " + database.get("username", notification.getFrom()) + "<br>" +
                    notification.getDescription() + "<br>" +
                    notification.getDate() +
                    "</body></html>");
            noti.setMaximumSize(new Dimension(245, Integer.MAX_VALUE));
            noti.setHorizontalAlignment(SwingConstants.LEFT);
            noti.setBorderPainted(false);
            noti.setFocusPainted(false);
            noti.setContentAreaFilled(false);
            notificationsPanel.add(noti);
        }

        int contentHeight = notificationsPanel.getPreferredSize().height;
        int preferredHeight = Math.min(contentHeight, 400);
        scrollPane.setPreferredSize(new Dimension(270, preferredHeight));
        add(scrollPane);
    }
}
