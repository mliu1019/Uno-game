package GamePackage;

import CardPackage.*;
import UtilPackage.Uno;

import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    private String name = "default";
    private final Game g;
    private ArrayList<Card> deck;

    private Card.Color nextWantedColor = Card.Color.NONE; //By default

    public Player(Game game) {
        game.addPlayers(this);
        g = game;
        deck = new ArrayList<Card>();
    }

    public void setName(String n) {
        name = n;
    }

    public void declareNextColor(Card.Color c) {
        // preset next wanted color for test convenience.
        nextWantedColor = c;
    }

    public void draw_card() {
        Card c = g.dealCard();
        addCardToHand(c);
    }

    public void addCardToHand(Card c) {
        deck.add(c);
    }

    public void playCard(int index) throws Exception {
        if (!isValidMove(index)) {
            throw new Uno.IllegalHandException("Card " + index + " is an illegal move.");
        }
        Card c = deck.remove(index);

        if (c.isWildType()) {
            ((WildCard) c).setWildColor(nextWantedColor);
        }
        c.causeEffect(g);

        g.discard(c);

//        System.out.println("Card " + index + " is played.");
    }

    public void make_turn() {
        showHand();

        Scanner cin = new Scanner(System.in);  // Create a Scanner object

        while (true) {
            System.out.println("Card (index) to play: ");

            int index = cin.nextInt();

            try {
                playCard(index);
                break;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        }
    }

    public boolean isValidMove(int index) {
        if (!(index > -1) || !(index < deck.size())) {
            return false;
        }

        Card c = deck.get(index);

        if (c.getClass().equals(WildCard.class) || c.getClass().equals(Wild4Card.class)) return true;

        if (!c.isColor((Card.Color)g.getState(Game.GameState.nextColor))) {
            if (c.getClass().equals(UnoCard.class)) {
                return ((UnoCard) c).isNumber((int) g.getState(Game.GameState.nextNumber));
            } else {
                return c.getEffect().equals(g.getState(Game.GameState.nextEffect));
            }
        }

        return true;
    }

    /*
    Getters
     */
    public Card getCard(int index) {
        return deck.get(index);
    }

    public int deckSize() {
        return deck.size();
    }

    public void showHand() {
        System.out.println("GamePackage.Player " + name + " to play with hands:");
        for (int i=0; i<deck.size(); ++i) {
            System.out.println(i + ": " + deck.get(i));
        }
        System.out.println();
    }
}
