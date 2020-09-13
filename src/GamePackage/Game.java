package GamePackage;

import CardPackage.*;

import java.util.*;

public class Game {
    public enum GameState {
        turnRate, shouldEnd, shouldSkip, nextDraw, nextColor, nextNumber, nextEffect, nextPlayer
    }

    ArrayList<Player> players;
    ArrayList<Card> drawPile;
    ArrayList<Card> discardPile;

    /*
     * Creates all the possible game states.
     */
    private Map<GameState, Object> state = new HashMap<GameState, Object>() {{
        put(GameState.shouldEnd, false); /* if there's a winner and game ends */
        put(GameState.shouldSkip, false); /* if the next player misses a turn */
        put(GameState.nextDraw, 0); /* the number of cards the next player should draw */
        put(GameState.nextColor, Card.Color.NONE); /* the next color on card */
        put(GameState.nextNumber, -1); /* the next number on card */
        put(GameState.nextEffect, Card.Effect.NONE); /* the next effect on card */
        put(GameState.turnRate, 1); /* the direction for the order of play, 1 for clockwise and -1 for counterclockwise*/
        put(GameState.nextPlayer, 0); /* the next player */
    }};;

    /*
     * Initializes a new game.
     */
    public Game() {
        players = new ArrayList<>();
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
    }

    /*
     * Initializes a new deck of shuffled cards.
     */
    public void initDeck() {
        drawPile.clear();
        discardPile.clear();

        for (Card.Color c: Card.Color.values()) {
            if (c == Card.Color.NONE) { /* initializes wild cards */
                for (int i=0; i<4; ++i) {
                    drawPile.add(new WildCard());
                    drawPile.add(new Wild4Card());
                }
            } else {
                drawPile.add(new NumberCard(0, c));

                for (int count=0; count<2; ++count) { /* initializes functional cards */
                    drawPile.add(new SkipCard(c));
                    drawPile.add(new ReverseCard(c));
                    drawPile.add(new Draw2Card(c));
                    for (int i = 1; i <= 9; ++i) { /* initializes number cards */
                        drawPile.add(new NumberCard(i, c));
                    }
                }
            }
        }

        Collections.shuffle(drawPile);
    }

    /*
     * Adds players to the game.
     *
     * @param pp ArrayList of player objects to be added to the game
     */
    public void addPlayers(Player ... pp) {
        Collections.addAll(players, pp);
    }

    /*
     * Adds players to the game.
     */
    public void dealFirstHand() {
        int NUM_CARDS_PER_PLAYER = 7;

        /* deals 7 cards to each player */
        for (Player p: players) {
            for (int i=0; i<NUM_CARDS_PER_PLAYER; ++i) {
                p.draw_card();
            }
        }

        Random rand = new Random();

        /* keeps selecting a card until there is one with both number and color */
        while (true) {
            int index = rand.nextInt(drawPile.size());
            Card c = drawPile.get(index); /* randomly selects a card to set the initial state of the game */

            /* selects a card with both number and color */
            if (c.getClass().equals(NumberCard.class)) {
                setState(GameState.nextColor, c.getColor());
                setState(GameState.nextNumber, ((NumberCard) c).getNumber());

                drawPile.remove(index);
                discardPile.add(c);
                break; /* set initial state complete */
            }
        }
    }

    /*
     * Deals the card on top of the draw pile to the current player.
     */
    public Card dealCard() {
        return drawPile.remove(0);
    }

    /*
     * Starts the game and enters the game loop.
     */
    public void start() {
        dealFirstHand();

        while (state.get(GameState.shouldEnd).equals(false)) {
            try {
                turn();
            } catch (Exception e) {
                // TODO: for displaying error messages when implementing GUI
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Game has ended.");
    }

    /*
     * Makes a single turn for the current player.
     */
    public void turn() throws Exception {

        Player p = players.get((int)getState(GameState.nextPlayer));

        int toDraw = (int)getState(GameState.nextDraw);
        while (toDraw -- > 0) {
            p.draw_card();
        }

        if (getState(GameState.shouldSkip).equals(false)) {
            p.make_turn();
        } else {
            setState(GameState.shouldSkip, false);
        }

        checkEnding(p);
        checkPile();

        finishTurn();
    }

    /*
     * Checks if the current player has no cards left and game ends.
     *
     * @param p the current player object
     */
    public void checkEnding(Player p) {
        if (p.deckSize() == 0) {
            setState(GameState.shouldEnd, true);
        }
    }

    /*
     * Checks if the draw pile if empty and update the two piles.
     */
    public void checkPile() {
        if (drawPile.size() == 0) {
            Card topCard = discardPile.get(discardPile.size()-1); /* sets aside the top card in the discard pile */

            discardPile.remove(discardPile.size()-1);
            for (int i=0; i<discardPile.size(); ++i) { /* moves all cards in the discard pile to form the new draw pile */
                Card temp = discardPile.remove(0);
                drawPile.add(temp);
            }
            Collections.shuffle(drawPile);

            discardPile.add(topCard); /* uses the top card in the discard pile as the game state */
        }
    }

    /*
     * Finishes the current player's turn.
     */
    public void finishTurn() {
        advanceTurn();
    }

    private void advanceTurn() {
        setState(GameState.nextPlayer,((int)getState(GameState.nextPlayer) + (int)getState(GameState.turnRate) + players.size()) % players.size());
    }

    /*
     * Adds a card to the discard pile.
     *
     * @param c the card to be discarded
     */
    public void discard(Card c) {
        discardPile.add(c);
    }

    /*
     * Sets the state.
     */
    public void setState(GameState gs, Object val) {
        state.put(gs, val);
    }

    /*
     * Gets the state.
     */
    public Object getState(GameState gs) {
        return state.get(gs);
    }

    /*
     * Gets the number of players.
     */
    public int playerSize() {
        return players.size();
    }

    /*
     * Gets the number of cards discarded.
     */
    public int deckSize() {
        return drawPile.size();
    }
}
