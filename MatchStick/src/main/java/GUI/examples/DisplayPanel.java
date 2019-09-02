package GUI.examples;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DisplayPanel extends JPanel {
    private String localDir = System.getProperty("user.dir");
    private String image = localDir + "\\src\\main\\resources\\MatchstickNumbers";

    public DisplayPanel() {
        ImageIcon icon = new ImageIcon(image+"0.png");
        ImageIcon icon1 = new ImageIcon(image+"1.png");
        ImageIcon icon2 = new ImageIcon(image+"2.png");
        JLabel label1 = new JLabel(icon);
        JLabel label2 = new JLabel(icon1);
        JLabel label3 = new JLabel(icon2);
        //label1.getContentPane().setBackground(Color.black);

        add(label1);
        add(label2);
        add(label3);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new DisplayPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
