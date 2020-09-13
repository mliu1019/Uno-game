import GamePackage.*;
import CardPackage.*;

import UtilPackage.Uno;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class GameFlowTest {
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
    public void testTakeTurns() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardsToHand(new NumberCard(1, Card.Color.RED));
        p1.addCardsToHand(new NumberCard(2, Card.Color.RED));
        p2.addCardsToHand(new NumberCard(3, Card.Color.RED));

        p0.playCard(0);
        game.finishTurn();
        assertEquals(1, game.getState(Game.GameState.nextPlayer));

        p1.playCard(0);
        game.finishTurn();
        assertEquals(2, game.getState(Game.GameState.nextPlayer));

        p2.playCard(0);
        game.finishTurn();
        assertEquals(0, game.getState(Game.GameState.nextPlayer));
    }

    @Test
    public void testSkip() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardsToHand(new SkipCard(Card.Color.RED));
        p1.addCardsToHand(new NumberCard(1, Card.Color.RED));
        p2.addCardsToHand(new SkipCard(Card.Color.RED));

        p0.playCard(0);
        game.finishTurn();

        game.turn();

        assertEquals(2, game.getState(Game.GameState.nextPlayer));
        assertThrows(Uno.IllegalPlayerTurn.class, () -> p1.playCard(0));

        p2.playCard(0);
        game.finishTurn();

        game.turn();

        assertEquals(1, game.getState(Game.GameState.nextPlayer));
    }

    @Test
    public void testReverse() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardsToHand(new ReverseCard(Card.Color.RED));
        p0.playCard(0);
        game.finishTurn();
        assertEquals(2, game.getState(Game.GameState.nextPlayer));

        p2.addCardsToHand(new ReverseCard(Card.Color.RED));
        p2.playCard(0);
        game.finishTurn();
        assertEquals(0, game.getState(Game.GameState.nextPlayer));
    }

    @Test
    public void testWild() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardsToHand(new WildCard());
        p1.addCardsToHand(new NumberCard(1, Card.Color.GREEN));

        assertFalse(p2.isValidMove(0));

        p0.declareNextColor(Card.Color.GREEN);
        p0.playCard(0);
        game.finishTurn();

        assertEquals(Card.Color.GREEN, game.getState(Game.GameState.nextColor));
    }

    @Test
    public void testWild4() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardsToHand(new Wild4Card());
        p1.addCardsToHand(new NumberCard(1, Card.Color.RED));

        p0.declareNextColor(Card.Color.GREEN);
        p0.playCard(0);
        game.finishTurn();

        assertEquals(true, game.getState(Game.GameState.shouldSkip));
        assertEquals(Card.Color.GREEN, game.getState(Game.GameState.nextColor));
        assertEquals(1, game.getState(Game.GameState.nextPlayer));

        game.turn();
        assertEquals(5, p1.deckSize());
    }

}
