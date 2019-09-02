package OOprogram;

import java.util.*;
//import java.util.Set;

public class UniqueWordCounter implements WordCounter{

    @Override
    public Set process(List<String> wordList) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String string : wordList) {
            if (map.containsKey(string)) {
                map.put(string, map.get(string) + 1);
            } else {
                map.put(string, 1);
            }
        }
        return map.entrySet();
    }
}
