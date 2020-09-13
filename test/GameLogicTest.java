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
    public void testTakeTurns() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardToHand(new NumberCard(1, Card.Color.RED));
        p1.addCardToHand(new NumberCard(2, Card.Color.RED));
        p2.addCardToHand(new NumberCard(3, Card.Color.RED));

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

    /*
    @Test
    public void testSkip() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardToHand(new SkipCard(Card.Color.RED));
        p0.playCard(0);
        game.finishTurn();
        assertEquals(2, game.getState(Game.GameState.nextPlayer));

        p2.addCardToHand(new SkipCard(Card.Color.RED));
        p2.playCard(0);
        game.finishTurn();
        assertEquals(1, game.getState(Game.GameState.nextPlayer));
    }
     */

    @Test
    public void testReverse() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p0.addCardToHand(new ReverseCard(Card.Color.RED));
        p0.playCard(0);
        game.finishTurn();
        assertEquals(2, game.getState(Game.GameState.nextPlayer));

        p2.addCardToHand(new ReverseCard(Card.Color.RED));
        p2.playCard(0);
        game.finishTurn();
        assertEquals(0, game.getState(Game.GameState.nextPlayer));
    }


}
