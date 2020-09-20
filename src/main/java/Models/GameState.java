package Models;

import CardPackage.Card;

public class GameState {
    boolean shouldStart = false;
    boolean shouldEnd = false;
    boolean shouldSkip = false;

    String nextPlayerID;
    int nextPlayer = 0;
    int nextNumber = -1;
    Card.Color nextColor = Card.Color.NONE;
    Card.Effect nextEffect = Card.Effect.NONE;

    public boolean getShouldStart() {
        return shouldStart;
    }

    public boolean getShouldEnd() {
        return shouldEnd;
    }

    public boolean getShouldSkip() {
        return shouldSkip;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    public int getNextNumber() {
        return nextNumber;
    }

    public Card.Color getNextColor() {
        return nextColor;
    }

    public Card.Effect getNextEffect() {
        return nextEffect;
    }

    public void setShouldStart(boolean shouldStart) {
        this.shouldStart = shouldStart;
    }

    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }

    public void setShouldSkip(boolean shouldSkip) {
        this.shouldSkip = shouldSkip;
    }

    public void setNextPlayer(int nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public void setNextNumber(int nextNumber) {
        this.nextNumber = nextNumber;
    }

    public void setNextColor(Card.Color nextColor) {
        this.nextColor = nextColor;
    }

    public void setNextEffect(Card.Effect nextEffect) {
        this.nextEffect = nextEffect;
    }

    public String getNextPlayerID() {
        return nextPlayerID;
    }

    public void setNextPlayerID(String nextPlayerID) {
        this.nextPlayerID = nextPlayerID;
    }
}
