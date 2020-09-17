package CardPackage;

import GamePackage.Game;

public class Wild4Card extends WildCard {
    public Wild4Card() {
        effect = Effect.WILD4;
        color = Color.NONE;
    }


    @Override
    public void causeEffect(Game g) {
        g.setState(Game.GameState.shouldSkip, true); /* next player misses a turn */
        g.setState(Game.GameState.nextDraw, 4); /* next player draws 4 cards */
        g.setState(Game.GameState.nextColor, color); /* current player decides color */
        color = Color.NONE;
    }

    @Override
    public String toString() {
        return color + " WILD4";
    }
}
