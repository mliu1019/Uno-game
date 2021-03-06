package CardPackage;

import GamePackage.Game;

public class WildCard extends Card {
    public WildCard() {
        effect = Effect.WILD;
        color = Color.NONE;
    }


    public void setWildColor(Color c) {
        color = c;
    }


    @Override
    public void causeEffect(Game g) {
        g.setState(Game.GameState.nextColor, color); /* current player decides color */
        color = Color.NONE;
    }

    @Override
    public String toString() {
        return color + " WILD";
    }
}
