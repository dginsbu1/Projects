package BlackJack;

import java.util.ArrayList;

public class Deck{

    private ArrayList<Card> deck = new ArrayList<Card>();
    //This makes a deck consisting of 52 cards.
    //The ace is called 1.
    public Deck(){
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 10; j++){
                if(i == 0)
                    deck.add(new Card(j + 1, "Clubs"));
                if(i == 1)
                    deck.add(new Card(j + 1, "Diamonds" ));
                if(i == 2)
                    deck.add(new Card(j + 1, "Hearts" ));
                if(i == 3)
                    deck.add(new Card(j + 1, "Spades" ));
            }
        deck.add(new PictureCard("Jack", "Clubs"));
        deck.add(new PictureCard("Queen", "Clubs"));
        deck.add(new PictureCard("King", "Clubs"));
        deck.add(new PictureCard("Jack", "Diamonds"));
        deck.add(new PictureCard("Queen", "Diamonds"));
        deck.add(new PictureCard("King", "Diamonds"));
        deck.add(new PictureCard("Jack", "Hearts"));
        deck.add(new PictureCard("Queen", "Hearts"));
        deck.add(new PictureCard("King", "Hearts"));
        deck.add(new PictureCard("Jack", "Spades"));
        deck.add(new PictureCard("Queen", "Spades"));
        deck.add(new PictureCard("King", "Spades"));
    }

    //takes a card out of the deack and returns it
    public Card takeAnotherCard(){
        return  deck.remove((int)(Math.random() * deck.size()));
    }
}





