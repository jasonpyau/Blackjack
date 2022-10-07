import java.util.*;
public class Blackjack {
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Blackjack");
        double bet, money = 1000;
        String response;
        ArrayList<String> deck = new ArrayList<String>();
        ArrayList<String> playerCards = new ArrayList<String>();
        ArrayList<String> computerCards = new ArrayList<String>();
        newDeck(deck);
        
        while(true) {
            if(deck.size()<26)
                newDeck(deck);
            playerCards.clear();
            computerCards.clear();
            playerCards.add(deck.remove(draw(deck)));
            playerCards.add(deck.remove(draw(deck)));
            computerCards.add(deck.remove(draw(deck)));
            
            bet = askBet(0,money,scan);
            output(true, playerCards, computerCards);
            while(addCards(playerCards)<21) {
                response = move(bet, money, playerCards.size(), scan);
                if (response.equals("hit")) {
                    playerCards.add(deck.remove(draw(deck)));
                }
                else if(response.equals("double")) {
                    bet*=2;
                    playerCards.add(deck.remove(draw(deck)));
                    break;
                }
                else {
                    break;
                }
                output(true, playerCards, computerCards);   
            }
            computerCards.add(deck.remove(draw(deck)));
            while (addCards(computerCards)<17&&addCards(playerCards)<22) {
                computerCards.add(deck.remove(draw(deck)));
            }
            output(false, playerCards, computerCards);
            money +=winOrLose(bet, playerCards.size(), playerCards, computerCards);
            if (money==0) {
                System.out.println("You have no more money! Would you like to restart? Press <enter> to continue.");
                scan.nextLine();
                response = scan.nextLine(); 
                System.out.println("Restarted. Spend your money more wisely next time!");
                money = 1000;
            }
        }
    }
    
    // Makes new deck of 52 cards
    public static ArrayList<String> newDeck(ArrayList<String> deck) {   
        deck.clear();
        for (int a = 0; a < 4; a++) {
            for(int i = 2; i <= 10; i++) {
                deck.add(String.valueOf(i));
            }
            deck.add("J");
            deck.add("Q");
            deck.add("K");
            deck.add("A");
        }
        return new ArrayList<String>();
    }
    
    //Draws a card from deck
    public static int draw(ArrayList<String> deck) {
        return((int)(Math.random()*(deck.size()-1)));
    }
    
    //Computes sum of all cards held by Player/Computer
    public static int addCards(ArrayList<String> cards) {
        int value = 0, ACards = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).equals("K")||cards.get(i).equals("J")||cards.get(i).equals("Q")) {
                value = value + 10;
            }
            else if (cards.get(i).equals("A")) {
                value = value + 11;
                ACards++;
            }
            else {
                value = value + Integer.parseInt(cards.get(i));
            }
            if (value>21&&ACards>0) {
                value = value - 10;
                ACards--;
            }
        }
        return value;
    }
    
    //Asks user amount to bet
    public static double askBet(double bet, double money, Scanner scan) {
        System.out.println("\n\n\n\n\n\n\n\n\n\nMoney: $"+(int)money);
        System.out.print("Enter an amount to bet: $");
        try {
            bet = scan.nextInt();
        }
        catch (Exception e) {
            scan.nextLine();
            System.out.println("Invalid Input. Input an integer.");
            bet = askBet(bet,money,scan);
        }
        if (bet>0) {
            if (money>=bet) {
                return bet;
            }
            else {
                System.out.println("Not enough to bet.\n\n\n");
                bet = askBet(bet,money,scan);
            }
        }
        else {
            System.out.println("Must bet at least $1.\n\n\n");
            bet = askBet(bet,money,scan);
        }
        return bet;
    }
    
    //Processes user's decision 
    public static String move(double bet, double money, int moves, Scanner scan) {
            String r, response = scan.next().toLowerCase();  
            if (response.contains("hit")||response.equals("h")) 
                r = "hit";
            else if (response.contains("stand")||response.equals("s"))
                r = "stand";
            else if ((response.contains("double")||response.equals("d"))&&moves==2) {
                if (money>=(bet*2)) 
                    r = "double";
                else {
                    System.out.println("Not enough to bet.");
                    r = move(bet, money, moves, scan);
                }
            }
            else {
                if (moves==2) {
                    System.out.println("Invalid input. Hit, Stand, or Double?");
                    r = move(bet, money, moves, scan);
                }
                else {
                    System.out.println("Invalid input. Hit or Stand?");
                    r = move(bet, money, moves, scan);
                }
            }
            return r; 
        }
        
    //Output Value of Player & Computer
    public static void output(boolean hitOrStand, ArrayList<String> playerCards, ArrayList<String> computerCards) {
        System.out.println("\n\nYour Hand: "+playerCards);
        System.out.println("Value: "+addCards(playerCards));
        System.out.println("\nDealer Hand: "+computerCards);
        System.out.println("Value: "+addCards(computerCards));
        if (hitOrStand) {
            if (playerCards.size()==2) 
                System.out.println("\nHit, Stand, or Double? "); 
            else
                System.out.println("\nHit or Stand? "); 
        }
    }
    
    //Decides winner & changes money accordingly
    public static double winOrLose(double bet, int moves, ArrayList<String> playerCards, ArrayList<String> computerCards) {
        int playerValue = addCards(playerCards), computerValue = addCards(computerCards);
        if(playerValue>21) {
            System.out.println("\n\nBust, you lose $"+(int)bet+".");
            return 0-bet;
        }
        else if(playerValue==computerValue) {
            System.out.println("\n\nPush, you won $0.");
            return 0;
        }
        else if (playerValue==21) {
            if(moves==2) {
                System.out.println("\n\nBlackjack! You won $"+(int)(bet*1.5)+".");
                return bet*1.5;
            }
            else {
                System.out.println("\n\nYou won $"+(int)bet+".");
                return bet;
            }
        }
        else if(computerValue>21) {
            System.out.println("\n\nYou won $"+(int)bet+".");
            return bet;
        }
        else if(playerValue>computerValue) {
            System.out.println("\n\nYou won $"+(int)bet+".");
            return bet;
        }
        else {
            System.out.println("\n\nYou lose $"+(int)bet+".");
            return 0-bet;
        }
    }
}
