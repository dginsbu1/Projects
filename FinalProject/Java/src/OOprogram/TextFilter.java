package OOprogram;

import javax.lang.model.element.NestingKind;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TextFilter implements LineFilter{

//    public static void main(String[] args) throws IOException {
//        String path = ("C:\\Users\\dgmon\\YU\\test.txt");
//        String text = DocumentProcessorController.pathToString(path);
//        System.out.println(text);
//        TextFilter filter = new TextFilter("this");
//        System.out.println(filter.process(text));
//    }

    private String searchString;
    public TextFilter(String searchString){
        this.searchString = searchString;
    }
    @Override
    public String process(String text){
        StringBuilder filteredText = new StringBuilder();
        Scanner scanner = new Scanner(text);
        while(scanner.hasNextLine()){
            String nextLine = scanner.nextLine();
            if(nextLine.contains(searchString)){
                filteredText.append(nextLine + "\n");
            }
        }
        return filteredText.toString();
    }
}
