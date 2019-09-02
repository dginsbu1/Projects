package Castle;

import java.util.Comparator;
public class CardComparator implements Comparator<Card>{
    public int compare(Card c1, Card c2){
        int diff = c1.getVal() - c2.getVal();
        if(diff == 0)
            diff = c1.getSuit().compareTo(c2.getSuit());
        return diff;
    }
}
