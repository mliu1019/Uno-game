import CardPackage.Card;
import CardPackage.NumberCard;
import CardPackage.WildCard;
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
    public void testValidMove() {
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


}
