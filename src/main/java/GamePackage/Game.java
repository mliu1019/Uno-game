package GamePackage;

import CardPackage.*;
import MVC.Models.PlayCommand;
import MVC.Models.PlayFeedback;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    public enum GameState {
        players, turnRate, shouldStart, shouldEnd, shouldSkip, nextDraw, nextColor, nextNumber, nextEffect, nextPlayer, nextPlayerID
    }

    ArrayList<Player> players;
    ArrayList<Card> drawPile;
    ArrayList<Card> discardPile;

    /*
     * Creates all the possible game states.
     */
    private Map<GameState, Object> state = new HashMap<>() {{
        put(GameState.shouldEnd, false); /* if there's a winner and game ends */
        put(GameState.shouldStart, false); /* if there's a winner and game ends */
        put(GameState.shouldSkip, false); /* if the next player misses a turn */
        put(GameState.nextDraw, 0); /* the number of cards the next player should draw */
        put(GameState.nextColor, Card.Color.NONE); /* the next color on card */
        put(GameState.nextNumber, -1); /* the next number on card */
        put(GameState.nextEffect, Card.Effect.NONE); /* the next effect on card */
        put(GameState.turnRate, 1); /* the direction for the order of play, 1 for clockwise and -1 for counterclockwise*/
        put(GameState.nextPlayer, 0); /* the next player */
        put(GameState.nextPlayerID, "");
    }};

    private int maxNumPlayers = 4;

    /*
     * Initializes a new game.
     */
    public Game() {
        players = new ArrayList<>();
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
    }

    public boolean ready() {
        return players.size() == maxNumPlayers;
    }

    public void setNumPlayers(int n) {
        maxNumPlayers = n;
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

        setState(GameState.nextPlayerID, players.get(0).getPlayerID());
    }


    /*
     * Deals the card on top of the draw pile to the current player.
     */
    public Card dealCard() {
        checkPile();
        return drawPile.remove(0);
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

        if (getState(GameState.shouldSkip).equals(false)) { /* determines if player should skip turn */
            p.make_turn();
        } else {
            setState(GameState.shouldSkip, false);
        }

        checkEnding(p);

    }

    /*
     * Checks if the current player has no cards left and game ends.
     *
     * @param p the current player object
     */
    public boolean checkEnding(Player p) {
        if (p.deckSize() == 0) {
            setState(GameState.shouldEnd, true);
            // System.out.println("The winner is " + p.getName() + "!");
            return true;
        }
        return false;
    }


    /*
     * Checks if the draw pile if empty and update the two piles.
     */
    public void checkPile() {
        if (drawPile.size() == 0) {
            Card topCard = discardPile.remove(discardPile.size()-1); /* sets aside the top card in the discard pile */

            int num_cards = discardPile.size();
            for (int i=0; i<num_cards; ++i) { /* moves all cards in the discard pile to form the new draw pile */
                Card temp = discardPile.remove(0);
                drawPile.add(temp);
            }
            Collections.shuffle(drawPile);

            discardPile.add(topCard); /* uses the top card in the discard pile as the game state */
        }
    }

    /*
     * Sets the state for the next player's turn.
     */
    public void advanceTurn() {
        int nextp = ((int)getState(GameState.nextPlayer) + (int)getState(GameState.turnRate) + players.size()) % players.size();
        setState(GameState.nextPlayer, nextp);
        setState(GameState.nextPlayerID, players.get(nextp).getPlayerID());
    }

    private Player findPlayer(String pid) {
        for (Player p: players) {
            if (p.getPlayerID().equals(pid)) {
                return p;
            }
        }
        return null;
    }

    public PlayFeedback preCheckGameCondition(Player p) {
        if (p == null) {
            return new PlayFeedback(false, "Player not in game.");
        }

        if (getState(GameState.shouldStart).equals(false) || getState(GameState.shouldEnd).equals(true)) {
            return new PlayFeedback(false, "Game has not started.");
        }

        if (!getState(GameState.nextPlayerID).equals(p.getPlayerID())) {
            return new PlayFeedback(false, "Not your turn to play.");
        }

        return new PlayFeedback(true, "");
    }

    public PlayFeedback makePlay(PlayCommand cmd) {
        Player p = findPlayer(cmd.getPlayerID());
        var feedback = preCheckGameCondition(p);
        if (!feedback.isSuccess()) return feedback;

        if (getState(GameState.shouldSkip).equals(true)) {
            feedback.setSuccess(false);
            feedback.setMessage("Player turn should be skipped.");
            return feedback;
        }

        if (cmd.getCommand().equals(PlayCommand.CMD.DRAW)) {
            p.draw_card();
            feedback.setMessage("Player Draws a card.");
            return feedback;
        }

        if(cmd.getCommand().equals(PlayCommand.CMD.WILD)) {
            p.declareNextColor(cmd.getWildColor());
        }
        return p.playCard(cmd.getIndex());
    }

    public PlayFeedback endPlay(String playerID) {
        Player p = findPlayer(playerID);
        var feedback = preCheckGameCondition(p);
        if (!feedback.isSuccess()) return feedback;

        p.endTurn();

        feedback.setMessage("Player end turn.");
        return feedback;
    }

    public void makeWildColor(String pid, Card.Color c) {
        Player p = findPlayer(pid);
        if (p != null) {
            p.declareNextColor(c);
        }
    }

    /*
     * Adds a card to the draw pile.
     *
     * @param c the card to be drawn
     */
    public void draw(Card c) {
        drawPile.add(c);
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
     * Discards all cards.
     */
    public void discardAll() {
        int num_cards = drawPile.size();
        for (int i=0; i<num_cards; ++i) { /* moves all cards from the draw pile to form the discard pile */
            Card temp = drawPile.remove(0);
            discardPile.add(temp);
        }
    }


    /*
     * Returns the number of cards in the draw pile.
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }


    /*
     * Returns the number of cards in the draw pile.
     */
    public int getDiscardPileSize() {
        return discardPile.size();
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
    public int getNumPlayers() {
        return players.size();
    }

    public Map<GameState, Object> getStates() {
        return state;
    }

    /*
     * Gets the number of cards discarded.
     */
    public int deckSize() {
        return drawPile.size();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public List<String> getPlayerNames() {
        return players.stream()
                .map(object -> Objects.toString(object))
                .collect(Collectors.toList());
    }

}
