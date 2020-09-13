import CardPackage.*;
import GamePackage.*;
import UtilPackage.Uno;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidMoveTest {
    private Game game;
    private Player p1, p2;

    @Before
    public void setUp() {
        game = new Game();
        p1 = new Player(game);

        game.initDeck();
    }

    @Test
    public void testIllegalThrows() {
        game.dealFirstHand();

        assertThrows(Uno.IllegalHandException.class, ()-> {
            p1.playCard(-1);
        });

        assertThrows(Uno.IllegalHandException.class, ()-> {
            p1.playCard(7);
        });
        // p1.showHand();
    }

    @Test
    public void testValidMoveNumber() {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new NumberCard(1, Card.Color.GREEN));
        assertFalse(p1.isValidMove(0));

        assertThrows(Uno.IllegalHandException.class, ()-> {
            p1.playCard(0);
        });

        game.finishTurn();
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
    }

    @Test
    public void testValidMoveEffect() {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new SkipCard(Card.Color.GREEN));
        assertFalse(p1.isValidMove(0));

        assertThrows(Uno.IllegalHandException.class, ()-> {
            p1.playCard(0);
        });

        game.finishTurn();
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
    }

    @Test
    public void testValidMoveBonus() {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new SkipCard(Card.Color.RED));
        p1.addCardsToHand(new Wild4Card());
        assertFalse(p1.isValidMove(1));

        assertThrows(Uno.IllegalHandException.class, ()-> {
            p1.playCard(1);
        });

        game.finishTurn();
        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
    }

    @Test
    public void testPlayableCards() throws Exception {
        game.discardAll();
        game.draw(new NumberCard(1, Card.Color.RED));

        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new NumberCard(1, Card.Color.GREEN));

        p1.make_turn();

        game.finishTurn();
        assertEquals(1, p1.deckSize());
    }

    @Test
    public void testUnplayableCards() throws Exception {
        game.discardAll();
        game.draw(new NumberCard(1, Card.Color.BLUE));

        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new NumberCard(1, Card.Color.GREEN));

        p1.make_turn();

        game.finishTurn();
        assertEquals(2, p1.deckSize());
    }

}
