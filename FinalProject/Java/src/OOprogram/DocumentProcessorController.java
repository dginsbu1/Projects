package OOprogram;


import jdk.internal.dynalink.beans.StaticClass;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentProcessorController {
    String searchString;
    String path;
    String text;
    boolean grep, wc = false;
    DocumentProcessor.DocumentProcessorBuilder builder;
    DocumentProcessor processor;

    public DocumentProcessorController() {}

    public void beginProcess(String[] args) throws IOException {
        buildProcessor(args);
        Object result = processor.process(pathToString(path));
        if(grep && !wc){
            String resultString;
            resultString = (String) result;
            System.out.println(resultString);
        }
        else{
            Map<String, Integer> resultMap;
            resultMap = (Map) result;
            Set<Map.Entry<String, Integer>> set = resultMap.entrySet();
            for(Map.Entry entry: set){
                System.out.println(entry);
            }
        }

    }

    private void buildProcessor(String[] args) throws IOException {
        reset();
        builder = new DocumentProcessor.DocumentProcessorBuilder();
        if (args[0].equals("grep")) {//basic grep ex. "grep this silly.txt"
            searchString = args[1];
            path = args[2];
            grep = true;
            builder.setLineFilter(new TextFilter(searchString));
            if (args.length != 3) {//complicated grep ex.  "grep this silly.txt | wc"
                wc = true;
                setWC(builder);
            }
        } else if (args[0].equals("wc")) {//just wc. ex. "wc silly.text"
            path = args[1];
            wc = true;
            setWC(builder);
        }
        //System.out.printf("Grep: %b, wc: %b \n", grep, wc);
        text = pathToString(path);
        processor = builder.build();
    }
    //resets all variables that may have been changed in past iteration.
    private void reset(){
        searchString = null;
        path = null;
        text = null;
        grep = false;
        wc = false;
        builder = null;
        processor = null;
    }

    public static String pathToString(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while(line != null){
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        String fileAsString = sb.toString();
        return fileAsString;
    }

    public void setWC(DocumentProcessor.DocumentProcessorBuilder builder) {
        builder.setCaseConverter(new LowerCaser());
        builder.setWordFinder(new Splitter());
        builder.setNonABCFilter(new NonEnglishFilter());
        builder.setWordCounter(new UniqueWordCounter());
    }
}
