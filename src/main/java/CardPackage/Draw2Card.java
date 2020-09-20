package CardPackage;

import GamePackage.Game;

public class Draw2Card extends Card {
    public Draw2Card(Color c) {
        color = c;
        effect = Effect.DRAW2;
    }


    @Override
    public void causeEffect(Game g) {
        g.setState(Game.GameState.nextDraw, 2); /* the next player draws two cards */
        // g.setState(Game.GameState.shouldSkip, true); /* the next player misses a turn */
        g.setState(Game.GameState.shouldStack, true); /* the next player misses a turn */
    }

    @Override
    public String toString() {
        return color + " DRAW2";
    }
}
