package CardPackage;

import GamePackage.Game;

public class Wild4Card extends WildCard {
    public Wild4Card() {
        effect = Effect.WILD4;
        color = Color.NONE;
    }

    @Override
    public void causeEffect(Game g) {
        g.setNextState(Game.GameState.shouldSkip, true);
        g.setNextState(Game.GameState.nextDraw, 4);
        g.setNextState(Game.GameState.nextColor, color);
        color = Color.NONE;
    }
}
