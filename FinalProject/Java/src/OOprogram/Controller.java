package OOprogram;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controller {
    public static void main(String args[] ) throws IOException {
        DocumentProcessorController processor = new DocumentProcessorController();
        processor.beginProcess(args);

        //TODO GROUP TESTING
//        DocumentProcessorController processor = new DocumentProcessorController();
//        String[] wc = {"wc","C:\\Users\\dgmon\\YU\\test.txt"};
//        String[] grep = {"grep","this", "C:\\Users\\dgmon\\YU\\test.txt"};
//        String[] grepWc = {"grep","this", "C:\\Users\\dgmon\\YU\\test.txt", "|", "wc"};
//
//        processor.beginProcess(wc);
//        processor.beginProcess(grep);
//        processor.beginProcess(grepWc);
        //TODO INDIVIDUAL TESTING
//        String answer;
//        List<String> listAnswer;
//        Set<Map.Entry> setAnswer;
//        String path = ("C:\\Users\\dgmon\\YU\\test.txt");
//        String text = DocumentProcessorController.pathToString(path);
//        System.out.println(text);
//        //TODO TEXTFILTER
//        TextFilter filter = new TextFilter("this");
//        //System.out.println(filter.process(text));
//        //TODO CONVERT CASE
//        LowerCaser caser = new LowerCaser();
//        answer = caser.process(text);
//        System.out.println(answer);
//
//        //TODO FIND WORDS
//        Splitter splitter = new Splitter();
//        listAnswer = splitter.process(answer);
//        System.out.println(listAnswer);
//
//        //TODO REMOVE NONABC
//        NonABCFilter abcFilter = new NonEnglishFilter();
//        listAnswer = abcFilter.process(listAnswer);
//        System.out.println(listAnswer);
//
//         //TODO COUNT WORDS
//        UniqueWordCounter wordCounter = new UniqueWordCounter();
//        setAnswer = wordCounter.process(listAnswer);
//        System.out.println(setAnswer);

    }
}
