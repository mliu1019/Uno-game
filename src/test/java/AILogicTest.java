import CardPackage.*;
import GamePackage.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AILogicTest {
    private Game game;
    private AI p;

    @Before
    public void setUp() {
        game = new Game();
        p = new AI(game, "-");
    }

    @Test
    public void testNumberCard() throws Exception {
        game.setState(Game.GameState.nextNumber, 2);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        game.addToDiscardPile(new NumberCard(7, Card.Color.YELLOW));

        p.addCardsToHand(new NumberCard(2, Card.Color.YELLOW));
        p.addCardsToHand(new NumberCard(2, Card.Color.BLUE));

        p.playCard(p.selectCard());

        assertEquals(Card.Color.YELLOW, game.getState(Game.GameState.nextColor));
        assertEquals(2, game.getState(Game.GameState.nextNumber));
        assertEquals(Card.Effect.NONE, game.getState(Game.GameState.nextEffect));
    }

    @Test
    public void testWildCard() throws Exception {
        game.setState(Game.GameState.nextNumber, 2);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        game.addToDiscardPile(new NumberCard(7, Card.Color.YELLOW));

        p.addCardsToHand(new WildCard());
        p.addCardsToHand(new NumberCard(2, Card.Color.YELLOW));
        p.addCardsToHand(new WildCard());

        p.playCard(p.selectCard());

        assertEquals(Card.Color.YELLOW, game.getState(Game.GameState.nextColor));
        assertEquals(2, game.getState(Game.GameState.nextNumber));
        assertEquals(Card.Effect.NONE, game.getState(Game.GameState.nextEffect));
    }

}
