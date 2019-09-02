package BlackJack;

import java.util.ArrayList;
public class Player extends CardHolder{

    private String name;
    //@param double mon, and String str
    //Constructs a player with name str and money mon
    public Player(double mon, String str){
        super.setMon(mon);
        name = str;
    }
    //returns players name
    public String getName(){
        return name;
    }

    //This simply prints out the contents of the player's hand
    public void showHand(){
        Card c;
        ArrayList<Card> hand = getHand();
        System.out.print("You have ");
        for(int i = 0; i  < hand.size(); i++){
            c = hand.get(i);
            if(i == hand.size() - 1 && i != 0)
                System.out.println("and the " +  c.getValue() + " of " +  c.getSuit());
            else
                System.out.println("the " + c.getValue() + " of " + c.getSuit() + ", ");
        }
    }

}



