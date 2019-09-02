package commandLine.MatchStickSolver;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.Thread.sleep;

public class MatchStick {
    //paths for creating the NuSMV file and results
    private static String localDir = System.getProperty("user.dir");
    private static String NuSMVFilePathWrite = localDir+"\\src\\main\\resources\\MatchStick\\NuSMVFileWrite.smv";
    private static String NuSMVFilePathRead = localDir+"\\src\\main\\resources\\MatchStick\\NuSMVFileRead.smv";
    private static String NuSMVResultsPath = localDir+"\\src\\main\\resources\\MatchStick\\results.txt";
    //paths for  the NuSMV file and results for getAllAnswers
    //used only for getAllSolutions() method
    private static String NuSMVFilePathWriteAuto = localDir+"\\src\\main\\resources\\MatchStick\\NuSMVFileWriteAuto.smv";
    private static String NuSMVFilePathReadAuto = localDir+"\\src\\main\\resources\\MatchStick\\NuSMVFileReadAuto.smv";
    private static String NuSMVResultsPathAuto = localDir+"\\src\\main\\resources\\MatchStick\\resultsAuto.txt";

    private Scanner in = new Scanner(System.in);
    private String equation;
    //for setting initial variables
    private int a, b,c;
    private char symbol;
    private char sam;//Subtract, Add, Move;
    private int moveNum;
    //for writing NuSMVFile
    private String partOne, partTwo, partThree, partFour;
    //for extracting answers
    private int ansA, ansB, ansC;
    private char ansSym = '*';
    private int varA, varB, varC;
    private char varSym;
    //for multiple solutions
    private boolean moreSolutions = true;
    private int solutionsFound = 0;
    private boolean foundSolution = false;
    private String LTLSpec = "d.remaining = 0 & ((a.output - b.output = c.output & !d.plus) | (a.output + b.output = c.output & d.plus))";

    //creates a matchstick object and solves the problem from either input or file
    public static void main(String[] args) throws IOException, InterruptedException {
        while(true) {
            MatchStick matchStick = new MatchStick();
            matchStick.solveProblem();
        }
    }
    //Calls each individual method to solve the problem
    //and present the solution
    public void solveProblem() throws IOException, InterruptedException {
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
    //solves riddles from a file instead of user input
    public void solveProblem(String filePath) throws IOException, InterruptedException {
        setNuSMVSections();
        InputStream in = new FileInputStream(filePath);
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        String line = buf.readLine();
        while (line != null) {
            moreSolutions = true;
            equation  = line.replace(" ", "");//remove spaces;
            a = Character.getNumericValue(equation.charAt(0));
            symbol = equation.charAt(1);
            b = Character.getNumericValue(equation.charAt(2));
            //3 is the "=" sign
            c = Character.getNumericValue(equation.charAt(4));
            System.out.println("a= " + a + " b= " + b + " c= " + c + " symbol = " + symbol);
            //line two
            equation = buf.readLine().replace(" ","");//remove spaces;//this is the moves
            sam = equation.charAt(0);
            moveNum = Integer.valueOf(equation.substring(1));
            //if the sam is m, then we hav to multiply by 21.
            System.out.println("You chose letter "+sam+" with number "+moveNum);
            if(sam == 'm') moveNum = 21*moveNum;
            if(sam == 's') moveNum = 20*moveNum;
            while(moreSolutions) {
                writeNuSMV();
                runNuSMV();
                //allows for the program to finish writing the file before reading
                sleep(250);
                scanFile();
                presentSolution();
                resetAns();
            }
            LTLSpec = "d.remaining = 0 & ((a.output - b.output = c.output & !d.plus) | (a.output + b.output = c.output & d.plus))";
            solutionsFound = 0;
            line = buf.readLine();
        }
    }
    //gets user input to set the equation
    public void setEquation(){
        //get input
        System.out.println("please type in your equation (ex. 2 + 2 = 5)");
        equation = in.nextLine().replace(" ","");//remove spaces
        //set the variables
        a        = Character.getNumericValue(equation.charAt(0));
        symbol   = equation.charAt(1);
        b        = Character.getNumericValue(equation.charAt(2));
        c        = Character.getNumericValue(equation.charAt(4));
        System.out.println("a= "+a+" b= "+b+" c= "+c+" symbol = "+symbol+"\n");
    }
    //gets user input for the type of problem (add, subtract, move),
    //and the number required
    public void setMoves() {
        System.out.println("please type in the type of moves s (subtraction) "+
                "a (addition), or m (move) followed by the number allowed (ex. a3 = add three)");
        equation = in.nextLine().replace(" ","");//remove spaces
        sam = equation.charAt(0);
        moveNum = Integer.valueOf(equation.substring(1));
        //The NuSMV program treats subtraction and moving differently,
        //so we have to multiply by 20 or 21 for "s" or "m"
        String movesM = "You chose letter "+sam+" with number "+moveNum+"\n";
        if(sam == 'm') moveNum = 21*moveNum;
        if(sam == 's') moveNum = 20*moveNum;
    }
    //sets up the unchanging portions NuSMV file sections that remain static
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
        FileWriter fw = new FileWriter(NuSMVFilePathWrite);
        fw.write(partOne);
        fw.write(partTwo);
        fw.write(partThree);
        fw.write(partFour);
        fw.close();
    }
    //used only for getAllSolutions() method
    public void writeNuSMV(String extension) throws IOException, InterruptedException {
        //partOne and partFour never change
        setPartTwo();
        setPartThree();
        FileWriter fw = new FileWriter(NuSMVFilePathWriteAuto);
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
                "\td : newSymbol(\""+symbol+"\", c.remaining);\n";
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
    private void augmentLTLSPec() {
        LTLSpec = LTLSpec+ "& !(a.output = "+varA+" & b.output = "+varB+" & c.output = "+varC+" & d.output = \""+varSym+"\")";
    }
    //runs the NuSMV program and writes the result to a file results.txt
    public void runNuSMV() throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String command = "cmd.exe /c"+"NuSMV -bmc "+NuSMVFilePathWrite+" > "+NuSMVResultsPath;
        Process child = Runtime.getRuntime().exec(command);
    }
    //used only for getAllSolutions() method
    public void runNuSMV(String extension) throws IOException, InterruptedException {
        Runtime rt = Runtime.getRuntime();
        String command = "cmd.exe /c"+"NuSMV -bmc "+NuSMVFilePathWriteAuto+" > "+NuSMVResultsPathAuto;
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
    //used only for getAllSolutions() method
    public void scanFile(String extension) throws IOException {
        InputStream is = new FileInputStream(NuSMVResultsPathAuto);
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
    public void presentSolution(){
        //if no more solutions
        if(foundSolution == false){
            System.out.println("There are: "+solutionsFound+" solution(s) in total\n");
            //System.out.println("No More Solutions\n");
            moreSolutions = false;
        }
        else{
            System.out.printf("The solution is: %d %s %d = %d \n", ansA,ansSym,ansB,ansC);
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
    //returns all possible solutions to the String equation in as an ArrayList
    //mainly used when working with other programs
    public ArrayList<String> getAllAnswers(String equation, String moves) throws IOException, InterruptedException {
        ArrayList<String> allAnswers = new ArrayList<String>();
        equation  = equation.replace(" ", "");//remove spaces;
        moreSolutions = true;
        setNuSMVSections();
        //sets up all the variables
        a = Character.getNumericValue(equation.charAt(0));
        symbol = equation.charAt(1);
        b = Character.getNumericValue(equation.charAt(2));
        c = Character.getNumericValue(equation.charAt(4));
        sam = moves.charAt(0);
        moveNum = Character.getNumericValue(moves.charAt(1));
        //if the sam is m, then we hav to multiply by 21.
        //System.out.println("You chose letter "+sam+" with number "+moveNum);
        if(sam == 'm') moveNum = 21*moveNum;
        if(sam == 's') moveNum = 20*moveNum;
        while(moreSolutions) {
            writeNuSMV("Auto");
            runNuSMV("Auto");
            sleep(250);//allows for the program to finish writing the file before reading
            scanFile("Auto");
            if(foundSolution == false){
                moreSolutions = false;
            }
            else{
                allAnswers.add(""+ansA+""+ansSym+""+ansB+"="+ansC);
                moreSolutions = true;
                solutionsFound++;
            }
            resetAns();
        }
        return allAnswers;
    }
}
