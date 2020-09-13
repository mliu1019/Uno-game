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
        g.setNextState(Game.GameState.nextColor, color);
        color = Color.NONE;
    }
}
