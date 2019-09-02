package Castle;


public class Card implements Comparable<Card>{
    //This is a normal card. It has a number, a name, and a suit.
    private int value;
    private String suit;
    private String name;
    private boolean isFaceUp;
    //constructs a card with value val and suit str
    public Card(int val, String str){
        value = val;
        name = "" + val;
        suit = str;
        isFaceUp = true;
    }
    //No parameters constructor used in subclasses
    public Card(){
        value = 0;
        name = "";
        suit = "";
        isFaceUp = true;
    }
    //compares 2 cards by number and, if needed by suit. returns negative if it is lesser and positive if it is greater.
    // it returns 0 if the cards are equal
    public int compareTo(Card other){
        if(value < other.value)
            return -1;
        else if( value > other.value)
            return 1;
        return suit.compareTo(other.suit);
    }
    //returns a boolean of if the cards are equal
    public boolean equals(Object other){
        return value == ((Card)other).value && suit.equals(((Card)other).suit);
    }
    // Getters

    //Returns the value
    public int getVal(){
        return value;
    }
    //Returns the suit
    public String getSuit(){
        return suit;
    }
    //Returns the name
    public String getName(){
        return name;
    }
    //Returns !isFaceUp
    public boolean isFaceDown(){
        return !isFaceUp;
    }
    //Setters

    //@param String str
    // sets Cards suit to str
    public void setSuit(String str){
        suit = str;
    }
    //sets name to nam
    public void setName(String nam){
        name = nam;
    }
    //sets vlaue to val
    public void setVal(int val){
        value = val;
    }
    //sets isFaceUp to tf
    public void setFace(boolean tf){
        isFaceUp = tf;
    }
    //returns the card in the form of a string. the name and the first letter of the suit
    public String toString(){
        return getVal() + getSuit().substring(0,1) + " ";
    }
}




