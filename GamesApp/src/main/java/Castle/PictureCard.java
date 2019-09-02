package Castle;

public class PictureCard extends Card{

    //The value of the card is either Jack, Queen, or King
    //It is instead of the number of a regular card
    public PictureCard(String nam, String suit){
        setName(nam);
        setSuit(suit);
        //set values
        if(nam.equals("Jack"))
            setVal(11);
        else if(nam.equals("Queen"))
            setVal(12);
        else if(nam.equals("King"))
            setVal(13);
        else if(nam.equals("Ace"))
            setVal(14);
    }
    //makes a picture card based on int val and String suit
    public PictureCard(int val, String suit){
        setVal(val);
        setSuit(suit);
        //set name
        if( val == 11)
            setName("Jack");
        else if( val == 12)
            setName("Queen");
        else if( val == 13)
            setName("King");
        else if( val == 14)
            setName("Ace");
    }
}

