import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.awt.Color.*;
public class AccountMenu extends JPopupMenu implements ActionListener {
    private final JButton btnViewAcc, btnEditAcc, btnHistory, btnLogOut;
    private final String username;
    private final TracebackFunction tracebackFunction;
    private final JFrame parent;
    public AccountMenu(String username, TracebackFunction tracebackFunction, JFrame parent){
        this.username = username;
        this.tracebackFunction = tracebackFunction;
        this.parent = parent;

        btnViewAcc = new JButton("View Account");
        btnEditAcc = new JButton("Edit Account");
        btnHistory = new JButton("History");
        btnLogOut = new JButton("Log Out");

        btnViewAcc.addActionListener(this);
        btnEditAcc.addActionListener(this);
        btnHistory.addActionListener(this);
        btnLogOut.addActionListener(this);

        btnViewAcc.setForeground(new Color(58,30,0));
        btnViewAcc.setBackground(new Color(196, 164, 132));
        btnViewAcc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnViewAcc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnViewAcc.setForeground(WHITE); // Change to the desired color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnViewAcc.setForeground(new Color(58,30,0)); // Change back to the default color
            }
        });
        btnEditAcc.setForeground(new Color(58,30,0));
        btnEditAcc.setBackground(new Color(196, 164, 132));
        btnEditAcc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnEditAcc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnEditAcc.setForeground(WHITE); // Change to the desired color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnEditAcc.setForeground(new Color(58,30,0)); // Change back to the default color
            }

        });
        btnHistory.setForeground(new Color(58,30,0));
        btnHistory.setBackground(new Color(196, 164, 132));
        btnHistory.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnHistory.setForeground(WHITE); // Change to the desired color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnHistory.setForeground(new Color(58,30,0)); // Change back to the default color
            }

        });
        btnLogOut.setForeground(new Color(70,13,13));
        btnLogOut.setBackground(new Color(196, 164, 132));
        btnLogOut.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnLogOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogOut.setForeground(WHITE); // Change to the desired color
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogOut.setForeground(new Color(70,13,13)); // Change back to the default color
            }
        });

        setBackground(new Color(196,164,132));
        add(btnViewAcc);
        add(btnEditAcc);
        add(btnHistory);
        add(btnLogOut);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnViewAcc){
            //ViewAccountPage (own account)
            tracebackFunction.pushPage(new ViewAccountPage(username,0,tracebackFunction));
            parent.dispose();
        }
        else if (e.getSource() == btnEditAcc){
            //EditAccountPage
            tracebackFunction.pushPage(new EditAccountPage(username,tracebackFunction));
            parent.dispose();
        }
        else if (e.getSource() == btnHistory){
            PageHistoryFrame pageHistory = new PageHistoryFrame(parent,tracebackFunction.getPageHistory());
        }
        else if (e.getSource() == btnLogOut){
            //LoginPage
            tracebackFunction.clear();
            new LoginPage(tracebackFunction);
            parent.dispose();
        }
    }
}
