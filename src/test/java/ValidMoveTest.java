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
        p1 = new Player(game, "-");

        game.initDeck();
    }

    /*
     * This test makes sure that the player only plays card with matching number or color.
     */
    @Test
    public void testValidMoveNumberColor() {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new NumberCard(1, Card.Color.GREEN));
        assertFalse(p1.isValidMove(0));

        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
    }


    /*
     * This test makes sure that the player only plays card with matching effect.
     */
    @Test
    public void testValidMoveEffect() {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new SkipCard(Card.Color.GREEN));
        assertFalse(p1.isValidMove(0));

        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
    }


    /*
     * This test makes sure that the player can always play the wild card.
     */
    @Test
    public void testValidMoveWild() {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new WildCard());
        assertTrue(p1.isValidMove(0));
    }


    /*
     * This test makes sure that the player only plays the wild4 card if there are no other playable cards.
     */
    @Test
    public void testValidMoveBonus() {
        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new SkipCard(Card.Color.RED));
        p1.addCardsToHand(new Wild4Card());
        assertFalse(p1.isValidMove(1));

        assertEquals(Card.Color.RED, game.getState(Game.GameState.nextColor));
    }


    /*
     * This test makes sure that the player should play out a card after drawing it if it can be played.
     */
    @Test
    public void testPlayableCards() throws Exception {
        game.discardAll();
        game.draw(new NumberCard(1, Card.Color.RED));

        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new NumberCard(1, Card.Color.GREEN));

        p1.make_turn();

        assertEquals(1, p1.deckSize());
    }


    /*
     * This test makes sure that the player should not play out a card after drawing it if it cannot be played.
     */
    @Test
    public void testUnplayableCards() throws Exception {
        game.discardAll();
        game.draw(new NumberCard(1, Card.Color.BLUE));

        game.setState(Game.GameState.nextPlayer, 0);
        game.setState(Game.GameState.nextColor, Card.Color.RED);

        p1.addCardsToHand(new NumberCard(1, Card.Color.GREEN));

        p1.make_turn();

        assertEquals(2, p1.deckSize());
    }

}
