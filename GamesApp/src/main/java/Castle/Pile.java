package Castle;

import java.util.ArrayList;
public class Pile{
    private ArrayList<Card> pile;

    //no args constructor. sets pile to an empty arrayList
    public Pile(){
        pile = new ArrayList<Card>();
    }

    //checks if cards can be added to the pile,adds them if they can, deals with special cases
    //and returns a string based on if the cards were added
    public String addCards(ArrayList<Card> cards){
        if(!check(cards))
            return "false";
        for(Card c : cards){
            pile.add(c);
            //tens are special
            if (c.getVal() == 10){
                pile = new ArrayList<Card>();
                return "goAgain";
            }
        }
        if(checkForCodek()){
            System.out.println("CODEK");
            pile.clear();
            return "goAgain";
        }
        return "true";
    }


    //returns the value of the top card
    public int topCardVal(){
        if(pile.size() == 0)
            return 0;
        int val = pile.get(pile.size() - 1).getVal();
        return val;
    }

    //returns true if cards can be played, otherwise returns false
    public boolean check(ArrayList<Card> cards){
        int val = cards.get(0).getVal();
        if(val == 2 || val == 10)
            return true;
        //sevens are special
        if(topCardVal() == 7)
            return (topCardVal() >= val);
        return (topCardVal() <= val);
    }
    //returns true if a codek had happend. a codek is when all four cards of the
    //same suit have been played one on top of each other. This results in a clearing of the pile
    public boolean checkForCodek(){
        int size = pile.size();
        if(size >= 4){
            for(int i = size-1; i >= size-3; i--)
                if(pile.get(i).getVal() != pile.get(i-1).getVal())
                    return false;
            return true;
        }
        else return false;
    }
    //returns the pile
    public ArrayList<Card> getPile(){
        ArrayList<Card> temp = new ArrayList<Card>(pile.size());
        for(Card c : pile)
            temp.add(c);
        pile.clear();
        return temp;
    }
}

