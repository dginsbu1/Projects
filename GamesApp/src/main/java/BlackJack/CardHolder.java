package BlackJack;

import java.util.ArrayList;
public class CardHolder{

    private ArrayList<Card> hand = new ArrayList<Card>();
    private double money;

    // No paramaters constructor.
    // sets
    public CardHolder(){
        money = 0;
    }

    //returns the sum of the cardHolders hand taking into account that
    //aces can count for either 1 or 11 depending on the other cards in the hand
    public int getSum(){
        int sum = 0;
        int countAce = 0;
        for(Card a : hand){
            if(a.getNum() == 1)
                countAce++;
            sum += a.getNum();
        }
        if(countAce == 0)
            return sum;
        if(sum < 12)
            sum += 10;
        return sum;
    }

    //This prints out the contents of the dealer's hand
    public void showHand(){
        Card c;
        ArrayList<Card> hand = getHand();
        System.out.print("The dealer has ");
        for(int i = 0; i  < hand.size(); i++){
            c = hand.get(i);
            if(i == hand.size() - 1 && i != 0)
                System.out.println("and the " +  c.getValue() + " of " +  c.getSuit());
            else
                System.out.println("the " + c.getValue() + " of " + c.getSuit() + ", ");
        }
    }

    //Getters

    //returns the cardHolders hand
    public ArrayList<Card> getHand(){
        return hand;
    }
    //returns the cardHolders money
    public double getMon(){
        return money;
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

    //@param double x
    //sets cardHolders money to x
    public void setMon(double x){
        money = x;
    }
    //@param a double x
    //adds x to CardHolders hand
    public void addMon(double x){
        money += x;
    }
    //@param a double x
    //subtracts x from cardHolders hand
    public void subtractMon(double x){
        money -= x;
    }
}
