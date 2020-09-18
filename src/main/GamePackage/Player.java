package GamePackage;

import CardPackage.*;
import Models.PlayFeedback;
import UtilPackage.Uno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
    private String name = "default";
    private Game g;
    private ArrayList<Card> deck;
    private int playerIdx;
    private String playerID;

    private Card.Color nextColor = Card.Color.NONE; // By default

    public Player(Game game, String id) {
        g = game;
        playerIdx = game.getNumPlayers();
        playerID = id;
        game.addPlayers(this);
        deck = new ArrayList<>();

    }

    public void setName(String s) {
        name = s;
    }

    /*
     * Lets the current player declare the color they want to be played next.
     */
    public void declareNextColor(Card.Color c) {
        nextColor = c;
    }


    /*
     * Draws a card for the current player.
     */
    public void draw_card() {
        Card c = g.dealCard();
        addCardsToHand(c);
    }


    /*
     * Adds a card to the current player's hand.
     */
    public void addCardsToHand(Card ...c) {
        deck.addAll(Arrays.asList(c));
    }


    /*
     * Plays a card from the current player.
     *
     * @param index the index of the card the current player wishes to play
     */
    public PlayFeedback playCard(int index)  {
        if (!g.getState(Game.GameState.nextPlayer).equals(playerIdx)) {
            return new PlayFeedback(false, "Not your turn.");
        }

        if (!isValidMove(index)) { /* determines if the card is playable */
            return new PlayFeedback(false, "Move is illegal.");
        }

        Card c = deck.remove(index); /* removes card from player's hand */

        if (c.isWildType()) { /* allows player to declare the next color */
            ((WildCard) c).setWildColor(nextColor);
        }
        c.causeEffect(g);

        g.discard(c); /* adds card to discard pile */

        return new PlayFeedback(true, "Card " + c + " is played.");
    }


    /*
     * Sets a turn for the current player.
     */
    public void make_turn() throws Exception {
        showHand();

        if (!hasValidMoves(deck)) { /* if there are no available moves, automatically draws a card*/
            draw_card();
            try {
                playCard(deck.size()-1); /* card must be played if playable */
            } catch (Exception e) {
                System.out.println("Card drawn cannot be played");
            }
            return;
        }

        Scanner cin = new Scanner(System.in);

        System.out.println("Would you like to play a card?");
        boolean wantPlay = cin.nextBoolean(); /* if there are available moves, lets the user decide to play or not to play */

        if (!wantPlay) { /* if player does not want to play, draws a card and play if playable */
            draw_card();
            playCard(deck.size()-1);
            return;
        }

        while (true) { /* if player wants to play, demands for the card to be played */
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


    /*
     * Determines if the current player has any valid moves.
     */
    public boolean hasValidMoves(ArrayList<Card> deck) {

        for (int i=0; i<deck.size(); ++i) {
            if (isValidMove(i)) {
                return true;
            }
        }

        return false;
    }


    /*
     * Determines if the move attempted by the current player is valid.
     */
    public boolean isValidMove(int index) {
        if (!(index > -1) || !(index < deck.size())) {
            return false;
        }

        Card c = deck.get(index);

        if (c.getClass().equals(WildCard.class)) { /* wild cards can always be played */
            return true;
        }

        /* Bonus: "Wild Draw Four” card can only be played if the player has no cards matching the current color */
        if (c.getClass().equals(Wild4Card.class)) {
            for (int i=0; i<deck.size(); ++i) {
                if (deck.get(i).getColor() == (Card.Color)g.getState(Game.GameState.nextColor)) {
                    return false;
                }
            }
            return true;
        }

        if (!c.isColor((Card.Color)g.getState(Game.GameState.nextColor))) { /* determines if colors match */
            if (c.getClass().equals(NumberCard.class)) { /* determines if numbers match */
                return ((NumberCard) c).isNumber((int) g.getState(Game.GameState.nextNumber));
            } else { /* determines if effects match */
                return c.getEffect().equals(g.getState(Game.GameState.nextEffect));
            }
        }

        /* either colors, numbers, or effects match */
        return true;
    }

    /*
     * Gets the number of cards in hand.
     */
    public int deckSize() {
        return deck.size();
    }


    /*
     * Displays cards in hand to the current player.
     */
    public void showHand() {
        System.out.println("Player " + name + "'s current hand:");

        for (int i=0; i<deck.size(); ++i) {

            Card curr = deck.get(i);
            String cardFunction;

            if (curr.getClass().equals(NumberCard.class)) {
                cardFunction = Integer.toString(((NumberCard)curr).getNumber());
            } else {
                cardFunction = curr.getEffect().toString();
            }

            System.out.println(i + ": " + deck.get(i).getColor() + " " + cardFunction);
        }
        System.out.println();
    }


    public int getId() { return playerIdx; }

    public String getName() { return name; }

    public String getPlayerID() { return playerID; }

    public ArrayList<Card> getPlayerCards() {
        return deck;
    }

    public void setPlayerCards(ArrayList<Card> cards) {
        deck = cards;
    }
}
