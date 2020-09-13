package GamePackage;

import CardPackage.*;
import UtilPackage.Uno;
import UtilPackage.Uno.*;
import java.util.*;

public class Game {
    public enum GameState {
        turnRate, shouldEnd, shouldSkip, nextDraw, nextColor, nextNumber, nextEffect, nextPlayer
    }

    ArrayList<Player> players;
    ArrayList<Card> drawPile;
    ArrayList<Card> discardPile;

    private Map<GameState, Object> state = new HashMap<GameState, Object>() {{
        put(GameState.shouldEnd, false);
        put(GameState.shouldSkip, false);
        put(GameState.nextDraw, 0);
        put(GameState.nextColor, Card.Color.NONE);
        put(GameState.nextNumber, -1);
        put(GameState.nextEffect, Card.Effect.NONE);
        put(GameState.turnRate, 1);
        put(GameState.nextPlayer, 0);
    }};;

    public Game() {
        players = new ArrayList<>();
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
    }

    public void initDeck() {
        drawPile.clear();
        discardPile.clear();

        for (Card.Color c: Card.Color.values()) {
            if (c == Card.Color.NONE) {
                for (int i=0; i<4; ++i) {
                    drawPile.add(new WildCard());
                    drawPile.add(new Wild4Card());
                }
            } else {
                drawPile.add(new NumberCard(0, c));

                for (int count=0; count<2; ++count) {
                    drawPile.add(new SkipCard(c));
                    drawPile.add(new ReverseCard(c));
                    drawPile.add(new Draw2Card(c));
                    for (int i = 1; i <= 9; ++i) {
                        drawPile.add(new NumberCard(i, c));
                    }
                }
            }
        }
        Collections.shuffle(drawPile);
    }

    public void addPlayers(Player ... pp) {
        Collections.addAll(players, pp);
    }

    public void dealFirstHand() {
        int NUM_CARDS_PER_PLAYER = 7;

        for (Player p: players) {
            for (int i=0; i<NUM_CARDS_PER_PLAYER; ++i) p.draw_card();
        }

        Random rand = new Random();
        while (true) {
            int index = rand.nextInt(drawPile.size());
            Card c = drawPile.get(index);

            if (c.getClass().equals(NumberCard.class)) {
                setNextState(GameState.nextColor, c.getColor());
                setNextState(GameState.nextNumber, ((NumberCard) c).getNumber());

                drawPile.remove(index);
                discardPile.add(c);
                break;
            }
        }
    }

    public Card dealCard() {
        return drawPile.remove(0);
    }

    /*
    Game Logics
     */
    public void start() {
        dealFirstHand();
        while (state.get(GameState.shouldEnd).equals(false)) {
            try {
                turn();
            } catch (Exception e) {
                // Meant to display error message when using with GUI
                System.out.println(e.getMessage());
            }
        }
    }

    public void turn() throws Exception {
        if (getState(GameState.shouldEnd).equals(true)) {
            throw new Uno.InvalidTurnAfterGameEnd("Game has ended.");
        }
        Player p = players.get((int)getState(GameState.nextPlayer));

        int cardToDraw = (int)getState(GameState.nextDraw);
        while (cardToDraw -- > 0) {
            p.draw_card();
            checkPile();
        }

        if (!getState(GameState.shouldSkip).equals(true)) {
            p.make_turn();
            checkEnding(p);
            checkPile();
        }

        finishTurn();
    }

    public void checkEnding(Player p) {
        if (p.deckSize() == 0) {
            setNextState(GameState.shouldEnd, true);
        }
    }

    public void checkPile() {
        if (drawPile.size() == 0) {
            Card topCard = discardPile.get(discardPile.size()-1);
            discardPile.remove(discardPile.size()-1);
            for (int i=0; i<discardPile.size(); ++i) {
                Card temp = discardPile.remove(0);
                drawPile.add(temp);
            }
            Collections.shuffle(drawPile);
            discardPile.add(topCard);
        }
    }

    public void finishTurn() {
        //some actions are only effective in one turn
        setNextState(GameState.nextDraw, 0);
        setNextState(GameState.shouldSkip, false);
        setNextState(GameState.nextPlayer,
                ((int)getState(GameState.nextPlayer) + (int)getState(GameState.turnRate) + players.size()) % players.size());
    }

    public void discard(Card c) {
        discardPile.add(c);
    }

    /*
    Setters
     */
    public void setNextState(GameState gs, Object val) {
        state.put(gs, val);
    }

    public Object getState(GameState gs) {
        return state.get(gs);
    }

    /*
    Getters
     */
    public int playerSize() {
        return players.size();
    }

    public int deckSize() {
        return drawPile.size();
    }
}
