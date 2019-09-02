package GUI;

import java.awt.*;
import javax.swing.*;

public class SolutionDisplay{
    private String localDir = System.getProperty("user.dir");
    private String imagePath = localDir + "\\src\\main\\resources\\MatchstickNumbers\\";
    private JFrame solutionFrame;
    private String title;

    //creates a JFrame that will display all the solutions
    //Sets Title to Solution(s)
    public SolutionDisplay() {
        solutionFrame = new JFrame("Solution(s)");
    }
    //creates a JFrame that will display all the solutions
    //JFrame Title is set to the input
    public SolutionDisplay(String title) {
        solutionFrame = new JFrame(title);
    }
    //creates a JLabel from element and adds it to the JPanel panel
    private JLabel setUpJLabel(String element){
        ImageIcon img = new ImageIcon(imagePath+ element+".png");
        JLabel label = new JLabel("", img, JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        return label;
    }
    //converts the solution to a JPanel and adds it to the JFrame
    public void convertToImage(String[] solution) {
        JPanel panelSolution = createJPanel();
        for(int i = 0; i < solution.length;i++){
            panelSolution.add(setUpJLabel(solution[i]));
        }
        solutionFrame.add(panelSolution);
    }
    public void setTitle(String title){
        solutionFrame.setTitle(title);
    }
    //creates a JPanel and sets the color to white
    public JPanel createJPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        return panel;
    }
    //sets up the solutionFrame settings (after the JPanels have been added)
    //and displays it
    public void displayAnswers(int numOfSolutions){
        solutionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        solutionFrame.setBackground(Color.WHITE);
        solutionFrame.setLayout(new GridLayout(numOfSolutions,1));
        solutionFrame.pack();
        solutionFrame.setLocationRelativeTo(null);
        solutionFrame.setVisible(true);
    }
    //sets up the solutionFrame settings (after the JPanels have been added)
    //and displays it not centered
    public void displayAnswers(int numOfSolutions, String xValue){
        solutionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        solutionFrame.setBackground(Color.WHITE);
        solutionFrame.setLayout(new GridLayout(numOfSolutions,1));
        solutionFrame.pack();
        //for height and width
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int width = solutionFrame.getWidth();
        int height = solutionFrame.getHeight();
        if(xValue.equals("top")){
            solutionFrame.setLocation((screenWidth-width)/2,0);
        }
        if(xValue.equals("aboveCenter")){
            solutionFrame.setLocation((screenWidth-width)/2,height);
        }
        solutionFrame.setVisible(true);
    }
    //resets the settings and content of the JFrame to use it
    //for more displays
    public void reset(){
        solutionFrame.removeAll();
        solutionFrame.setVisible(false);
        solutionFrame = new JFrame();
        if(title != null){
            solutionFrame.setTitle(title);
        }
    }
}
