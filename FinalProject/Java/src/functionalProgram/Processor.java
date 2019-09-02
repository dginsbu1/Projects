package functionalProgram;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Annotation;
import java.util.function.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Processor {

//TODO LINK TO STREAMS AND FUNCTIONAL https://www.geeksforgeeks.org/stream-in-java/
    public void process(String[] args) throws IOException {
        //set up grep ad wc
        boolean grep = false;
        boolean wc = false;
        String path = "";
        if(args[0].equals("grep")) {
            path = args[2];
            grep = true;
            if (args.length == 5)
                wc = true;
        }else{
            wc = true;
            path = args[1];
        }


        //get text
        File file = new File(path);
        List<String> lines = Files.readAllLines(Paths.get(path));

        //perform task based on grep/wc structure
        if(grep && !wc){ //basic grep ex. "grep this silly.txt"
            lines.stream().filter(line -> line.contains(args[1])).forEach(y->System.out.println(y));
        }
        else if(grep && wc){//complicated grep ex.  "grep this silly.txt | wc"
            Map myMap =
                lines.stream()
                    .filter(line -> line.contains(args[1]))
                    .map(line -> line.toLowerCase())
                    .flatMap(line -> Stream.of(line.split("\\s+")))//split
                    .map(word -> word.replaceAll("[^a-zA-Z]", "").trim())
                    .filter(word -> !word.equals(""))//get rid of "" that were counted as words
                    .map(word -> new AbstractMap.SimpleEntry<String,Integer>(word, 1))
                    .collect(Collectors.toMap(word -> word.getKey(), word2 -> word2.getValue(), (wordA, wordB) -> wordA+wordB));
            printMe(myMap);

        } else if(wc){//just wc. ex. "wc silly.text")
            Map WCMap =
                lines.stream()
                    .map(line -> line.toLowerCase())
                    .flatMap(line -> Stream.of(line.split("\\s+")))//split
                    .map(word -> word.replaceAll("[^a-zA-Z]", "").trim())
                    .filter(line -> !line.equals(""))//get rid of "" that were counted as words
                    .map(word -> new AbstractMap.SimpleEntry<String,Integer>(word, 1))
                    .collect(Collectors.toMap(word -> word.getKey(), word2 -> word2.getValue(), (wordA, wordB) -> wordA+wordB));
                    //.forEach(y->System.out.println(y));
            printMe(WCMap);

        }
    }
    public static void printMe(Map<String, Integer> map) {
        for(Map.Entry entry : map.entrySet()){
            System.out.println(entry.getKey()+" "+ entry.getValue());
        }
    }



    public static void main(String[] args) throws IOException {
        Processor processor = new Processor();
        processor.process(args);
    }
}

