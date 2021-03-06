import CardPackage.Card;
import CardPackage.Draw2Card;
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
        p1 = new Player(game, "-");
        p2 = new Player(game, "-");

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
        assertEquals(110, game.getDiscardPileSize());

        for (int i=0; i<5; ++i) { /* moves all cards from the draw pile to form the discard pile */
            p1.draw_card();
        }

        assertEquals(104, game.getDrawPileSize());
        assertEquals(1, game.getDiscardPileSize());
        // p1.showHand();
    }

    /*
     * This test makes sure to end the game if a player has played all the cards.
     */
    @Test
    public void testGameEnds() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new NumberCard(1, Card.Color.RED));
        p1.playCard(0);

        assertTrue(game.checkEnding(p1));
    }


    /*
     * This test makes sure that the stacking feature works.
     */
    @Test
    public void testStacking() throws Exception {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);
        p1.draw_card();
        p2.draw_card();

        p1.addCardsToHand(new Draw2Card(Card.Color.RED));
        p1.playCard(1);

        p2.addCardsToHand(new Draw2Card(Card.Color.GREEN));
        p2.playCardDraw2(1);

        p1.addCardsToHand(new Draw2Card(Card.Color.BLUE));
        p1.playCardDraw2(1);

        p2.endTurn();

        assertEquals(0, game.getState(Game.GameState.nextPlayer));
        assertEquals(7, p2.deckSize());
    }
}
