import GamePackage.*;
import CardPackage.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class GameLogicTest {
    private Game game;
    private Player p1, p2, p3;

    @Before
    public void setUp() {
        game = new Game();
        p1 = new Player(game);
        p2 = new Player(game);
        p3 = new Player(game);

        game.initDeck();
    }

    @Test
    public void testNormalCardPlays() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextNumber, 7);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardToHand(new NumberCard(1, Card.Color.RED));
        p1.addCardToHand(new NumberCard(1, Card.Color.BLUE));
        p2.addCardToHand(new NumberCard(1, Card.Color.BLUE));
        p2.addCardToHand(new NumberCard(0, Card.Color.RED));
        p3.addCardToHand(new WildCard());
        p3.addCardToHand(new WildCard());

        p1.playCard(0);
        assertEquals(1, game.getState(Game.GameState.nextNumber));
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));

        p2.playCard(0);
        assertEquals(1, game.getState(Game.GameState.nextNumber));
        assertEquals(Card.Color.BLUE, game.getState(Game.GameState.nextColor));

        p3.declareNextColor(Card.Color.GREEN);
        p3.playCard(0);
        assertEquals(Card.Color.GREEN, game.getState(Game.GameState.nextColor));
    }

    @Test
    public void testGameTakeTurns() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardToHand(new NumberCard(1, Card.Color.RED));
        p1.addCardToHand(new NumberCard(2, Card.Color.RED));
        p2.addCardToHand(new ReverseCard(Card.Color.RED));
        p3.addCardToHand(new ReverseCard(Card.Color.RED));

        /**
         * This test checks that after playing a normal card, turn should be set to next player.
         * And test for whether Reverse Card can effectively reverse the turn.
         */
        p1.playCard(0);
        game.finishTurn();
        assertEquals(1, game.getState(Game.GameState.nextPlayer));

        p2.playCard(0);
        game.finishTurn();
        assertEquals(0, game.getState(Game.GameState.nextPlayer));

        p1.playCard(0);
        game.finishTurn();
        assertEquals(2, game.getState(Game.GameState.nextPlayer));

        p3.playCard(0);
        game.finishTurn();
        assertEquals(0, game.getState(Game.GameState.nextPlayer));
    }
}
