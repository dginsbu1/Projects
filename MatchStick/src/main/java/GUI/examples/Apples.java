package GUI.examples;

import java.awt.*;
import java.awt.GridLayout;
import javax.swing.*;

/*
 * LabelDemo.java needs one other file:
 *   images/middle.gif
 */
public class Apples extends JPanel {
    private String localDir = System.getProperty("user.dir");
    private String imagePath = localDir + "\\src\\main\\resources\\MatchstickNumbers\\";
    private JLabel label0, label1, label2, label3, label4,label5, label6, label7, label8, label9, labelM, labelP, labelE;

    public Apples() {
        super(new GridLayout(1,1));  //1 row, 1 column
        JLabel label0, label1, label2, label3, label4,label5, label6, label7, label8, label9, labelM, labelP, labelE;
        ImageIcon img0 = new ImageIcon(imagePath+  "0.png");
        ImageIcon img1 = new ImageIcon(imagePath+ "1.png");
        label0 = new JLabel("", img0, JLabel.CENTER);
        label1 = new JLabel("", img1, JLabel.CENTER);
        label0.setOpaque(true);
        label1.setOpaque(true);
        label0.setBackground(Color.WHITE);
        label1.setBackground(Color.WHITE);
        this.add(label0);
        this.add(label1);

    }
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Solution");
        //frame.setLayout(new GridLayout(2,1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new Apples());
        frame.getContentPane().add(new Apples());
        //Display the window.
        frame.setLayout(new GridLayout(2,1));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //  System.out.println(localDir);
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);

                createAndShowGUI();
            }
        });
    }
    //creates and adds an image corresponding to the input to the JFrame
    private void setUpJLabel(String element){
        ImageIcon img = new ImageIcon(imagePath+ element+".png");
        JLabel label = new JLabel("", img, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        this.add(label);
    }
    //Adds all the images corresponding to solution and displays them
    public void convertToImage(String[] solution) {
        JFrame frame = new JFrame("Solution");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        for(int i = 0; i < solution.length;i++){
            setUpJLabel(solution[i]);
        }
        frame.add(this);
        frame.add(this);
        frame.pack();
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

}
