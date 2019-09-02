package GUI.creator;

import GUI.SolutionDisplay;
import commandLine.MatchStickSolver.MatchStick;
import commandLine.creator.EquationCreator;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.plaf.FontUIResource;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.Thread.sleep;

public class MatchStickCreator {
    //paths for creating the NuSMV file and results
    static String localDir = System.getProperty("user.dir");
    static String NuSMVFilePath = localDir+"\\src\\main\\resources\\creator\\NuSMVCreate.smv";
    static String NuSMVResultsPath = localDir+"\\src\\main\\resources\\creator\\CreateResults.txt";
    static String NuSMVFilePathRead = localDir+"\\src\\main\\resources\\creator\\NuSMVFilePartThree";

    static Scanner input = new Scanner(System.in);
    static private String questionType;
    static private int used;
    static private int hintsGiven = 0;//Used for hints
    static private int ADDSIZE = 20;//Used for how many adds and subs there are
    static private String partThree;
    static private String suggestion; //Used to check answers
    static private String allHints = ""; //Used to combine hints
    static private String problemType = "";

    static SolutionDisplay solutionDisplayer = new SolutionDisplay();
    static SolutionDisplay questionDisplayer = new SolutionDisplay();
    static boolean moreSolutions = true;
    static int solutionsFound = 0;
    static boolean foundSolution = false;
    private static ArrayList<String> correctAnswers;

    //Runs a program that allows the user to solve various matchstick problems
    public static void main(String args[]) throws IOException, InterruptedException {
        generalSetUp();
        while(true) {
            String questionType = getQuestionType();
            String qtype = "The question type is: " + questionType;
            JOptionPane.showMessageDialog(null, qtype, "Matchstick ", JOptionPane.PLAIN_MESSAGE);
            String[] equation = EquationCreator.createEquation();
            writeNuSMV(equation, questionType);
            runNuSMV();
            sleep(250);
            String[] result = scanNuSMV();
            int numMoves = setUpVariables(result);
            displayResults(result, numMoves);
            boolean correct = false;
            String help = "If you need help you can type \"hint\"" + " for a hint or \"help\" for the answer";
            while(!correct){
                correct = checkAnswer(equation, result, numMoves);
            }
            reset();
        }
    }


    //Sets up the static portion of the NuSMV file
    private static void generalSetUp() throws IOException {
        setPartThree();
        setUpWindow();
    }
    //Sets the second half the equation (which never changes)
    private static String setPartThree() throws IOException {
        Path path = Paths.get(NuSMVFilePathRead);
        partThree = new String(Files.readAllBytes(Paths.get(NuSMVFilePathRead)));
        return partThree;
    }
    //sets the size settings for the GUI display
    private static void setUpWindow() {
        //sets up the text OptionPaneSettings
        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(
                "Arial", Font.BOLD, 35)));//button
        UIManager.put("TextField.font", new FontUIResource(new Font(
                "Arial", Font.BOLD, 50)));//textBox
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
                "Arial", Font.BOLD, 50)));//actual message
        UIManager.put("OptionPane.minimumSize",new Dimension(800,600));//size of box
    }

    //Determines the type of question to create (addition, subtraction, move) based on user input
    private static String getQuestionType() {
        String[] options = {"Addition", "Subtraction", "Move"};
        int x = JOptionPane.showOptionDialog(null, "What type of problem do you want?",
                "Click a button",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
        //String problemType = JOptionPane.showInputDialog("what type of problem do you want? s (subtraction) a (addition), "+
        //      "or m (move)?");
        if (x == 0) {
            problemType = "a";
        }
        else if (x == 1) {
            problemType = "s";
        }
        else if (x == 2) {
            problemType = "m";
        }
        else exit(0);
        questionType = problemType;
        return problemType;
    }
    //Writes a NuSMV that creates a matchstick problem based on the input
    private static void writeNuSMV(String[] equation, String questionType) throws IOException, InterruptedException {
        String partOne = setPartOne(equation);
        String LTLSPEC = setPartTwo(questionType);
        //partThree already set.
        FileWriter fw = new FileWriter(NuSMVFilePath);
        fw.write(partOne);
        fw.write(LTLSPEC);
        fw.write(partThree);
        fw.close();
    }
    //Sets the first part of the NuSMV file based on the equation given
    private static String setPartOne(String[] equ) {
        return "MODULE main\n" +
                "VAR\n" +
                "\ta : changeNum("+equ[0]+", 0); --mov*21\n" +
                "\tb : changeNum("+equ[2]+", a.used);\n" +
                "\tc : changeNum("+equ[4]+", b.used);\n" +
                "\td : changeSymbol(\""+equ[1]+"\", c.used);\n" +
                "DEFINE\n" +
                " mini := 20;\n" +
                "  pls := 1;\n" +
                " used := -(d.used);\n" +
                "  add := (used / mini);\n" +
                "  sub := (used mod mini) / pls;";
    }
    //Sets the middle part of the NuSMV file based on the type
    private static String setPartTwo(String questionType) {
        String LTLSPEC = "";
        if(questionType.equals("a")) LTLSPEC = "\nLTLSPEC G !(add != 0 & sub = 0";
        if(questionType.equals("s")) LTLSPEC = "\nLTLSPEC G !(sub != 0 & add = 0";
        if(questionType.equals("m")) LTLSPEC = "\nLTLSPEC G !(sub != 0 & add = sub";
        //added so no "correct equations"
        LTLSPEC+= "& !((a.output - b.output = c.output & d.output = \"-\") | (a.output + b.output = c.output & d.output = \"+\")));\n";
        return LTLSPEC;
    }
    //Runs the NuSMV file and save the results to CreateResults.txt
    private static void runNuSMV() throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String command = "cmd.exe /c"+"NuSMV -bmc "+NuSMVFilePath+" > "+NuSMVResultsPath;
        Process child = Runtime.getRuntime().exec(command);
    }
    //scans the CreateResults.txt and extracts the elements of the equation
    //as well as the number of moves
    private static String[] scanNuSMV() throws IOException {
        String[] result = new String[5];
        InputStream is = new FileInputStream(NuSMVResultsPath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        while (line != null) {
            int lineSize = line.length();
            if (line.contains(".output") && !line.contains("specification")) {
                if (line.contains("a"))//first number
                    result[0] = line.charAt(lineSize - 1)+"";
                if (line.contains("d"))//arithmetic symbol i.e. +/-
                    result[1] = line.charAt(lineSize - 2)+"";
                if (line.contains("b"))//second number
                    result[2] = line.charAt(lineSize - 1)+"";
                if (line.contains("c"))//last number
                    result[4] = line.charAt(lineSize - 1)+"";
            }
            if(line.contains("used = ") && !line.contains(".")) {//for number used
                used = Integer.valueOf(line.substring(line.indexOf("= ")+2));
            }
            line = buf.readLine();
        }
        result[3] = "=";//equal sign
        return result;
    }
    //sets the number of available moves
    //The NuSMV program treats each operation differently
    //so we have to divide or mod to get the correct value of each
    private static int setUpVariables(String[] result) throws IOException, InterruptedException {
        //sets up answer array
        int numMoves = 0;
        if(questionType.equals("a")) numMoves = used / 20;
        if(questionType.equals("s")) numMoves = used % 20;
        if(questionType.equals("m")) numMoves = used % 20;
        MatchStick answerGetter = new MatchStick();
        correctAnswers = answerGetter.getAllAnswers(EquationCreator.equationToString(result), questionType+numMoves);
        return numMoves;
    }
    //Displays the equation to the command line
    private static String displayResults(String[] result, int numMoves) throws InterruptedException {
        String resultString = equationToString(result);
        String[] resStri = resultString.split("");
        presentEquation(resStri);
        String probAsFol = "Your problem is as follows:\n"+ resultString+"\nYou have "+numMoves+" "+getType()+"(s)" +
                "\nIf you need help you can type \"hint\" for a hint or \"help\" for the answer";
        suggestion = JOptionPane.showInputDialog(probAsFol);
        return suggestion;
    }
    //Turns a String array into a regular String
    private static String equationToString(String[] result) {
        StringBuilder equationString = new StringBuilder();
        for(String element: result)
            equationString.append(element);
        String equation = equationString.toString();
        return equation;
    }
    public static void presentEquation(String[]resStri) throws InterruptedException {
        questionDisplayer.convertToImage(resStri);
        questionDisplayer.displayAnswers(1,"top");
    }
    //Returns the type of problem being created (addition, subtraction, move)
    public static String getType(){
        if(questionType.equals("a")) return "addition";
        if(questionType.equals("s")) return "subtraction";
        if(questionType.equals("m")) return "move";
        return null;
    }
    //Checks the user's answer against the solution
    private static boolean checkAnswer(String[] equation, String[] result, int numMoves) throws InterruptedException {
        String answer = equationToString(equation);
        suggestion = suggestion.replace(" ","");//Removes spaces
        String resultString = equationToString(result);
        String probAsFol = "Your problem is as follows:\n"+
                resultString+"\nYou have "+numMoves+" "+getType()+"(s)\nIf you need help you can type \"hint\" for a hint or \"help\" for the answer";
        if(isValidSolution(suggestion)){//correct answer
            presentSolution();
            sleep(500);
            String congrats = "Congratulations, you got it!!!.\n";
            JOptionPane.showMessageDialog(null,congrats, "Matchstick Solver", JOptionPane.PLAIN_MESSAGE);
            hintsGiven = 0;//Resets the hint count
            return true;
        } else if(suggestion.equals("help")) {
            String here = "Here's the solution\n"+answer;
            JOptionPane.showMessageDialog(null, here, "Matchstick Solver", JOptionPane.PLAIN_MESSAGE);
            suggestion = JOptionPane.showInputDialog(probAsFol+ "\n" + answer);
        } else if(suggestion.equals("hint")) {
            allHints += giveHint(equation);
            suggestion = JOptionPane.showInputDialog(probAsFol+ "\n" + allHints);
        } else {
            String nope = "Nope, but good try.";
            JOptionPane.showMessageDialog(null, nope, "Matchstick Solver", JOptionPane.PLAIN_MESSAGE);
            suggestion = JOptionPane.showInputDialog(probAsFol);
            return false;
        }
        return false;
    }
    //returns true if suggested answer matches any of the correct answers
    private static boolean isValidSolution(String suggestion) {
        for(String validAnswer : correctAnswers)
            if(suggestion.equals(validAnswer))
                return true;
        return false;
    }
    public static void presentSolution() throws InterruptedException {
        String[] solStri = suggestion.split("");
        solutionDisplayer.setTitle("CONGRATS!!! YOU GOT IT");
        solutionDisplayer.convertToImage(solStri);
        solutionDisplayer.displayAnswers(1, "aboveCenter");
        sleep(5000);
        questionDisplayer.reset();
        solutionDisplayer.reset();
    }
    //gives a hint to the user as to the solution
    private static String giveHint(String[] equation) {
        if(hintsGiven > 4){
            String cmon = "I literally gave you the whole equation...\n" + "maybe you should stick with basic addition problems";
            JOptionPane.showMessageDialog(null, cmon, "Matchstick Solver", JOptionPane.PLAIN_MESSAGE);
            return "";
        }
        int currentHint = hintsGiven+1;
        String hint = "Element in position " + currentHint + ": " + equation[hintsGiven] + "\n";
        hintsGiven++;
        return hint;
    }
    private static void reset() {
        hintsGiven = 0;
        allHints = "";
    }
}
