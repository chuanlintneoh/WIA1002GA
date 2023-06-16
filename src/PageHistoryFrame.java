import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
public class PageHistoryFrame extends JFrame {
    private final JFrame parent;
    private final LinkedList<String> pageHistory;
    private final JScrollPane scrollPane;
    private final JTextArea history;
    public PageHistoryFrame(JFrame parent, LinkedList<String> pageHistory){
        super("Page History");
        this.pageHistory = pageHistory;
        this.parent = parent;

        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(400,300));

        history = new JTextArea();
        history.setEditable(false);

        for (String entry: pageHistory){
            history.append(entry + "\n");
        }

        scrollPane.setViewportView(history);

        getContentPane().add(scrollPane);

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}