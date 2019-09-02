package Castle;

import java.util.ArrayList;

public class DeckTwo{

    private ArrayList<Card> deck = new ArrayList<Card>();
    private final String[] NAMES = {"Jack", "Queen", "King", "Ace", "Clubs", "Diamonds", "Hearts", "Spades"};
    //This makes a deck consisting of 52 cards.
    //The ace is called 1.
    public DeckTwo(){
        for(int i = 0; i < 4; i++)
            for(int j = 1; j < 10; j++){
                if(i == 0)
                    deck.add(new Card(j + 1, "Clubs"));
                if(i == 1)
                    deck.add(new Card(j + 1, "Diamonds" ));
                if(i == 2)
                    deck.add(new Card(j + 1, "Hearts" ));
                if(i == 3)
                    deck.add(new Card(j + 1, "Spades" ));
            }
        //this does the picture cards
        for(int i = 0; i <= 3; i++)
            for(int j = 4; j < NAMES.length; j++)
                deck.add(new PictureCard(NAMES[i],NAMES[j]));
    }

    //takes a card out of the deack and returns it
    public Card nextCard(){
        return  deck.remove((int)(Math.random() * deck.size()));
    }
    //returns the deck
    public ArrayList<Card> getDeck(){
        return deck;
    }
    //returns the size of the deck
    public int getSize(){
        return deck.size();
    }
}





