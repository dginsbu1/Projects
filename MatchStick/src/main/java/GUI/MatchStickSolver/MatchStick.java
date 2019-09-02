package GUI.MatchStickSolver;//import javax.xml.soap.SAAJMetaFactory;

import GUI.SolutionDisplay;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

public class MatchStick {
    //paths for creating the NuSMV file and results
    static String localDir = System.getProperty("user.dir");
    static String NuSMVFilePathWrite = localDir+"\\src\\main\\resources\\MatchStick\\NuSMVFileWrite.smv";
    static String NuSMVFilePathRead = localDir+"\\src\\main\\resources\\MatchStick\\NuSMVFileRead.smv";
    static String NuSMVResultsPath = localDir+"\\src\\main\\resources\\MatchStick\\results.txt";
    static String equation;
    //for setting initial variables
    static int a, b,c;
    static char symbol;
    static char sam;//Subtract, Add, Move;
    static int moveNum;
    //for writing NuSMVFile
    static String partOne, partTwo, partThree, partFour;
    //for extracting answers
    static int ansA, ansB, ansC;
    static char ansSym = '*';
    static int varA, varB, varC;
    static char varSym;
    //for multiple solutions
    static boolean moreSolutions = true;
    static int solutionsFound = 0;
    static boolean foundSolution = false;
    static String LTLSpec = "d.remaining = 0 & ((a.output - b.output = c.output & !d.plus) | (a.output + b.output = c.output & d.plus))";
    //for displaying solutions
    static SolutionDisplay displayer;

    //creates a matchstick object and solves the problem from user input
    public static void main(String[] args) throws IOException, InterruptedException {
        MatchStick matchStick = new MatchStick();
        matchStick.solveProblem();
    }
    //Calls each individual method to solve the problem
    //and present the solution
    public void solveProblem() throws IOException, InterruptedException {
        setUpGraphics();
        setEquation();
        setMoves();//add/sub/move and the number
        setNuSMVSections();
        while(moreSolutions) {
            writeNuSMV();
            runNuSMV();
            sleep(250);//gives time to load file
            scanFile();
            presentSolution();
            resetAns();
        }
    }

    //sets up the SolutionDisplay and the Window
    private void setUpGraphics() {
        displayer = new SolutionDisplay();
        setUpWindow();
    }

    //sets the size settings for the GUI display
    private void setUpWindow() {
        //sets up the text OptionPaneSettings
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(
                "Arial", Font.BOLD, 35)));//button
        UIManager.put("TextField.font", new FontUIResource(new Font(
                "Arial", Font.BOLD, 50)));//textBox
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
                "Arial", Font.BOLD, 50)));//actual message
        UIManager.put("OptionPane.minimumSize",new Dimension(800,600));//size of box
    }

    //gets user input to set the equation
    public void setEquation(){
        equation = JOptionPane.showInputDialog(null, "Enter Equation:","Matchstick Solver", JOptionPane.PLAIN_MESSAGE);//BOX
        equation = equation.replace(" ","");//remove spaces
        a = Character.getNumericValue(equation.charAt(0));
        symbol = equation.charAt(1);
        b =      Character.getNumericValue(equation.charAt(2));
        c =      Character.getNumericValue(equation.charAt(4));
        String message = "a= "+a+" b= "+b+" c= "+c+" symbol = "+symbol+"\n";
        JOptionPane.showMessageDialog(null, message, "Matchstick Solver", JOptionPane.PLAIN_MESSAGE);//BOX
    }

    //gets user input for the type of problem (add, subtract, move),
    //and the number required
    public void setMoves() {
        equation = JOptionPane.showInputDialog("Please type in the type of moves: s (subtraction) "+
                "a (addition), or m (move) followed by the number allowed (ex. a3 = add three)").replace(" ", "");
        sam = equation.charAt(0);
        moveNum = Integer.valueOf(equation.substring(1));
        String movesM = "You chose letter "+sam+" with number "+moveNum+"\n";//BOX
        JOptionPane.showMessageDialog(null, movesM, "Matchstick Solver", JOptionPane.PLAIN_MESSAGE);//BOX
        //The NuSMV treats subtraction differently, so we have to multiply by 20 or 21 for "m"
        //relevant for NuSMV file
        if(sam == 'm') moveNum = 21*moveNum;
        if(sam == 's') moveNum = 20*moveNum;
    }

    //sets up the NuSMV file sections that remain static
    private void setNuSMVSections() throws IOException {
        setPartOne();
        setPartFour();
    }
    //sets the part of the NuSMV file that comes before the user input
    private void setPartOne() {
        partOne = "MODULE main\n" +
                "VAR\n";
    }
    //sets the part of the NuSMV file that comes after the user input
    //reads it from a file
    private void setPartFour() throws IOException {
        Path path = Paths.get(NuSMVFilePathRead);
        partFour = new String(Files.readAllBytes(Paths.get(NuSMVFilePathRead)));
    }

    //writes a NuSMV file based on the user input
    public void writeNuSMV() throws IOException, InterruptedException {
        //partOne and partFour never change
        setPartTwo();
        setPartThree();
        FileWriter fw=new FileWriter(NuSMVFilePathWrite);
        fw.write(partOne);
        fw.write(partTwo);
        fw.write(partThree);
        fw.write(partFour);
        fw.close();
    }
    //sets the part of the NuSMV file containing user input
    private void setPartTwo() {
        partTwo = "a : newNumMovMatch("+a+","+moveNum+"); --mov*21\n" +
                "\tb : newNumMovMatch("+b+",a.remaining);\n" +
                "\tc : newNumMovMatch("+c+", b.remaining);\n" +
                "\td : newSymbol(\""+symbol+"\", c.remaining);";
    }
    //sets the part of the NuSMV file with the LTLSPEC statement
    //it changes every time a solution is found
    public void setPartThree() {
        if(solutionsFound > 0 ){
            augmentLTLSPec();
        }
        partThree = "\nLTLSPEC G !("+LTLSpec+");\n";
    }
    //changes the LTLSPEC to get more answers if they exist
    private static void augmentLTLSPec() {
        LTLSpec = LTLSpec+ "& !(a.output = "+varA+" & b.output = "+varB+" & c.output = "+varC+" & d.output = \""+varSym+"\")";
    }

    //runs the NuSMV program and writes the result to a file results.txt
    public void runNuSMV() throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String command = "cmd.exe /c"+"NuSMV -bmc "+NuSMVFilePathWrite+" > "+NuSMVResultsPath;
        Process child = Runtime.getRuntime().exec(command);

    }
    //scans the results.txt file for a potential solution
    public void scanFile() throws IOException {
        InputStream is = new FileInputStream(NuSMVResultsPath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        while (line != null) {
            int lineSize = line.length();
            if (line.contains(".output") && !line.contains("specification")) {
                //the answers are at the end of each line
                if (line.contains("a")) {
                    ansA = Character.getNumericValue(line.charAt(lineSize - 1));
                    foundSolution = true;//could have put this in any line
                }
                if (line.contains("b"))
                    ansB = Character.getNumericValue(line.charAt(lineSize - 1));
                if (line.contains("c"))
                    ansC = Character.getNumericValue(line.charAt(lineSize - 1));
                if (line.contains("d"))
                    ansSym = line.charAt(lineSize - 2);
            }
            line = buf.readLine();
        }
    }
    //prints out the solution (if one exists) or "No more solutions"
    //if none exist
    public void presentSolution() throws InterruptedException {
        //if no more solutions
        if(foundSolution == false){
            if(solutionsFound == 0){//no solutions
                String message = "There are no solutions to the problem.\n"+
                        "even King Solomon would be scratching his head.";
                JOptionPane.showMessageDialog(null, message, "Solution(s)", JOptionPane.PLAIN_MESSAGE);//BOX
                exit(0);
            }
            else {
                displayer.displayAnswers(solutionsFound);
                moreSolutions = false;
            }
        }
        else{
            String[] solution = {ansA+"",ansSym+"",ansB+"","=", ansC+""};
            displayer.convertToImage(solution);
            moreSolutions = true;
            solutionsFound++;
        }
    }
    //resets all variables after each iteration
    //sets them to negative values so errors are apparent.
    public void resetAns() {
        varA = ansA;
        varB = ansB;
        varC = ansC;
        varSym = ansSym;
        ansA = -1;
        ansB = -1;
        ansC = -1;
        ansSym = '*';
        foundSolution = false;
    }
}
