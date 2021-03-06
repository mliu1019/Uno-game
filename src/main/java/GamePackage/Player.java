package GamePackage;

import CardPackage.*;
import MVC.Models.PlayFeedback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
    protected String name = "default";
    protected Game g;
    protected ArrayList<Card> deck;
    protected int playerIdx;
    protected String playerID;

    private Card.Color nextColor = Card.Color.NONE; // By default

    private Card lastPlayed = null;

    public Player(Game game, String id) {
        g = game;
        playerIdx = game.getNumPlayers();
        playerID = id;
        game.addPlayers(this);
        deck = new ArrayList<>();
    }

    public void endTurn() {
        if (deck.size() == 0) {
            g.endGame(playerID);
            return;
        }
        int toDraw = (int) g.getState(Game.GameState.nextDraw);
        while (toDraw --> 0) draw_card();

        g.setState(Game.GameState.shouldDisarm, false);
        g.setState(Game.GameState.shouldSkip, false);
        g.setState(Game.GameState.nextDraw, 0);

        if (lastPlayed != null) {
            lastPlayed.causeEffect(g);
        }

        g.advanceTurn();
    }

    public void endStackTurn() {
        int toDraw = (int) g.getState(Game.GameState.nextDraw);
        toDraw += 2;
        g.setState(Game.GameState.nextDraw, toDraw);

        if (lastPlayed != null) {
            g.setState(Game.GameState.shouldStack, true);
        }

        g.advanceTurn();
    }

    public void setName(String s) {
        name = s;
    }


    /**
     * Lets the current player declare the color they want to be played next.
     */
    public void declareNextColor(Card.Color c) {
            nextColor = c;
    }


    /**
     * Draws a card for the current player.
     */
    public void draw_card() {
        Card c = g.dealCard();
        addCardsToHand(c);
    }


    /**
     * Adds a card to the current player's hand.
     *
     * @param c the array of cards to be added
     */
    public void addCardsToHand(Card ...c) {
        deck.addAll(Arrays.asList(c));
    }


    /**
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

        lastPlayed = deck.remove(index); /* removes card from player's hand */

        if (lastPlayed.isWildType()) { /* allows player to declare the next color */
            ((WildCard) lastPlayed).setWildColor(nextColor);
        }

        g.discard(lastPlayed); /* adds card to discard pile */

        endTurn();

        return new PlayFeedback(true, "Card " + lastPlayed + " is played.");
    }


    /**
     * Plays a Draw2 card from the current player.
     *
     * @param index the index of the card the current player wishes to play
     */
    public PlayFeedback playCardDraw2(int index)  {
        if (!g.getState(Game.GameState.nextPlayer).equals(playerIdx)) {
            return new PlayFeedback(false, "Not your turn.");
        }

        if (!isValidMoveDraw2(index)) { /* determines if the card is playable */
            return new PlayFeedback(false, "Draw2 Move is illegal.");
        }

        lastPlayed = deck.remove(index); /* removes card from player's hand */

        g.discard(lastPlayed); /* adds card to discard pile */

        endStackTurn();

        return new PlayFeedback(true, "Card " + lastPlayed + " is played.");
    }


    /**
     * Discards a card at index for testing purposes.
     *
     * @param index the index of the card to be discarded.
     */
    public void discardCard(int index) {
        lastPlayed = deck.remove(index); /* removes card from player's hand */
        g.discard(lastPlayed); /* adds card to discard pile */
        return;
    }


    /**
     * Sets a turn for the current player.
     */
    public void makeTurn() throws Exception {
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


    /**
     * Sets a stack turn for the current player.
     */
    public void makeStackTurn() throws Exception {
        showHand();

        if (!hasValidMovesDraw2(deck)) { /* if there are no available moves, automatically draw the stacked number*/
            int toDraw = (int)g.getState(Game.GameState.nextDraw);
            while (toDraw -- > 0) {
                draw_card();
            }
            return;
        }

        Scanner cin = new Scanner(System.in);

        System.out.println("Would you like to play a Wild2 card?");
        boolean wantPlay = cin.nextBoolean(); /* if there are available moves, lets the user decide to play or not to play */

        if (!wantPlay) { /* if player does not want to play, draws a card and play if playable */
            int toDraw = (int)g.getState(Game.GameState.nextDraw);
            while (toDraw -- > 0) {
                draw_card();
            }
            return;
        }

        while (true) { /* if player wants to play, demands for the card to be played */
            System.out.println("Card (index) to play: ");

            int index = cin.nextInt();

            try {
                playCardDraw2(index);
                break;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        }

        return;
    }


    /**
     * Disarm wild cards for the player.
     */
    public void disarmCard() {

        for (int i=deck.size()-1; i>=0; --i) {
            Card curr = deck.get(i);
            if (curr.getClass().equals(WildCard.class) || curr.getClass().equals(Wild4Card.class)) {
                discardCard(i);
            }
        }

        return;
    }


    /**
     * Determines if the current player has any valid moves.
     *
     * @param deck the hands of the player
     */
    public boolean hasValidMoves(ArrayList<Card> deck) {

        for (int i=0; i<deck.size(); ++i) {
            if (isValidMove(i)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Determines if the current player has any valid moves to stack.
     *
     * @param deck the hands of the player
     */
    public boolean hasValidMovesDraw2(ArrayList<Card> deck) {

        for (int i=0; i<deck.size(); ++i) {
            if (isValidMoveDraw2(i)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Determines if the move attempted by the current player is valid.
     *
     * @param index the index of the card to be determined
     */
    public boolean isValidMove(int index) {
        if (!(index > -1) || !(index < deck.size())) {
            return false;
        }

        Card c = deck.get(index);

        if (c.getClass().equals(WildCard.class) || c.getClass().equals(DisarmCard.class)) { /* wild cards can always be played */
            return true;
        }

        /* Bonus: "Wild Draw Four??? card can only be played if the player has no cards matching the current color */
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


    /**
     * Determines if the stacking move attempted by the current player is valid.
     *
     * @param index the index of the card to be determined
     */
    public boolean isValidMoveDraw2(int index) {
        if (!(index > -1) || !(index < deck.size())) {
            return false;
        }

        Card c = deck.get(index);

        if (c.getClass().equals(Draw2Card.class)) { /* only draw2 cards can be played */
            return true;
        }

        return false;
    }


    /**
     * Gets the number of cards in hand.
     */
    public int deckSize() {
        return deck.size();
    }


    /**
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
        for (int i=0; i<deck.size(); ++i) {
            if (isValidMove(i)) {
                deck.get(i).playable = true;
            } else {
                deck.get(i).playable = false;
            }
        }
        return deck;
    }

    public void setPlayerCards(ArrayList<Card> cards) {
        deck = cards;
    }

    @Override
    public String toString() {
        return playerID;
    }
}
