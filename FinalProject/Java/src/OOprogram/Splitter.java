package OOprogram;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
//split the text into individual words
public class Splitter implements WordFinder{

    @Override
    public List<String> process(String text) {
        //TODO verify that this is correct regex
        //return Arrays.asList(text.split("\\W+"));
        List<String> list = new LinkedList<String>(Arrays.asList(text.split("\\s+")));
        return list;
    }
}
