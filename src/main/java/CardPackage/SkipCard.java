package CardPackage;

import GamePackage.Game;

public class SkipCard extends Card {
    public SkipCard(Color c) {
        color = c;
        effect = Effect.SKIP;
    }


    @Override
    public void causeEffect(Game g) {
        g.setState(Game.GameState.shouldSkip, true); /* next player misses a turn */
    }

    @Override
    public String toString() {
        return color + " SKIP";
    }
}
