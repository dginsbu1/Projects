package BlackJack;

import java.util.Scanner;
import java.util.ArrayList;

public class House{
    static String decision, name;
    static Scanner in = new Scanner(System.in);
    static double pot, tempMoney;

    static Deck deck = new Deck();
    // pl = player; d = dealer; ch = Cardholder (dealer or player);

    //This asks the player for an input and checks to make sure it is a valid double. If it is it will return the double
    //Otherwise it will continue to prompt the user to type in a valid number
    public static double validInput(){
        double money = 0;
        while(in.hasNextDouble() == false){
            System.out.println("Please type a valid number");
            in.nextLine();
        }
        money = in.nextDouble();
        money = (int)(money*100) / 100.0;
        in.nextLine();
        return money;
    }

    //This asks the player what he/she would like to do and assigns String decision to what he/she inputs.
    public static void ask(Player pl){
        System.out.println(pl.getName() + ", would you like to hit or stay?");
        decision = in.nextLine().trim();
    }

    //Doubling down consists of doubling the bid, (if possible), and giving the player one card.
    public static void doubleDown(CardHolder pl){
        //this is called doubling for less. It occurs when the player doesn't have enough money to double down
        //It simply takes whatever the user has and adds it to the pot.
        if(pl.getMon() < pot){
            pot += pl.getMon();
            pl.setMon(0);
            hit(pl);
            pl.showHand();
        }
        //this is regular.
        else{
            pl.subtractMon(pot);
            pot += pot;
            hit(pl);
            pl.showHand();
        }
    }

    //This gives the player another card and shows the player's hand.
    public static void hit(CardHolder ch){
        ch.addToHand(deck.takeAnotherCard());
    }

    //This checks to see if the player's hand's value is less than 22, if the players hand exceeds 21
    //it sets the pot to 0, tells the player that his/her hand is to high, and
    // returns false. Otherwise it returns true.
    public static boolean under22(Player pl){
        if(pl.getSum() > 21){
            System.out.println("Sorry, you have more than 21");
            return false;
        }
        else
            return true;
    }

    // This uses the ask() method to ask the player what he/she would like to do (hit or stay). It then does it.
    //Once the player has decided to stay (assuming they havn't busted, it call the dealersTurn() method.
    public static void playersTurn(Player pl, CardHolder d){
        while(true){
            ask(pl);
            if(decision.equalsIgnoreCase("hit") || decision.equalsIgnoreCase("h")){
                hit(pl);
                pl.showHand();
                if(under22(pl) == false)
                    break;
            }
            else if(decision.equalsIgnoreCase("stay") || decision.equalsIgnoreCase("s")){
                dealersTurn(d, pl);
                break;
            }
        }
    }

    //Once the player has finished the dealer goes. He continues to hit until he reaches 17 or higher.
    //The dealer's hand is printed after the dealer draws each card
    public static void dealersTurn(CardHolder d, Player pl){
        while (d.getSum() < 17){
            hit(d);
            //this checks to see if the dealer has a Black Jack.
            //If he does, he automatically wins, assuming the player didn't already get one.
            if(d.getSum() == 21 && d.getHand().size() == 2){
                System.out.println("Sorry, " + pl.getName() + ", the dealer has a Black Jack. You lose");
                return;
            }
            System.out.println( "The dealer has " + d.getSum());
            if(d.getSum() > 21){
                System.out.println("The dealer busted, you won $" + pot);
                pl.addMon(pot + pot);
                return;
            }
        }
        System.out.println(compareHands(d, pl));
    }

    //Once both the player and the dealer have gone,
    //there respective hands are compared to decide who has the better hand.
    public static String compareHands(CardHolder d, Player pl){
        int sumOfDealer;
        int sumOfPlayer;
        sumOfDealer = d.getSum();
        sumOfPlayer = pl.getSum();

        if(sumOfDealer == sumOfPlayer){
            pl.addMon(pot);
            return (pl.getName() + " you tied the dealer.");
        }

        if(sumOfDealer > sumOfPlayer)
            return ("Sorry, " +  pl.getName() + " you lost");

        if(sumOfDealer < sumOfPlayer){
            pl.addMon(pot + pot);
            return ("Congratulations " + pl.getName() + " you won $" + pot);
        }
        return "";
    }

    //This is the actual main method
    public static void main(String[] args){
        //First, the player is greeted and asked what this/her name is and how much money he/she wants
        // and makes a Player player and CardJolder dealer.
        System.out.println("Hello, welcome to the wonderful world of Black Jack \nWhat is your name?");
        name = in.nextLine().trim();
        System.out.println("How much money would you like?");
        tempMoney = validInput();
        while(tempMoney < 0){
            System.out.println("Please type a valid number");
            tempMoney = validInput();
        }
        Player player = new Player( tempMoney, name);
        CardHolder dealer = new CardHolder();
        System.out.println("Just so you know, \nyou may type in the first letter of a response instead of the entire word ex: y instead of yes");
        while(true){
            while(player.getMon() < 1){
                System.out.println("Looks like you could use a loan from the bank. How much do you want?");
                player.addMon(validInput());
            }
            System.out.println("You have $" + player.getMon() + " \n" + player.getName() + ", how much would you like to bid?");
            pot = validInput();
            while( pot > player.getMon() || pot < 0){
                System.out.println("Sorry " + player.getName() + ". You can't bid that. please try again");
                pot = validInput();
            }
            System.out.println("You have bet $" + pot);
            player.subtractMon(pot);
            //the dealer only starts with one card so that you don't end up
            //seeing both of the dealers cards before you go.
            hit(dealer);
            hit(player);
            hit(player);
            while(true){
                player.showHand();
                //Checking for possible Black Jacks
                if(player.getSum() == 21){
                    System.out.println("You have a Black Jack");
                    hit(dealer);
                    if(dealer.getSum() == 21){
                        System.out.println("but the dealer has a Black Jack too, therefore you tied the dealer");
                        break;
                    }
                    else{
                        System.out.println("You won $" + 1.5*pot);
                        player.addMon(2.5*pot);
                        break;
                    }
                }
                //assuming no Black Jack, asks the player what he would like to do.
                dealer.showHand();
                System.out.println("Would you like to double down?");
                decision = in.nextLine().trim();
                if(decision.equalsIgnoreCase("yes") || decision.equalsIgnoreCase("y")){
                    doubleDown(player);
                    if(under22(player) == true)
                        dealersTurn(dealer, player);
                    break;
                }
                if(decision.equalsIgnoreCase("no") || decision.equalsIgnoreCase("n")){
                    playersTurn(player, dealer);
                    break;
                }
            }
            //Once the hand is over, the pot, deck and the hands of both the player and the dealer are reset.
            pot = 0;
            dealer.resetHand();
            player.resetHand();
            deck = new Deck();
        }
    }
}




