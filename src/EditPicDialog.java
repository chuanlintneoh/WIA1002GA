import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
public class EditPicDialog extends JDialog implements ActionListener{
    private final int userID;
    private final JButton btnUpload, btnDelete, btnClose;
    private final JLabel lblProfilePicture;
    private final Database database;
    public EditPicDialog(Frame parent, int userID, JLabel lblProfilePicture) {
        super(parent, "Edit Profile Picture", true);
        this.userID = userID;
        this.lblProfilePicture = lblProfilePicture;
        database = new Database();
        btnUpload = new JButton("Upload");
        btnUpload.addActionListener(this);
        btnUpload.setForeground(Color.WHITE);
        btnUpload.setBackground(new Color(46, 138, 87));
        btnDelete = new JButton("Delete");
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setBackground(Color.RED);
        btnDelete.addActionListener(this);
        btnClose = new JButton("Close");
        btnClose.addActionListener(this);

        // Create layout and add components
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialogPanel.add(lblProfilePicture);
        dialogPanel.add(btnUpload);
        dialogPanel.add(btnDelete);
        dialogPanel.add(btnClose);

        // Set dialog properties
        setContentPane(dialogPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(250,350);
        setLocationRelativeTo(parent);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUpload){
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedImage image = ImageIO.read(selectedFile);
                    if (image == null){
                        JOptionPane.showMessageDialog(this, "Invalid image file.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int maxWidth = 150;
                    int maxHeight = 200;
                    int width = image.getWidth();
                    int height = image.getHeight();
                    double widthRatio = (double) maxWidth / width;
                    double heightRatio = (double) maxHeight / height;
                    double scaleRatio = Math.min(widthRatio, heightRatio);
                    int newWidth = (int) (width * scaleRatio);
                    int newHeight = (int) (height * scaleRatio);
                    Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedImage bufferedResizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = bufferedResizedImage.createGraphics();
                    g2d.drawImage(resizedImage, 0, 0, null);
                    g2d.dispose();
                    ImageIO.write(bufferedResizedImage, "jpg", baos);
                    byte[] picture = baos.toByteArray();

                    database.setProfilePicture(picture, userID);
                    lblProfilePicture.setIcon(new ImageIcon(resizedImage));
                    lblProfilePicture.setPreferredSize(new Dimension(newWidth, newHeight));
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getSource() == btnDelete){
            database.deleteProfilePicture(userID);
            lblProfilePicture.setIcon(new ImageIcon("src/default_profile_pic.jpg"));
        }
        else if (e.getSource() == btnClose){
            dispose();
        }
    }
}
