import CardPackage.Card;
import CardPackage.*;
import GamePackage.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {
    private Game game;
    private Player p1;

    @Before
    public void setUp() {
        game = new Game();
        p1 = new Player(game);
    }

    @Test
    public void testNumberCard() throws Exception {
        game.setNextState(Game.GameState.nextNumber, 7);
        game.setNextState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardToHand(new NumberCard(2, Card.Color.RED));
        p1.addCardToHand(new NumberCard(2, Card.Color.BLUE));

        p1.playCard(0);
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
        assertEquals(2, game.getState(Game.GameState.nextNumber));
        assertEquals(Card.Effect.NONE, game.getState(Game.GameState.nextEffect));

        p1.playCard(0);
        assertEquals(Card.Color.BLUE, game.getState(Game.GameState.nextColor));
        assertEquals(2, game.getState(Game.GameState.nextNumber));
        assertEquals(Card.Effect.NONE, game.getState(Game.GameState.nextEffect));
    }

    @Test
    public void testSkipCard() throws Exception {
        game.setNextState(Game.GameState.nextNumber, 7);
        game.setNextState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardToHand(new SkipCard(Card.Color.RED));

        p1.playCard(0);
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
        assertEquals(true, game.getState(Game.GameState.shouldSkip));
    }

    @Test
    public void testReverseCard() throws Exception {
        game.setNextState(Game.GameState.nextNumber, 7);
        game.setNextState(Game.GameState.nextColor, Card.Color.RED);

        Object turnRate = game.getState(Game.GameState.turnRate);

        p1.addCardToHand(new ReverseCard(Card.Color.RED));

        p1.playCard(0);
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
        assertEquals((int)turnRate * -1, game.getState(Game.GameState.turnRate));
    }

    @Test
    public void testDraw2Card() throws Exception {
        game.setNextState(Game.GameState.nextNumber, 7);
        game.setNextState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardToHand(new Draw2Card(Card.Color.RED));

        p1.playCard(0);
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
        assertEquals(true, game.getState(Game.GameState.shouldSkip));
        assertEquals(2, game.getState(Game.GameState.nextDraw));
    }

    @Test
    public void testWildCard() throws Exception {
        game.setNextState(Game.GameState.nextNumber, 7);
        game.setNextState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardToHand(new WildCard());

        p1.playCard(0);
        assertEquals(Card.Color.NONE, game.getState(Game.GameState.nextColor));
    }

    @Test
    public void testWild4Card() throws Exception {
        game.setNextState(Game.GameState.nextNumber, 7);
        game.setNextState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardToHand(new Wild4Card());

        p1.playCard(0);
        assertEquals(Card.Color.NONE, game.getState(Game.GameState.nextColor));
        assertEquals(true, game.getState(Game.GameState.shouldSkip));
        assertEquals(4, game.getState(Game.GameState.nextDraw));
    }
}
