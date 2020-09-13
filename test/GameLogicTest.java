import GamePackage.*;
import CardPackage.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class GameLogicTest {
    private Game game;
    private Player p0, p1, p2;

    @Before
    public void setUp() {
        game = new Game();
        p0 = new Player(game);
        p1 = new Player(game);
        p2 = new Player(game);

        game.initDeck();
    }

    @Test
    public void testNormalCardPlays() throws Exception {
        game.setNextState(Game.GameState.nextPlayer, 0);
        game.setNextState(Game.GameState.nextNumber, 7);
        game.setNextState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardToHand(new UnoCard(1, Card.Color.RED));
        p0.addCardToHand(new UnoCard(1, Card.Color.BLUE));
        p1.addCardToHand(new UnoCard(1, Card.Color.BLUE));
        p1.addCardToHand(new UnoCard(0, Card.Color.RED));
        p2.addCardToHand(new WildCard());
        p2.addCardToHand(new WildCard());

        p0.playCard(0);
        assertEquals(1, game.getState(Game.GameState.nextNumber));
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));

        p1.playCard(0);
        assertEquals(1, game.getState(Game.GameState.nextNumber));
        assertEquals(Card.Color.BLUE, game.getState(Game.GameState.nextColor));

        p2.declareNextColor(Card.Color.GREEN);
        p2.playCard(0);
        assertEquals(Card.Color.GREEN, game.getState(Game.GameState.nextColor));
    }

    @Test
    public void testGameTakeTurns() throws Exception {
        game.setNextState(Game.GameState.nextPlayer, 0);
        game.setNextState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardToHand(new UnoCard(1, Card.Color.RED));
        p0.addCardToHand(new UnoCard(2, Card.Color.RED));
        p1.addCardToHand(new ReverseCard(Card.Color.RED));
        p2.addCardToHand(new ReverseCard(Card.Color.RED));

        /**
         * This test checks that after playing a normal card, turn should be set to next player.
         * And test for whether Reverse Card can effectively reverse the turn.
         */
        p0.playCard(0);
        game.finishTurn();
        assertEquals(1, game.getState(Game.GameState.nextPlayer));

        p1.playCard(0);
        game.finishTurn();
        assertEquals(0, game.getState(Game.GameState.nextPlayer));

        p0.playCard(0);
        game.finishTurn();
        assertEquals(2, game.getState(Game.GameState.nextPlayer));

        p2.playCard(0);
        game.finishTurn();
        assertEquals(0, game.getState(Game.GameState.nextPlayer));
    }
}
