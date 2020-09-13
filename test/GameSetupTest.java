import GamePackage.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameSetupTest {
    private Game game;
    private Player p1, p2;

    int NUM_PLAYERS = 2;
    int NUM_CARDS_TOTAL = 108;
    int NUM_CARDS_PER_PLAYER = 7;
    int FIRST_HAND = 1;

    @Before
    public void setUp() {
        game = new Game();
        p1 = new Player(game);
        p2 = new Player(game);

        game.initDeck();
    }

    /*
     * This test makes sure that the deck and the players' hands are properly set up.
     */
    @Test
    public void testGameSetup() {
        assertEquals(NUM_PLAYERS, game.playerSize());
        assertEquals(NUM_CARDS_TOTAL, game.deckSize());

        game.dealFirstHand();

        assertEquals(NUM_CARDS_TOTAL - NUM_PLAYERS*NUM_CARDS_PER_PLAYER - FIRST_HAND, game.deckSize());
        assertEquals(NUM_CARDS_PER_PLAYER, p1.deckSize());
        assertEquals(NUM_CARDS_PER_PLAYER, p2.deckSize());
    }

}