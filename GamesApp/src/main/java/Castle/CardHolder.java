package Castle;

import java.util.ArrayList;
public class CardHolder{

    private ArrayList<Card> hand;

    // No paramaters constructor.
    public CardHolder(){
        hand = new ArrayList<Card>();
    }
    // Gives cards to the cardHolder's hand if he has to pick up the pile
    //public void addCards(ArrayList<Card> pile){
    //for(int i = 0; i <
    //use merge sort
    //  }


    //Getters

    //returns the cardHolders hand
    public ArrayList<Card> getHand(){
        return hand;
    }

    //@param Card c
    //adds c to the cardHolders hand
    public void addToHand(Card c){
        hand.add(c);
    }
    //sets the cardHolders hand to an empty ArraList<Card>
    public void resetHand(){
        hand = new ArrayList<Card>();
    }

}

