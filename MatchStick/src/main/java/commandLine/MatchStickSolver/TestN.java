package commandLine.MatchStickSolver;

import java.io.IOException;

public class TestN {
    public static void main(String[] args) throws IOException, InterruptedException {
        MatchStick matchStick = new MatchStick();
        String[] answers = matchStick.getAllAnswers("1-0=3", "a3").toArray(new String[0]);
        for(String element : answers)
            System.out.println(element);
    }
}
