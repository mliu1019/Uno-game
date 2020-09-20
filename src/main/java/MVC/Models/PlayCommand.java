package MVC.Models;

import CardPackage.Card;

public class PlayCommand {
    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public CMD getCommand() {
        return command;
    }

    public void setCommand(CMD command) {
        this.command = command;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Card.Color getWildColor() {
        return wildColor;
    }

    public void setWildColor(Card.Color wildColor) {
        this.wildColor = wildColor;
    }

    public enum CMD {
        PLAY("PLAY"), DRAW("DRAW"), WILD("WILD");

        private final String cmdString;

        CMD(String val) {
            cmdString = val;
        }

        @Override
        public String toString() {
            return cmdString;
        }
    }

    String     playerID;
    CMD        command;
    int        index;
    Card.Color wildColor;
}
