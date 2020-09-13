import CardPackage.Card;
import CardPackage.NumberCard;
import GamePackage.Game;
import GamePackage.Player;
import UtilPackage.Uno;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogicTest {
    private Game game;
    private Player p1, p2;

    @Before
    public void setUp() {
        game = new Game();
        p1 = new Player(game);
        p2 = new Player(game);

        game.initDeck();
    }

    /*
     * This test makes sure that if draw pile is empty, the top card in the discard pile is set aside and the rest of
     * the discard pile is shuffled to create a new deck.
     */
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
    public void testGameEnds() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new NumberCard(1, Card.Color.RED));
        p1.playCard(0);

        game.finishTurn();
        assertEquals(true, game.checkEnding(p1));
    }
}
