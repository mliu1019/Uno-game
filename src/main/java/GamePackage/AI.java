package GamePackage;

import CardPackage.*;

import java.util.ArrayList;
import java.util.Hashtable;


public class AI extends Player {
    public AI(Game game, String id) {
        super(game, id);
    }

    /**
     * Sets a turn for the current AI.
     */
    @Override
    public void makeTurn() throws Exception {
        showHand();

        if (!hasValidMoves(deck)) { /* if there are no available moves, automatically draws a card*/
            draw_card();
            try {
                playCard(deck.size()-1); /* card must be played if playable */
            } catch (Exception e) {
                System.out.println("Card drawn cannot be played");
            }
            return;
        }

        int index = selectCard();
        playCard(index);

        }


    /**
     * Sets a stack turn for the AI.
     */
    @Override
    public void makeStackTurn() throws Exception {
        showHand();

        if (!hasValidMovesDraw2(deck)) { /* if there are no available moves, automatically draw the stacked number*/
            int toDraw = (int)g.getState(Game.GameState.nextDraw);
            while (toDraw -- > 0) {
                draw_card();
            }
            return;
        }

        int index = selectCard();
        playCard(index);
    }


    /**
     * Lets the AI decide which card to play
     * @return the index of the card AI decides to play
     */
    public int selectCard() {

        ArrayList<Integer> playableCards = new ArrayList<>();;
        for (int i=0; i<deck.size(); ++i) {
            if (isValidMove(i)) {
                playableCards.add(i);
            }
        }

        if (playableCards.size() == 1) {
            return playableCards.get(0);
        }

        ArrayList<Card> discardPile = g.getDiscardPile();

        Hashtable<String, Integer> my_dict = new Hashtable<String, Integer>();

        int redCount = 0;
        int yellowCount = 0;
        int greenCount = 0;
        int blueCount = 0;

        for (Card c: discardPile) {
            if (c.getColor() == Card.Color.RED) {
                redCount++;
            } else if (c.getColor() == Card.Color.YELLOW) {
                yellowCount++;
            } else if (c.getColor() == Card.Color.GREEN) {
                greenCount++;
            } else if (c.getColor() == Card.Color.BLUE) {
                blueCount++;
            }
        }

        int indexToPlay = -1;
        int maxWeight = 0;

        for (int i=0; i<playableCards.size(); ++i) {
            Card.Color currColor =  deck.get(playableCards.get(i)).getColor();
            if (currColor == Card.Color.RED && redCount > maxWeight) {
                maxWeight = redCount;
                indexToPlay = playableCards.get(i);
            } else if (currColor == Card.Color.YELLOW && yellowCount > maxWeight) {
                maxWeight = yellowCount;
                indexToPlay = playableCards.get(i);
            } else if (currColor == Card.Color.GREEN && greenCount > maxWeight) {
                maxWeight = greenCount;
                indexToPlay = playableCards.get(i);
            } else if (currColor == Card.Color.BLUE && blueCount > maxWeight) {
                maxWeight = blueCount;
                indexToPlay = playableCards.get(i);
            }
        }

        return indexToPlay == -1 ? playableCards.get(0) : indexToPlay;
    }

}

