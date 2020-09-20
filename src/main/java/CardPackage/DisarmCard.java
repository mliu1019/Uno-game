package CardPackage;

import GamePackage.Game;

import java.awt.*;

public class DisarmCard extends WildCard {
    public DisarmCard() {
        color = Color.NONE;
        effect = Effect.DISARM;
    }


    @Override
    public void causeEffect(Game g) {
        g.setState(Game.GameState.shouldDisarm, true); /* all players should disarm wild cards */
        color = Color.NONE;
    }

    @Override
    public String toString() {
        return color + " DISARM";
    }
}
