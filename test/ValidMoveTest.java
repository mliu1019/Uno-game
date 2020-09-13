import CardPackage.Card;
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
        p2 = new Player(game);

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
    public void testReplenishCards() throws Exception {
        game.discardAll();

        assertEquals(0, game.getDrawPileSize());
        assertEquals(108, game.getDiscardPileSize());

        for (int i=0; i<5; ++i) { /* moves all cards from the draw pile to form the discard pile */
            p1.draw_card();
        }

        assertEquals(102, game.getDrawPileSize());
        assertEquals(1, game.getDiscardPileSize());
        // p1.showHand();
    }

    @Test
    public void testComplicatedGameLogic() {

    }
}
