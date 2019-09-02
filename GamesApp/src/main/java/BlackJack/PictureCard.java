package BlackJack;

public class PictureCard extends Card{

    private String value;

    //The value of the card is either Jack, Queen, or King
    //It is instead of the number of a regular card
    public PictureCard(String num, String str){
        value = num;
        super.setSuit(str);
    }
    // Getters

    //All picture cards have a value of 10.
    public int getNum(){
        return 10;
    }
    //Returns the value of the card
    public String getValue(){
        return value;
    }
}

