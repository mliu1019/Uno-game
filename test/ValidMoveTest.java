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
    }

    @Test
    public void testCardLogic() {

        //TODO: Test symbols next (skip)
    }

    @Test
    public void testComplicatedGameLogic() {

        //TODO: a complicated example of valid/invalid moves over
    }
}
