package OOprogram;

import java.util.List;

public class NonEnglishFilter implements NonABCFilter{

    @Override
    public List process(List<String> wordList) {
       for(int i = 0; i < wordList.size(); i++){

           wordList.set(i,wordList.get(i).replaceAll("[^a-zA-Z]", "").trim());
           if(wordList.get(i).equals("")){
                wordList.remove(i);
           }
       }
       return wordList;
    }
}
