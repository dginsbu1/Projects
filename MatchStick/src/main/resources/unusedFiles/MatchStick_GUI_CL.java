//package com.commandLine;//import javax.xml.soap.SAAJMetaFactory;
/*package unusedFiles;
import commandLine.MatchStickSolver.MatchStick;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import static java.lang.Thread.sleep;

public class MatchStickGC {
    //paths for creating the NuSMV file and results
    static String localDir = System.getProperty("user.dir");
    static String NuSMVFilePathWrite = localDir+"\\src\\main\\resources\\NuSMVFileWrite.smv";
    static String NuSMVFilePathRead = localDir+"\\src\\main\\resources\\NuSMVFileRead.smv";
    static String NuSMVResultsPath = localDir+"\\src\\main\\resources\\results.txt";
    static Scanner in = new Scanner(System.in);
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
    /*GUI
    //for displaying solutions
    static SolutionDisplay displayer;
    */
/*
    //creates a matchstick object and solves the problem from either input or file
    public static void main(String[] args) throws IOException, InterruptedException {
        MatchStick matchStick = new MatchStick();
        //reading from a file
        //matchStick.solveFromFile(args[0]);

        //using user input
        matchStick.solveProblem();
    }

    //solves riddles from a file instead of user input
    //"C:\\Users\\dgmon\\GIT\\MatchStick\\src\\main\\resources\\input.txt";
    public void solveFromFile(String filePath) throws IOException, InterruptedException {
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
            moveNum = Character.getNumericValue(equation.charAt(1));
            //if the sam is m, then we hav to multiply by 21.
            System.out.println("You chose letter "+sam+" with number "+moveNum);
            if(sam == 'm') moveNum = 21*moveNum;
            if(sam == 's') moveNum = 20*moveNum;
            while(moreSolutions) {
                writeNuSMV();
                runNuSMV();
                //allows for the program to finish writing the file before reading
                sleep(500);
                scanFile();
                presentSolution();
                resetAns();
            }
            LTLSpec = "d.remaining = 0 & ((a.output - b.output = c.output & !d.plus) | (a.output + b.output = c.output & d.plus))";
            solutionsFound = 0;
            line = buf.readLine();
        }
    }

    //Calls each individual method to solve the problem
    // and present the solution
    public void solveProblem() throws IOException, InterruptedException {
        //GUI
        //setUpGraphics();
        setEquation();
        setMoves();//add/sub/move and the number
        setNuSMVSections();
        while(moreSolutions) {
            writeNuSMV();
            runNuSMV();
            sleep(200);//gives time to load file
            scanFile();
            presentSolution();
            resetAns();
        }
    }

    /* GUI
    sets up the SolutionDisplay and the Window
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

    */
/*
    //reads a file to get the last section of the NuSMV file that will be written
    private void setNuSMVSections() throws IOException {
        Path path = Paths.get(NuSMVFilePathRead);
        partThree = new String(Files.readAllBytes(Paths.get(NuSMVFilePathRead)));
    }
    //sets the size settings for the GUI display
//    private void setUpWindow() {
//        //sets up the text OptionPaneSettings
//        UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font(
//                "Arial", Font.BOLD, 35)));//button
//        UIManager.put("TextField.font", new FontUIResource(new Font(
//                "Arial", Font.BOLD, 50)));//textBox
//        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
//                "Arial", Font.BOLD, 50)));//actual message
//        UIManager.put("OptionPane.minimumSize",new Dimension(800,600));//size of box
//    }

    //gets user input to set the equation
    public void setEquation(){
        // GUI equation = JOptionPane.showInputDialog("Enter Equation:");
        // CL System.out.println("please type in your equation (ex. 2 + 2 = 5)");
        equation = in.nextLine().trim();
        equation = equation.replace(" ","");//remove spaces
        a = Character.getNumericValue(equation.charAt(0));
        symbol = equation.charAt(1);
        b =      Character.getNumericValue(equation.charAt(2));
        c =      Character.getNumericValue(equation.charAt(4));
        //GUI
        //String message = "a= "+a+" b= "+b+" c= "+c+" symbol = "+symbol+"\n";
        //JOptionPane.showMessageDialog(null, message, "Matchstick Solver", JOptionPane.PLAIN_MESSAGE);//BOX
        System.out.println("a= "+a+" b= "+b+" c= "+c+" symbol = "+symbol+"\n");
    }
    //gets user input for the type of problem (add, subract, move),
    //and the number required
    public void setMoves() {
        /*GUI
        equation = JOptionPane.showInputDialog("Please type in the type of moves: s (subtraction) "+
                "a (addition), or m (move) followed by the number allowed (ex. a3 = add three)").replace(" ", "");
        */
        /* CL
        System.out.println("please type in the type of moves s (subtraction) "+
                "a (addition), or m (move) followed by the number allowed (ex. a3 = add three)");
        equation = in.nextLine().trim();

        equation = equation.replace(" ","");//remove spaces
        sam = equation.charAt(0);
        moveNum = Character.getNumericValue(equation.charAt(1));
        //The NuSMV treats subtraction differently, so we have to multiply by 20 or 21 for "m"
        //relevant for NuSMV file
        String movesM = "You chose letter "+sam+" with number "+moveNum+"\n";
        // CL System.out.println(movesM);
        // GUI JOptionPane.showMessageDialog(null, movesM, "Matchstick Solver", JOptionPane.PLAIN_MESSAGE);//BOX
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
/*
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
    public void presentSolution(){
        //if no more solutions
        if(foundSolution == false){
            // CL System.out.println("There are: "+solutionsFound+" in total");
            // CL System.out.println("No More Solutions\n");
            // GUI displayer.displayAnswers(solutionsFound);
            moreSolutions = false;
        }
        else{
            // CL System.out.printf("The solution is: %d %s %d = %d \n", ansA,ansSym,ansB,ansC);
            // GUI String[] solution = {ansA+"",ansSym+"",ansB+"","=", ansC+""};
            // GUI displayer.convertToImage(solution);
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

        /*
        secondHalf = "\nLTLSPEC G !("+LTLSpec+");\n" +
                "MODULE newSymbol(symbol, input)\n" +
                "VAR\n" +
                "\toutput: {\"-\", \"+\"};\n" +
                "\tmini : 20..25;\n" +
                "\tpls : 1..3;\n" +
                "ASSIGN \n" +
                "\tmini := 20; -- value of each sub\n" +
                "\tpls := 1;  -- value of each add \n" +
                "\toutput := case\n" +
                "\t\t\tsymbol = \"+\" & input >= mini : {\"+\",\"-\"};\n" +
                "\t\t\tsymbol = \"-\" & input > 0 & input < mini : {\"+\",\"-\"}; \n" +
                "\t\t\tTRUE : symbol; \n" +
                "\t\t\tesac;\n" +
                "DEFINE \n" +
                "\tremaining := case\n" +
                "\t\tsymbol = \"+\" & output = \"-\" : input - mini;\n" +
                "\t\tsymbol = \"-\" & output = \"+\" : input - pls; \n" +
                "\t\tTRUE \t\t\t\t\t\t: input;\n" +
                "\t\tesac;\n" +
                "\tplus := (output = \"+\");\n" +
                "-------------------------------------------------------\n" +
                "MODULE newNumMovMatch(number, input) --input is 20*sub + 1*add\n" +
                "VAR\n" +
                "\toutput: {0,1,2,3,4,5,6,7,8,9};\n" +
                "\n" +
                "DEFINE \n" +
                "\tmini:= 20; -- value of each sub\n" +
                "\tpls := 1;  -- value of each add \n" +
                "\tadd := (input mod mini) / pls;\n" +
                "\tsub := (input / mini);\n" +
                "ASSIGN \n" +
                "\toutput := case\n" +
                "\t\tnumber = 0 & sub >= 1 & add >= 1 : {0,6,9};\t\t   \tTRUE : number; esac union case\n" +
                "\t\tnumber = 0 & sub >= 2 & add >= 1 : {0,6,9,2,3,5};  \tTRUE : number; esac union case\n" +
                "        number = 0 & sub >= 3 & add >= 1 : {0,6,9,2,3,5,4};\tTRUE : number; esac union case\t\n" +
                "\n" +
                "\t\tnumber = 1 & sub >= 1 & add >= 4 : {1,2,5};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 1 & sub >=1 & add >= 5\t : {1,2,5,6};\t\tTRUE : number; esac union case\n" +
                "\n" +
                "\t\tnumber = 2 & sub >= 1 & add >= 1 : {2,3};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub >= 1 & add >= 2 : {2,3,0,6,9}; \tTRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub >= 2 & add >= 2 : {2,3,0,6,9,5};\tTRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub >= 3 & add >= 1 : {7};\t\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub >= 3 & add >= 2 : {7,4};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub >= 4 & add >= 1 : {1};\t\t\t\tTRUE : number; esac union case\n" +
                "\n" +
                "\t\tnumber = 3 & sub >= 0 & add >= 1 : {3};\t\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 3 & sub >= 1 & add >= 1 : {3,2,5};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 3 & sub >= 2 & add >= 1 : {4};\t\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 3 & sub >= 1 & add >= 2 : {3,2,5,0,6};\t\tTRUE : number; esac union case\n" +
                "\n" +
                "\t\tnumber = 4 & sub >= 1 & add >= 2 : {4,3,5};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 4 & sub >= 1 & add >= 3 : {4,3,5,0,6};\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 4 & sub >= 2 & add >= 1 : {7};\t\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 4 & sub >= 2 & add >= 3 : {2};\t\t\t\tTRUE : number; esac union case\n" +
                "\n" +
                "\t\tnumber = 5 & sub >= 1 & add >= 1 : {3};\t\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub >= 1 & add >= 2 : {3,0};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub >= 2 & add >= 1 : {4};\t\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub >= 2 & add >= 2 : {4,2};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub >= 3 & add >= 1 : {7};\t\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub >= 4 & add >= 1 : {1};\t\t\t\tTRUE : number; esac union case\n" +
                "\n" +
                "\t\tnumber = 6 & sub >= 1 & add >= 1 : {0,9};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 6 & sub >= 2 & add >= 1 : {0,9,2,3};\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 6 & sub >= 3 & add >= 1 : {0,9,2,3,4};\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 6 & sub >= 4 & add >= 1 : {0,9,2,3,4,7};\tTRUE : number; esac union case\n" +
                "\t\tnumber = 6 & sub >= 5 & add >= 1 : {0,9,2,3,4,7,1};\tTRUE : number; esac union case\n" +
                "\n" +
                "\t\tnumber = 7 & sub >= 1 & add >= 2 : {4};\t\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 7 & sub >= 1 & add >= 3 : {4,2,5};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 7 & sub >= 1 & add >= 4 : {4,2,5,6};\t\tTRUE : number; esac union case\n" +
                "\t\t--NOTHING FOR 8\n" +
                "\t\tnumber = 9 & sub >= 1 & add >= 1 : {0,6};\t\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 9 & sub >= 2 & add >= 1 : {0,6,2};\t\t\tTRUE : number; esac union case\n" +
                "\t\t--just adding--------------\n" +
                "\t\tnumber = 0 & add = 0  : {0};\n" +
                "\t\tnumber = 0 & add >= 1 : {0,8};\n" +
                "\t\tnumber = 1 & add = 0  : {1};\n" +
                "\t\tnumber = 1 & add = 1  : {1,7};\n" +
                "\t\tnumber = 1 & add = 2  : {1,7,4};\n" +
                "\t\tnumber = 1 & add = 3  : {1,7,4,3,9};\n" +
                "\t\tnumber = 1 & add = 4  : {1,7,4,3,9,0};\n" +
                "\t\tnumber = 1 & add >= 5 : {1,7,4,3,9,0,8};\n" +
                "\t\tnumber = 2 & add = 0  : {2};\n" +
                "\t\tnumber = 2 & add = 1  : {2};\n" +
                "\t\tnumber = 2 & add >= 2 : {2,8};\n" +
                "\t\tnumber = 3 & add = 0  : {3};\n" +
                "\t\tnumber = 3 & add = 1  : {3,9};\n" +
                "\t\tnumber = 3 & add >= 2 : {3,9,8};\n" +
                "\t\tnumber = 4 & add = 0  : {4};\n" +
                "\t\tnumber = 4 & add = 1  : {4};\n" +
                "\t\tnumber = 4 & add = 2  : {4,9};\n" +
                "\t\tnumber = 4 & add >= 3 : {4,9,8};\n" +
                "\t\tnumber = 5 & add = 0  : {5};\n" +
                "\t\tnumber = 5 & add = 1  : {5,6,9};\n" +
                "\t\tnumber = 5 & add >= 2 : {5,6,9,8};\n" +
                "\t\tnumber = 6 & add = 0  : {6};\n" +
                "\t\tnumber = 6 & add >= 1 : {6,8};\n" +
                "\t\tnumber = 7 & add = 0  : {7};\n" +
                "\t\tnumber = 7 & add = 1  : {7};\n" +
                "\t\tnumber = 7 & add = 2  : {7,3};\n" +
                "\t\tnumber = 7 & add = 3  : {7,3,0,9};\n" +
                "\t\tnumber = 7 & add >= 4 : {7,3,0,9,8};\n" +
                "\t\tnumber = 8 & add >= 0 : {8};\n" +
                "\t\tnumber = 9 & add = 0  : {9};\n" +
                "\t\tnumber = 9 & add >= 1 : {9,8};\n" +
                "\t\tTRUE: number;\n" +
                "\t\tesac union case\n" +
                "\t\t--just sutraction----------------------------------------------\n" +
                "\t\tnumber = 0 & sub = 0 : {0};\n" +
                "\t\tnumber = 0 & sub = 1 : {0};\n" +
                "\t\tnumber = 0 & sub = 2 : {0};\n" +
                "\t\tnumber = 0 & sub = 3 : {0,7};\n" +
                "\t\tnumber = 0 & sub >= 4: {0,7,1};\n" +
                "\t\tnumber = 1 & sub >= 0: {1};\n" +
                "\t\tnumber = 2 & sub >= 0: {2};\n" +
                "\t\tnumber = 3 & sub = 0 : {3};\n" +
                "\t\tnumber = 3 & sub = 1 : {3};\n" +
                "\t\tnumber = 3 & sub = 2 : {3,7};\n" +
                "\t\tnumber = 3 & sub >= 2: {3,7,1};\n" +
                "\t\tnumber = 4 & sub = 0 : {4};\n" +
                "\t\tnumber = 4 & sub = 1 : {4};\n" +
                "\t\tnumber = 4 & sub > 1 : {4,1};\n" +
                "\t\tnumber = 5 & sub >= 0: {5};\n" +
                "\t\tnumber = 6 & sub = 0 : {6};\n" +
                "\t\tnumber = 6 & sub > 0 : {6,5};\n" +
                "\t\tnumber = 7 & sub = 0 : {7};\n" +
                "\t\tnumber = 7 & sub > 0 : {7,1};\n" +
                "\t\tnumber = 8 & sub = 0 : {8};\n" +
                "\t\tnumber = 8 & sub = 1 : {8,0,6,9};\n" +
                "\t\tnumber = 8 & sub = 2 : {8,0,6,9,2,3,5};\n" +
                "\t\tnumber = 8 & sub = 3 : {8,0,6,9,2,3,5,4};\n" +
                "\t\tnumber = 8 & sub = 4 : {8,0,6,9,2,3,5,4,7};\n" +
                "\t\tnumber = 8 & sub = 5 : {8,0,6,9,2,3,5,4,7,1};\n" +
                "\t\tnumber = 8 & sub > 5 : {8,0,6,9,2,3,5,4,7,1};\n" +
                "\t\tnumber = 9 & sub = 0 : {9};\n" +
                "\t\tnumber = 9 & sub = 1 : {9,3,5};\n" +
                "\t\tnumber = 9 & sub = 2 : {9,3,5,4};\n" +
                "\t\tnumber = 9 & sub = 3 : {9,3,5,4,7};\n" +
                "\t\tnumber = 9 & sub > 3 : {9,3,5,4,7,1};\n" +
                "\t\tTRUE: number;\n" +
                "\t\tesac union case\n" +
                "\t\tTRUE\t\t\t\t : number;\n" +
                "\t\tesac;\n" +
                "\n" +
                "\n" +
                "DEFINE\n" +
                "\tremaining := case \n" +
                "\t\tnumber = 0 & (output = 6) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 0 & (output = 9) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 0 & (output = 2) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 0 & (output = 3) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 0 & (output = 5) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 0 & (output = 4) :  input - (3*mini) - (1*pls);\n" +
                "\n" +
                "\t\tnumber = 1 & (output = 2) :  input - (1*mini) - (4*pls);\n" +
                "\t\tnumber = 1 & (output = 5) :  input - (1*mini) - (4*pls);\n" +
                "\t\tnumber = 1 & (output = 6) :  input - (1*mini) - (5*pls);\n" +
                "\n" +
                "\t\tnumber = 2 & (output = 3) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 2 & (output = 0) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 2 & (output = 6) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 2 & (output = 9) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 2 & (output = 5) :  input - (2*mini) - (2*pls);\n" +
                "\t\tnumber = 2 & (output = 7) :  input - (3*mini) - (1*pls);\n" +
                "\t\tnumber = 2 & (output = 4) :  input - (3*mini) - (2*pls);\n" +
                "\t\tnumber = 2 & (output = 1) :  input - (4*mini) - (1*pls);\n" +
                "\n" +
                "\t\tnumber = 3 & (output = 2) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 3 & (output = 5) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 3 & (output = 0) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 3 & (output = 6) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 3 & (output = 4) :  input - (2*mini) - (1*pls);\n" +
                "\n" +
                "\t\tnumber = 4 & (output = 3) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 4 & (output = 5) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 4 & (output = 0) :  input - (1*mini) - (3*pls);\n" +
                "\t\tnumber = 4 & (output = 6) :  input - (1*mini) - (3*pls);\n" +
                "\t\tnumber = 4 & (output = 7) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 4 & (output = 2) :  input - (2*mini) - (3*pls);\n" +
                "\n" +
                "\t\tnumber = 5 & (output = 3) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 5 & (output = 0) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 5 & (output = 4) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 5 & (output = 2) :  input - (2*mini) - (2*pls);\n" +
                "\t\tnumber = 5 & (output = 7) :  input - (3*mini) - (1*pls);\n" +
                "\t\tnumber = 5 & (output = 1) :  input - (4*mini) - (1*pls);\n" +
                "\n" +
                "\t\tnumber = 6 & (output = 0) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 9) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 2) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 3) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 4) :  input - (3*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 7) :  input - (4*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 1) :  input - (5*mini) - (1*pls);\n" +
                "\n" +
                "\t\tnumber = 7 & (output = 4) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 7 & (output = 2) :  input - (1*mini) - (3*pls);\n" +
                "\t\tnumber = 7 & (output = 5) :  input - (1*mini) - (3*pls);\n" +
                "\t\tnumber = 7 & (output = 6) :  input - (1*mini) - (4*pls);\n" +
                "\n" +
                "\t\tnumber = 9 & (output = 0) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 9 & (output = 6) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 9 & (output = 2) :  input - (2*mini) - (1*pls);\n" +
                "\t\t----------just adding------------\n" +
                "\t\tnumber = 0 & output= 8 : input - 1*pls;\n" +
                "\t\tnumber = 1 & output= 7 : input - 1*pls;\n" +
                "\t\tnumber = 1 & output= 4 : input - 2*pls;\n" +
                "\t\tnumber = 1 & output= 3 : input - 3*pls;\n" +
                "\t\tnumber = 1 & output= 9 : input - 4*pls;\n" +
                "\t\tnumber = 1 & output= 0 : input - 4*pls;\n" +
                "\t\tnumber = 1 & output= 8 : input - 5*pls;\n" +
                "\t\tnumber = 2 & output= 8 : input - 2*pls;\n" +
                "\t\tnumber = 3 & output= 9 : input - 1*pls;\n" +
                "\t\tnumber = 3 & output= 8 : input - 2*pls;\n" +
                "\t\tnumber = 4 & output= 9 : input - 2*pls;\n" +
                "\t\tnumber = 4 & output= 8 : input - 3*pls;\n" +
                "\t\tnumber = 5 & output= 6 : input - 1*pls;\n" +
                "\t\tnumber = 5 & output= 9 : input - 1*pls;\n" +
                "\t\tnumber = 5 & output= 8 : input - 2*pls;\n" +
                "\t\tnumber = 6 & output= 8 : input - 1*pls;\n" +
                "\t\tnumber = 7 & output= 3 : input - 2*pls;\n" +
                "\t\tnumber = 7 & output= 0 : input - 3*pls;\n" +
                "\t\tnumber = 7 & output= 9 : input - 3*pls;\n" +
                "\t\tnumber = 7 & output= 8 : input - 4*pls;\n" +
                "\t\tnumber = 9 & output= 8 : input - 1*pls;\n" +
                "\t\t--just subtracting-----\n" +
                "\t\tnumber = 0 & output= 7 : input - 3*mini;\n" +
                "\t\tnumber = 0 & output= 1 : input - 4*mini;\n" +
                "\t\tnumber = 3 & output= 7 : input - 2*mini;\n" +
                "\t\tnumber = 3 & output= 1 : input - 3*mini;\n" +
                "\t\tnumber = 4 & output= 1 : input - 2*mini;\n" +
                "\t\tnumber = 6 & output= 5 : input - 1*mini;\n" +
                "\t\tnumber = 7 & output= 1 : input - 1*mini;\n" +
                "\t\tnumber = 8 & output= 0 : input - 1*mini;\n" +
                "\t\tnumber = 8 & output= 6 : input - 1*mini;\n" +
                "\t\tnumber = 8 & output= 9 : input - 1*mini;\n" +
                "\t\tnumber = 8 & output= 2 : input - 2*mini;\n" +
                "\t\tnumber = 8 & output= 3 : input - 2*mini;\n" +
                "\t\tnumber = 8 & output= 5 : input - 2*mini;\n" +
                "\t\tnumber = 8 & output= 4 : input - 3*mini;\n" +
                "\t\tnumber = 8 & output= 7 : input - 4*mini;\n" +
                "\t\tnumber = 8 & output= 1 : input - 5*mini;\n" +
                "\t\tnumber = 9 & output= 3 : input - 1*mini;\n" +
                "\t\tnumber = 9 & output= 5 : input - 1*mini;\n" +
                "\t\tnumber = 9 & output= 4 : input - 2*mini;\n" +
                "\t\tnumber = 9 & output= 7 : input - 3*mini;\n" +
                "\t\tnumber = 9 & output= 1 : input - 4*mini;\n" +
                "\t\tTRUE : input;\n" +
                "\t\tesac;";
        */
