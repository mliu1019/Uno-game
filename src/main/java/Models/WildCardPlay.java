package Models;

import CardPackage.Card;

public class WildCardPlay {
    String playerID;
    Card.Color color;
    int index;

    public WildCardPlay(String id, int i, Card.Color c) {
        playerID = id;
        index = i;
        color = c;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public Card.Color getColor() {
        return color;
    }

    public void setColor(Card.Color color) {
        this.color = color;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
