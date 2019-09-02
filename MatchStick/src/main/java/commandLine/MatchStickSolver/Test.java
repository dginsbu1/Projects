package commandLine.MatchStickSolver;

import java.io.*;

public class Test {
    public static int a, b,c,num;// 8-3=3:1
    public static String sym;
    public static String firstHalf, secondHalf;
    public static void main(String args[]) throws IOException {
        a=8;b=3;c=3;sym="-";num=1;
        setFirstHalf();
        setSecondHalf();
        /*
        for reading one document into another
        String path = "C:\\Users\\dgmon\\GIT\\MatchStick\\src\\main\\resources\\NuSMV.txt";
        InputStream is = new FileInputStream(path);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        */

        //writing
        FileWriter fw=new FileWriter("C:\\Users\\dgmon\\GIT\\MatchStick\\src\\main\\resources\\Test.txt");
        fw.write(firstHalf);
        fw.write("a : newNumMovMatch("+a+","+num+"); --mov*21\n" +
                "\tb : newNumMovMatch("+b+",a.remaining);\n" +
                "\tc : newNumMovMatch("+c+", b.remaining);\n" +
                "\td : newSymbol("+sym+", c.remaining);");
        fw.write(secondHalf);
        fw.close();
        System.out.println("Success...");
    }

    private static void setFirstHalf() {
        firstHalf = "MODULE main\n" +
                "VAR";
    }

    public static void setSecondHalf() {
        secondHalf = "LTLSPEC G (! ((d.remaining = 0) & ( ( (a.output - b.output = c.output) & !d.plus) | ((a.output + b.output = c.output) & d.plus)))  );\n" +
                "--FAIRNESS  \n" +
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
                "\t\tnumber = 0 & sub = 1 & add = 1\t : {0,6,9};\n" +
                "\t\tnumber = 0 & sub = 2 & add = 1   : {0,6,9,2,3};\n" +
                "\t\tnumber = 0 & sub >= 3 & add >= 1 : {0,6,9,2,3,4};\n" +
                "\t\tTRUE : number; esac union case\t\n" +
                "\t\tnumber = 1 & sub = 1 & add = 4\t : {1,2};\n" +
                "\t\tnumber = 1 & sub >=1 & add >= 5\t : {1,2,6};\n" +
                "\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub = 1 & add = 1   : {2,3};\n" +
                "\t\tnumber = 2 & sub = 1 & add >= 2: {2,3,0,6,9}; TRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub = 2 & add >= 2: {2,3,0,6,9,5};TRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub = 3 & add = 1: {7};\n" +
                "\t\tnumber = 2 & sub = 3 & add >= 2: {7,4};TRUE : number; esac union case\n" +
                "\t\tnumber = 2 & sub >= 4 & add >= 1: {1};\n" +
                "\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 3 & sub = 0 & add = 1: {3};\n" +
                "\t\tnumber = 3 & sub = 1 & add = 1: {3,2,5};\n" +
                "\t\tnumber = 3 & sub >= 2: {3,2,5,0,6};\n" +
                "\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 4 & sub = 1 & add = 2 : {4,3,5};\n" +
                "\t\tnumber = 4 & sub >=1 & add >=3: {4,3,5,0,6};TRUE : number; esac union case\n" +
                "\t\tnumber = 4 & sub =2 & (add = 1 | add = 2): {7};\n" +
                "\t\tnumber = 4 & sub >=2 & add >= 3: {2};\n" +
                "\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub = 1 & add = 1 : {3};\n" +
                "\t\tnumber = 5 & sub >= 1 & add >= 2 : {3,0};TRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub = 2 & add = 1 : {4};\n" +
                "\t\tnumber = 5 & sub >= 2 & add >= 2 : {4,2};TRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub >= 3 & add >= 1: {7};TRUE : number; esac union case\n" +
                "\t\tnumber = 5 & sub >= 4 & add >= 1: {1};\n" +
                "\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 6 & sub = 1 & add = 1 : {0,9};\n" +
                "\t\tnumber = 6 & sub = 2 & add = 1 : {0,9,2,3};\n" +
                "\t\tnumber = 6 & sub = 3 & add = 1 : {0,9,2,3,4};\n" +
                "\t\tnumber = 6 & sub = 4 & add = 1 : {0,9,2,3,4,7};\n" +
                "\t\tnumber = 6 & sub >= 5 & add >= 1 : {0,9,2,3,4,7,1};\n" +
                "\t\tTRUE : number; esac union case\n" +
                "\t\tnumber = 7 & sub = 1 & add = 2 : {4};\n" +
                "\t\tnumber = 7 & sub = 1 & add = 3 : {4,2,5};\n" +
                "\t\tnumber = 7 & sub = 1 & add = 4 : {4,2,5,6};\n" +
                "\t\tTRUE : number; esac union case\n" +
                "\t\t--NOTHING FOR 8\n" +
                "\t\tnumber = 9 & sub = 1 & add = 1 : {0,6};\n" +
                "\t\tnumber = 9 & sub >= 2 & add >= 1 : {0,6,2};\n" +
                "\t\tTRUE : number; esac union case\n" +
                "\t\t--just adding--------------\n" +
                "\t\tnumber = 0 & add = 0 : {0};\n" +
                "\t\tnumber = 0 & add >= 1 : {0,8};\n" +
                "\t\tnumber = 1 & add = 0: {1};\n" +
                "\t\tnumber = 1 & add = 1: {1,7};\n" +
                "\t\tnumber = 1 & add = 2: {1,7,4};\n" +
                "\t\tnumber = 1 & add = 3: {1,7,4,3,9};\n" +
                "\t\tnumber = 1 & add = 4: {1,7,4,3,9,0};\n" +
                "\t\tnumber = 1 & add >= 5: {1,7,4,3,9,0,8};\n" +
                "\t\tnumber = 2 & add = 0: {2};\n" +
                "\t\tnumber = 2 & add = 1: {2};\n" +
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
                "\t\tnumber = 0 & sub >= 4  : {0,7,1};\n" +
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
                "DEFINE\n" +
                "\tremaining := case \n" +
                "\t\tnumber = 0 & (output = 6 | output =  9) : input - mini - pls;\n" +
                "\t\tnumber = 0 & (output = 2  | output = 3 | output = 6) : input - (2*mini) - pls;\n" +
                "\t\tnumber = 0 & output = 4    : input - (3*mini) - pls;\n" +
                "\t\tnumber = 1 & (output = 2 | output = 5) : input - (1*mini) - (4*pls);\n" +
                "\t\tnumber = 1 & (output = 6) :  input - (1*mini) - (5*pls);\n" +
                "\t\tnumber = 2 & (output = 3) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 2 & (output = 0|output = 6|output = 9) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 2 & (output = 5) :  input - (2*mini) - (2*pls);\n" +
                "\t\tnumber = 2 & (output = 7) :  input - (3*mini) - (1*pls);\n" +
                "\t\tnumber = 2 & (output = 4) :  input - (3*mini) - (2*pls);\n" +
                "\t\tnumber = 2 & (output = 1) :  input - (4*mini) - (1*pls);\n" +
                "\t\tnumber = 3 & (output = 2 | output = 5) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 3 & (output = 0 | output = 6) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 3 & (output = 4) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 4 & (output = 3 | output = 5) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 4 & (output = 0 | output = 6) :  input - (1*mini) - (3*pls);\n" +
                "\t\tnumber = 4 & (output = 7) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 4 & (output = 2) :  input - (2*mini) - (3*pls);\n" +
                "\t\tnumber = 5 & (output = 3) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 5 & (output = 0) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 5 & (output = 4) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 5 & (output = 2) :  input - (2*mini) - (2*pls);\n" +
                "\t\tnumber = 5 & (output = 7) :  input - (3*mini) - (1*pls);\n" +
                "\t\tnumber = 5 & (output = 1) :  input - (4*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 0 | output = 9) :  input - (1*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 2 | output = 3) :  input - (2*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 4) :  input - (3*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 7) :  input - (4*mini) - (1*pls);\n" +
                "\t\tnumber = 6 & (output = 1) :  input - (5*mini) - (1*pls);\n" +
                "\t\tnumber = 7 & (output = 4) :  input - (1*mini) - (2*pls);\n" +
                "\t\tnumber = 7 & (output = 2 | output = 5) :  input - (1*mini) - (3*pls);\n" +
                "\t\tnumber = 7 & (output = 6) :  input - (1*mini) - (4*pls);\n" +
                "\t\tnumber = 9 & (output = 0 | output = 6) :  input - (1*mini) - (1*pls);\n" +
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
                "\t\tesac;\n" +
                "---------------------------------------\n" +
                "MODULE newNumAddMatch(number, add) --use .remaining / .output\n" +
                "VAR\n" +
                "\toutput: {0,1,2,3,4,5,6,7,8,9};\n" +
                "ASSIGN \n" +
                "\toutput := case\n" +
                "\t\tnumber = 0 & add = 0 : {0};\n" +
                "\t\tnumber = 0 & add >= 1 : {0,8};\n" +
                "\t\tnumber = 1 & add = 0: {1};\n" +
                "\t\tnumber = 1 & add = 1: {1,7};\n" +
                "\t\tnumber = 1 & add = 2: {1,7,4};\n" +
                "\t\tnumber = 1 & add = 3: {1,7,4,3,9};\n" +
                "\t\tnumber = 1 & add = 4: {1,7,4,3,9,0};\n" +
                "\t\tnumber = 1 & add >= 5: {1,7,4,3,9,0,8};\n" +
                "\t\tnumber = 2 & add = 0: {2};\n" +
                "\t\tnumber = 2 & add = 1: {2};\n" +
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
                "\t\tesac;\n" +
                "DEFINE\n" +
                "\tremaining := case \n" +
                "\t\tnumber = 0 & output= 0 : add;\n" +
                "\t\tnumber = 0 & output= 8 : add - 1;\n" +
                "\t\tnumber = 1 & output= 1 : add;\n" +
                "\t\tnumber = 1 & output= 7 : add - 1;\n" +
                "\t\tnumber = 1 & output= 4 : add - 2;\n" +
                "\t\tnumber = 1 & output= 3 : add - 3;\n" +
                "\t\tnumber = 1 & output= 9 : add - 4;\n" +
                "\t\tnumber = 1 & output= 0 : add - 4;\n" +
                "\t\tnumber = 1 & output= 8 : add - 5;\n" +
                "\t\tnumber = 2 & output= 2 : add;\n" +
                "\t\tnumber = 2 & output= 8 : add - 2;\n" +
                "\t\tnumber = 3 & output= 3 : add;\n" +
                "\t\tnumber = 3 & output= 9 : add - 1;\n" +
                "\t\tnumber = 3 & output= 8 : add - 2;\n" +
                "\t\tnumber = 4 & output= 4 : add;\n" +
                "\t\tnumber = 4 & output= 9 : add - 2;\n" +
                "\t\tnumber = 4 & output= 8 : add - 3;\n" +
                "\t\tnumber = 5 & output= 5 : add;\n" +
                "\t\tnumber = 5 & output= 6 : add - 1;\n" +
                "\t\tnumber = 5 & output= 9 : add - 1;\n" +
                "\t\tnumber = 5 & output= 8 : add - 2;\n" +
                "\t\tnumber = 6 & output= 6 : add;\n" +
                "\t\tnumber = 6 & output= 8 : add - 1;\n" +
                "\t\tnumber = 7 & output= 7 : add;\n" +
                "\t\tnumber = 7 & output= 3 : add - 2;\n" +
                "\t\tnumber = 7 & output= 0 : add - 3;\n" +
                "\t\tnumber = 7 & output= 9 : add - 3;\n" +
                "\t\tnumber = 7 & output= 8 : add - 4;\n" +
                "\t\tnumber = 8 & output= 8 : add;\n" +
                "\t\tnumber = 9 & output= 9 : add;\n" +
                "\t\tnumber = 9 & output= 8 : add - 1;\n" +
                "\t\tTRUE : add;\n" +
                "\t\tesac;\n" +
                "---------------------------------------------\n" +
                "MODULE newNumSubMatch(number, sub) --use .remaining / .output\n" +
                "VAR\n" +
                "\toutput: {0,1,2,3,4,5,6,7,8,9};\n" +
                "ASSIGN \n" +
                "\toutput := case\n" +
                "\t\tnumber = 0 & sub = 0 : {0};\n" +
                "\t\tnumber = 0 & sub = 1 : {0};\n" +
                "\t\tnumber = 0 & sub = 2 : {0};\n" +
                "\t\tnumber = 0 & sub = 3 : {0,7};\n" +
                "\t\tnumber = 0 & sub >= 4  : {0,7,1};\n" +
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
                "\t\tnumber = 8 & sub > 5 : {8,0,6,9,2,3,5,4,7,1};\t\t\n" +
                "\t\tnumber = 9 & sub = 0 : {9};\n" +
                "\t\tnumber = 9 & sub = 1 : {9,3,5};\n" +
                "\t\tnumber = 9 & sub = 2 : {9,3,5,4};\n" +
                "\t\tnumber = 9 & sub = 3 : {9,3,5,4,7};\n" +
                "\t\tnumber = 9 & sub > 3 : {9,3,5,4,7,1};\n" +
                "\t\tesac;\n" +
                "DEFINE\n" +
                "\tremaining := case \n" +
                "\t\tnumber = 0 & output= 0 : sub;\n" +
                "\t\tnumber = 0 & output= 7 : sub - 3;\n" +
                "\t\tnumber = 0 & output= 1 : sub - 4;\n" +
                "\n" +
                "\t\tnumber = 1 & output= 1 : sub;\n" +
                "\n" +
                "\t\tnumber = 2 & output= 2 : sub;\n" +
                "\n" +
                "\t\tnumber = 3 & output= 3 : sub;\n" +
                "\t\tnumber = 3 & output= 7 : sub - 2;\n" +
                "\t\tnumber = 3 & output= 1 : sub - 3;\n" +
                "\n" +
                "\t\tnumber = 4 & output= 4 : sub;\n" +
                "\t\tnumber = 4 & output= 1 : sub - 2;\n" +
                "\n" +
                "\t\tnumber = 5 & output= 5 : sub;\n" +
                "\n" +
                "\t\tnumber = 6 & output= 6 : sub;\n" +
                "\t\tnumber = 6 & output= 5 : sub - 1;\n" +
                "\n" +
                "\t\tnumber = 7 & output= 7 : sub;\n" +
                "\t\tnumber = 7 & output= 1 : sub - 1;\n" +
                "\n" +
                "\t\tnumber = 8 & output= 8 : sub;\n" +
                "\t\tnumber = 8 & output= 0 : sub - 1;\n" +
                "\t\tnumber = 8 & output= 6 : sub - 1;\n" +
                "\t\tnumber = 8 & output= 9 : sub - 1;\n" +
                "\t\tnumber = 8 & output= 2 : sub - 2;\n" +
                "\t\tnumber = 8 & output= 3 : sub - 2;\n" +
                "\t\tnumber = 8 & output= 5 : sub - 2;\n" +
                "\t\tnumber = 8 & output= 4 : sub - 3;\n" +
                "\t\tnumber = 8 & output= 7 : sub - 4;\n" +
                "\t\tnumber = 8 & output= 1 : sub - 5;\n" +
                "\n" +
                "\t\tnumber = 9 & output= 9 : sub;\n" +
                "\t\tnumber = 9 & output= 3 : sub - 1;\n" +
                "\t\tnumber = 9 & output= 5 : sub - 1;\n" +
                "\t\tnumber = 9 & output= 4 : sub - 2;\n" +
                "\t\tnumber = 9 & output= 7 : sub - 3;\n" +
                "\t\tnumber = 9 & output= 1 : sub - 4;\n" +
                "\t\tTRUE : sub;\n" +
                "\t\tesac;";
    }



}
