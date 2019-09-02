package OOprogram;


import javax.print.attribute.standard.NumberUp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentProcessor{

    private final LineFilter lineFilter;
    private final CaseConverter caseConverter;
    private final NonABCFilter nonABCFilter;
    private final WordFinder wordFinder;
    private final WordCounter wordCounter;
    boolean grep, wc;
    String text;

    private DocumentProcessor(DocumentProcessorBuilder builder){
        this.lineFilter = builder.getLineFilter();
        this.caseConverter = builder.getCaseConverter();
        this.nonABCFilter = builder.getNonABCFilter();
        this.wordFinder = builder.getWordFinder();
        this.wordCounter = builder.getWordCounter();
        grep = lineFilter != null;
        wc = caseConverter != null;
    }


    public Object process(String text){
        this.text = text;
        if(grep && wc){
            return processAll();
        }
        else if(grep){
            return processGrep();
        }
        else if(wc){
            return processWc();
        }
        else throw new IllegalArgumentException("Neither grep no WC was called");
    }

    private Map<Integer, String> processAll(){
        processGrep();
        return processWc();
    }

    private String processGrep(){
        text = lineFilter.process(text);
        return text;
    }

    private Map<Integer, String> processWc(){
        Set set = wordCounter.process(nonABCFilter.process(wordFinder.process(caseConverter.process(text))));
        return toMap(set);
    }
    private Map<String, Integer> toMap(Set<Map.Entry> set) {
        Map map = new HashMap<>();
        set.forEach(t -> map.put(t.getKey(),t.getValue()));//contains same key and value pair
        return map;
    }





    public static class DocumentProcessorBuilder {

        private  LineFilter lineFilter;
        private  CaseConverter caseConverter;
        private  WordFinder wordFinder;
        private  NonABCFilter nonABCFilter;
        private  WordCounter wordCounter;

        public DocumentProcessor build(){
            return new DocumentProcessor(this);
        }


        public LineFilter getLineFilter() {return lineFilter;}

        public CaseConverter getCaseConverter() {return caseConverter;}

        public NonABCFilter getNonABCFilter() {return nonABCFilter;}

        public WordFinder getWordFinder() {return wordFinder;}

        public WordCounter getWordCounter() {return wordCounter;}

        public DocumentProcessorBuilder setLineFilter(LineFilter lineFilter) {
            this.lineFilter = lineFilter;
            return this;
        }

        public DocumentProcessorBuilder setCaseConverter(CaseConverter caseConverter) {
            this.caseConverter = caseConverter;
            return this;
        }

        public DocumentProcessorBuilder setNonABCFilter(NonABCFilter nonABCFilter) {
            this.nonABCFilter = nonABCFilter;
            return this;
        }

        public DocumentProcessorBuilder setWordFinder(WordFinder wordFinder) {
            this.wordFinder = wordFinder;
            return this;
        }

        public DocumentProcessorBuilder setWordCounter(WordCounter wordCounter) {
            this.wordCounter = wordCounter;
            return this;
        }
    }
}


