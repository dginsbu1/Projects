package BlackJack;

public class Card{
    //This is a normal card. It has a number and a suit.
    private int num;
    private String suit;

    public Card(int x, String y){
        num = x;
        suit = y;
    }
    //No parameters constructor used in subclasses
    public Card(){
        num = 0;
        suit = "";
    }
    // Getters

    //Returns the number
    public int getNum(){
        return num;
    }
    //Returns the suit
    public String getSuit(){
        return suit;
    }
    //Returns the number in String form
    public String getValue(){
        return ("" + getNum());
    }
    //@param String str
    // sets Cards suit to str
    public void setSuit(String str){
        suit = str;
    }
}



