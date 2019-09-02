package Castle;

import java.util.Scanner;
import java.util.ArrayList;
public class Test {
    static Pile pile = new Pile();
    static ArrayList<Card> playerCards = new ArrayList<Card>();
    static DeckTwo deck2 = new DeckTwo();
    static Scanner in = new Scanner(System.in);
    static String playerResult;
    static String result;

    //this is the players turn.it asks the playrer for cards tries to play them and acts accordingly.
    //It returns a string based on the outcome of trying to play the cards
    public static String playerTurn(Player p){
        //tell player top card val
        System.out.println(" The top card has a value of " + pile.topCardVal());
        if(p.getHand().size() < 4 && p.getHand().get(p.getHandSize() - 1).isFaceDown()){
            System.out.println("You are up to your last three cards");
            playerCards = p.finalThree();
        }
        else{
            if(p.getHand().size() < 7 && p.getHand().get(p.getHandSize() - 1).isFaceDown()){
                System.out.println("You are up to your three face up cards");
                for( int i = 3; i < p.getHandSize(); i++)
                    p.getHand().get(i).setFace(true);
            }
            //ask player what he wants to play
            playerCards = p.act();
            while(playerCards == null){
                System.out.println("Sorry, you typed in a card that you don't have. please try again");
                playerCards = p.act();
            }
        }
        //If the cards were added to the pile, they are taken out of the players hand
        playerResult = pile.addCards(playerCards);
        if(playerResult.equals("true") || playerResult.equals("goAgain")){
            for(Card c : playerCards)
                p.getHand().remove(c);
            replenish(p);
        }
        else
            p.addToHand(pile.getPile());
        return playerResult;
    }

    //this is the computers turn. it finds the lowest card to play and plays it.
    public static String computerTurn(Player cpu){
        String result = new String();
        ArrayList<Card> cpuCards = new ArrayList<Card>();
        ArrayList<Card> twoOrTen = new ArrayList<Card>();
        boolean canPlay = false;
        if(cpu.getHandSize() < 4 && cpu.getHand().get(0).isFaceDown()){
            System.out.println("The computer is up to his last three cards");
            cpuCards.add(cpu.getHand().get(cpu.getHandSize() - 1));
            cpuCards.get(0).setFace(true);
            result = pile.addCards(cpuCards);
            if(result.equals("true") || result.equals("goAgain"))
                cpu.getHand().remove(cpuCards.get(0));
            else
                cpu.addToHand(pile.getPile());
            return result;
        }
        else{
            if(cpu.getHandSize() < 7 && cpu.getHand().get(3).isFaceDown()){
                for( int i = 3; i < cpu.getHandSize(); i++)
                    cpu.getHand().get(i).setFace(true);
                System.out.println("The computers is up to his three face up cards. ");
                System.out.println("The computers cards are");
                for(Card c : cpu.getHand())
                    if(!c.isFaceDown())
                        System.out.print(c);
                System.out.println();

            }
            //looks for a card to play
            for(Card c : cpu.getHand()){
                if(!c.isFaceDown()){
                    int cVal = c.getVal();
                    if(cVal != 2 && cVal != 10){
                        cpuCards.add(c);
                        canPlay = pile.check(cpuCards);
                        if(canPlay){
                            cpu.getHand().remove(c);
                            //finds all the cards with the same value as the one it is going to play and puts them all together
                            for(int i = 0; i < cpu.getHandSize(); i++){
                                Card ca = cpu.getHand().get(i);
                                if(!ca.isFaceDown())
                                    if(cVal == ca.getVal() && !c.equals(ca)){
                                        cpuCards.add(cpu.getHand().remove(i));
                                        i--;
                                    }
                            }
                            result = pile.addCards(cpuCards);
                            replenish(cpu);
                            return result;
                        }
                        else
                            cpuCards.clear();
                    }
                    //if the computer can't play a regular card, it sees if it has any twos or tens to play
                    else if(twoOrTen.size() == 0)
                        twoOrTen.add(c);
                }
            }
            if(!canPlay && twoOrTen.size() != 0){
                cpu.getHand().remove(twoOrTen.get(0));
                result = pile.addCards(twoOrTen);
                replenish(cpu);
            }
            //if it can't do anything it takes the pile
            else{
                cpu.addToHand(pile.getPile());
                result = "false";
            }
            return result;
        }
    }

    //once a player goes he needs to take/get cards until he has at least three fase up cards in his hand
    public static void replenish(Player p){
        int numCards = p.getHand().size();
        while(numCards < 9){
            if(deck2.getSize() != 0)
                p.addToHand(deck2.nextCard());
            numCards++;
        }
    }
    public static boolean checkForWin(Player p, Player c){
        if(p.getHandSize() == 0){
            p.getHand().clear();
            c.getHand().clear();
            pile = new Pile();
            deck2 = new DeckTwo();
            return true;
        }
        return false;
    }
    public static void playerWon(Player p){
        System.out.println("Congradualations " + p.getName() + " you won!!");
    }

    public static void computerWon(Player p){
        System.out.println("Sorry " + p.getName() + " you lost");
    }


    ////////////////////////////////////////////////////////////////
    public static void main(String[] args){


        //Explain the game
        System.out.println("Hello, welcome to Castle. Castle is a card game in which you get three face down cards" +
                " and six more cards of which you choose three to set aside. During your turn you have to play a card higher or equal" +
                " to the one that is one the top of the pile. The three exceptions are 2,10, and 7. You can play 2's and 10's " +
                "on anything. A 10 clears the pile, and a 2 makes the pile invisible. A seven is special in that you have to play a 7" +
                " or lower on a 7. If you cannot play a card or try to play a card and fail, you pick up the pile and the other player goes.");
        System.out.println("What is your name?");
        String name = in.nextLine().trim();
        Player player = new Player(name);
        Player cpu = new Player();
        while(true){
            Card[] startingCards = new Card[9];
            Card[] cpuStartingCards = new Card[9];
            ArrayList<Card> hand = player.getHand();
            ArrayList<Card> cpuHand = cpu.getHand();

            //initial set up
            //the initial set up involve getting three random face down cards
            //then choosing three more cards to set aside, and keeping the other three in your hand
            for(int i = 0; i < 9; i++){
                if(i < 3){
                    startingCards[i] = deck2.nextCard();
                    cpuStartingCards[i] = deck2.nextCard();
                    startingCards[i].setFace(false);
                    cpuStartingCards[i].setFace(false);
                }
                else{
                    startingCards[i] = deck2.nextCard();
                    cpuStartingCards[i] = deck2.nextCard();
                }
            }
            player.addToHand(startingCards);
            for( int i = 3; i < 6; i++){
                player.showHand();
                System.out.println("Please enter the number corresponding to where your card is in your hand" +
                        " that you would like to set as your face up card. For example " +
                        "enter a 1 if it is the first card");
                int num = player.askForVal();
                while (num <= 0 || num > hand.size() - i){
                    System.out.println("Please type a valid integer");
                    num = player.askForVal();
                }
                Card c =  player.getHand().remove(num + i - 1);
                player.getHand().add(i,c);
                player.getHand().get(i).setFace(false);
            }
            //computer set up
            for(int i = 0; i < 9; i++)
                cpuHand.add(cpuStartingCards[i]);
            int tracker = 0;
            for(int i = 3; i < 6; i++)
                for(Card c : cpuHand)
                    if(!c.isFaceDown()){
                        int num = c.getVal();
                        if(num == 10 || num == 2){
                            cpuHand.add(i, cpuHand.remove(cpuHand.indexOf(c)));
                            cpuHand.get(i).setFace(false);
                            tracker++;
                            break;
                        }
                    }
            cpu.sortt();
            while(tracker < 3){
                cpuHand.add(3 + tracker,cpuHand.remove(cpuHand.size()-1));
                cpuHand.get(3 + tracker).setFace(false);
                tracker++;
            }
            //starting complete
            //this is the actual player and computers turns
            while(cpu.getHandSize() != 0 || player.getHandSize() != 0){
                playerResult = playerTurn(player);
                while(playerResult.equals("goAgain")){
                    if(checkForWin(player,cpu)){
                        playerWon(player);
                        break;
                    }
                    playerResult = playerTurn(player);
                }
                if(checkForWin(player, cpu)){
                    playerWon(player);
                    break;
                }
                String result = computerTurn(cpu);
                while(result.equals("goAgain")){
                    if(checkForWin(cpu, player)){
                        computerWon(player);
                        break;
                    }
                    result = computerTurn(cpu);
                }
                if(checkForWin(cpu, player)){
                    computerWon(player);
                    break;
                }
            }
            System.out.println("Let's play again");
        }
    }
}


