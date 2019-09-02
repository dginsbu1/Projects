package Castle;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
public class Player{

    private ArrayList<Card> hand;
    private String name;
    private Scanner in;
    // No paramaters constructor.
    public Player(){
        hand = new ArrayList<Card>();
        in = new Scanner(System.in);
    }

    //@param nam. makes a player with name nam.
    public Player(String nam){
        hand = new ArrayList<Card>();
        name = nam;
        in = new Scanner(System.in);
    }

    //Getters
    //returns the players name
    public String getName(){
        return name;
    }

    //returns the cardHolders hand
    public ArrayList<Card> getHand(){
        return hand;
    }
    public int getHandSize(){
        return hand.size();
    }

    //various methods of adding cards to a players hand

    //@param Card c
    //adds c to the cardHolders hand
    public void addToHand(Card c){
        hand.add(c);
        sortt();
    }

    //adds pile to the hand and sorts it
    public void addToHand(Card[] pile){
        for(Card c : pile)
            hand.add(c);
        // System.out.println("OK");
        sortt();
    }

    //same as previous but with an arrayList
    public void addToHand(ArrayList<Card> pile){
        for(Card c : pile)
            hand.add(c);
        sortt();
    }

    //asks the player for a number
    public int askForVal(){
        while(in.hasNextInt() == false){
            System.out.println("Please type a valid number");
            in.nextLine();
        }
        int num = in.nextInt();
        in.nextLine();
        return num;
    }

    //asks the player for the first letter of suit names
    public String[] askForSuit(){
        System.out.println("Please enter the first letter of the suit(s) of the card(s) you would like to play." +
                "For example if you would like to play a spade and a club enter s c. Don't forget the space.");
        String suits = in.nextLine().trim();
        String[] word = suits.split("\\W");
        return word;
    }

    //tells the player his hand
    public void showHand(){
        System.out.println("You have");
        for(Card c : hand)
            if(!c.isFaceDown())
                System.out.print(c);
        System.out.println();
    }

    //ensures that the suits are valid
    public String[] validateAndAdjustSuits(String[] suits){
        for(int i = 0; i < suits.length; i++){
            if(suits[i].equalsIgnoreCase("c"))
                suits[i] = "Clubs";
            else if(suits[i].equalsIgnoreCase("d"))
                suits[i] = "Diamonds";
            else if(suits[i].equalsIgnoreCase("h"))
                suits[i] = "Hearts";
            else if(suits[i].equalsIgnoreCase("s"))
                suits[i] = "Spades";
            else{
                System.out.println("Sorry, you typed in the wrong suit letter. ");
                suits = askForSuit();
                i = -1;
            }
        }
        return suits;
    }

    //returns an array of Cards based on player input
    public ArrayList<Card> act(){
        int num;
        showHand();
        //first it gets a value
        System.out.println("What would you like to play? Please enter the value of your card. Aces are 14, Kings are 13," +
                " Queens are 12...");
        num = askForVal();
        while(num > 14 || num <= 1){
            System.out.println("Please enter an int within 2 and 14.");
            num = askForVal();
        }
        //now it gets the suits
        String[] word = askForSuit();
        word = validateAndAdjustSuits(word);
        ArrayList<Card> cards = new ArrayList<Card>(word.length);
        for(int i = 0; i < word.length; i++)
            if(num < 11)
                cards.add(new Card(num, word[i]));
            else
                cards.add(new PictureCard(num, word[i]));
        //verifies that the cards typed in exist in the players hand and he didn't type in the same card twice
        for(Card c : cards)
            if(hand.indexOf(c) == -1 || hand.get(hand.indexOf(c)).isFaceDown())
                return null;
        for(int i = 0; i < cards.size(); i++)
            for(int j = 0; j < cards.size(); j++)
                if(cards.get(i).getSuit().equals(cards.get(j).getSuit()) && i != j)
                    return null;

        return cards;
    }
    //once the player is down to his last three cards,
    //It asks the player which of the final three cards he would like to play
    public ArrayList<Card> finalThree(){
        ArrayList<Card> card = new ArrayList<Card>();
        System.out.println("Which card would you like to play? 1 through " + hand.size());
        int cardNum = askForVal();
        while( cardNum < 1 || cardNum > hand.size()){
            System.out.println("Please enter a valid int.");
            cardNum = askForVal();
        }
        //turn it face up
        hand.get(cardNum - 1).setFace(true);
        card.add(hand.get(cardNum - 1));
        System.out.println("You chose " + card.get(0).toString());
        return card;
    }

    //sorts the players hand
    //it has to remove the face down cards, sort the remaining cards and then put the face down cards down in order.
    public void sortt(){
        ArrayList<Card> temp = new ArrayList<Card>();
        for(int i = hand.size() - 1; i >= 0; i--)
            if(hand.get(i).isFaceDown()){
                Card c = hand.get(i);
                temp.add(hand.remove(hand.indexOf(c)));
            }
        Collections.sort(hand);
        for(int i = 0; i < temp.size(); i++)
            hand.add(i, temp.get(i));
    }

    //sets the cardHolders hand to an empty ArraList<Card>
    public void resetHand(){
        hand = new ArrayList<Card>();
    }

}
