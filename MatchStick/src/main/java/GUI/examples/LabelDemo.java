package GUI.examples;

import java.awt.*;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

/*
 * LabelDemo.java needs one other file:
 *   images/middle.gif
 */
public class LabelDemo extends JPanel {
    private String localDir = System.getProperty("user.dir");
    private String imagePath = localDir + "\\src\\main\\resources\\MatchstickNumbers\\";
    private JFrame frame;
    //private JLabel label0, label1, label2, label3, label4,label5, label6, label7, label8, label9, labelM, labelP, labelE

    public LabelDemo() {
        super(new GridLayout(1,1));  //1 row, 1 column
        frame = new JFrame("Solution");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //creates and adds an image corresponding to the input to the JFrame
    private void setUpJLabel(JPanel panel, String element){
        ImageIcon img = new ImageIcon(imagePath+ element+".png");
        JLabel label = new JLabel("", img, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        panel.add(label);
    }
    //creates a new JFrame to be used for displaying the answers
    public void createUpJFrame(){

    }
    //Adds all the images corresponding to solution the frame
    public void convertToImage(String[] solution) {
//        JFrame frame = new JFrame("Solution");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        for(int i = 0; i < solution.length;i++){
            setUpJLabel(panel,solution[i]);
        };
        frame.add(panel);
    }
    public void displayAnswers(int numOfSolutions){
        frame.setBackground(Color.WHITE);
        frame.setLayout(new GridLayout(numOfSolutions,1));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
  /*
    public void convertToImage(String[] solution){
        //Create and set up the window.
        JFrame frame = new JFrame("Solution");
        // {4,+,0,=,4}
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        for(int i = 0; i < solution.length;i++){
            String currentElement = solution[i];
            JLabel currentLabel = null;
            if(currentElement.equals("0")){
                currentLabel = label0;
            } else if(currentElement.equals("1")){
                currentLabel = label1;
            } else if(currentElement.equals("2")){
                currentLabel = label2;
            } else if(currentElement.equals("3")){
                currentLabel = label3;
            } else if(currentElement.equals("4")){
                currentLabel = label4.;
            } else if(currentElement.equals("5")){
                currentLabel = label5;
            } else if(currentElement.equals("6")){
                currentLabel = label6;
            } else if(currentElement.equals("7")){
                currentLabel = label7;
            } else if(currentElement.equals("8")){
                currentLabel = label8;
            } else if(currentElement.equals("9")){
                currentLabel = label9;
            } else if(currentElement.equals("-")){
                currentLabel = labelM;
            } else if(currentElement.equals("+")){
                currentLabel = labelP;
            } else if(currentElement.equals("=")){
                currentLabel = labelE;
            }
            this.add(currentLabel);
        }
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    } */
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
//    private static void createAndShowGUI() {
//        //Create and set up the window.
//        JFrame frame = new JFrame("Solution");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //Add content to the window.
//        //frame.getContentPane().setBackground(Color.red);
//        frame.add(new LabelDemo());
//
//
//        //Display the window.
//        frame.pack();
//        frame.setVisible(true);
//    }

//    public static void main(String[] args) {
//        //  System.out.println(localDir);
//        //Schedule a job for the event dispatch thread:
//        //creating and showing this application's GUI.
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                //Turn off metal's use of bold fonts
//                UIManager.put("swing.boldMetal", Boolean.FALSE);
//
//                createAndShowGUI();
//            }
//        });
//    }
}
