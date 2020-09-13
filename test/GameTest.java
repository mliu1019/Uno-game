import GamePackage.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
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
    public void testGameSetup() {
        assertEquals(2, game.playerSize());
        assertEquals(108, game.deckSize());

        game.dealFirstHand();

        assertEquals(108 - 14 - 1, game.deckSize());
        assertEquals(7, p1.deckSize());
        assertEquals(7, p2.deckSize());
    }
}