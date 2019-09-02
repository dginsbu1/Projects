package commandLine.creator;

import java.util.Random;

//creates/prints a random addition or subtraction equation
// using digits 0-9
public class EquationCreator {
    static int limit = 10;
    static Random randomNumGen = new Random();

    //prints out a random equation
    public static void main(String[] args){
        System.out.println(equationToString(createEquation()));
    }

    //creates and returns a String array containing the elements of an random equation
    //using positive nunbers 0-9 ex. [2,+,2,=,4]
    public static String[] createEquation(){
        int randIntOne = randomNumGen.nextInt(limit);
        String sym = getRandSymbol();
        int randIntTwo = getRandNum(randIntOne, sym);
        int result = getResult(randIntOne,sym,randIntTwo);
        String[] equation = {randIntOne+"",sym,randIntTwo+"","=", result+""};
        return equation;
    }
    //creates and returns a String array containing the elements of an random equation
    //using positive digits (0) through (max-1)
    public static String[] createEquation(int max){
        limit = max;
        return createEquation();
    }
    //returns a random arithmetic symbol ("-","+")
    private static String getRandSymbol() {
        String[] symArr = {"+","-"};
        int symPos = (int)(Math.random()*(symArr.length));
        String sym = symArr[symPos];
        return sym;
    }
    //returns a random number between 0 and limit-1 (9) depending on the inputs
    private static int getRandNum(int rand1, String sym) {
        int high = 0;
        if(sym.equals("+")){//guarantees the result won't exceed the limit
            high = limit - rand1;
        }
        else if(sym.equals("-")){//guarantees the result will not be negative
            high = rand1+1;
        }
        return randomNumGen.nextInt(high);
    }
    //@return the result of either adding or subtracting the two inputs
    private static int getResult(int rand1, String sym, int rand2) {
        if(sym.equals("+")){//addition
            return rand1 + rand2;
        }
        else if(sym.equals("-")){//subtraction
            return rand1 - rand2;
        }
        return -1;//should never happen
    }
    //turns an String array into a regular String
    public static String equationToString(String[] equation){
         StringBuilder equ = new StringBuilder();
         for(String element : equation){
             equ.append(element);
         }
         return equ.toString();
    }

}
