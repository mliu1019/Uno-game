package CardPackage;

import GamePackage.Game;

public class Draw2Card extends Card {
    public Draw2Card(Color c) {
        color = c;
        effect = Effect.DRAW2;
    }

    @Override
    public void causeEffect(Game g) {
        g.setNextState(Game.GameState.nextDraw, 2);
        g.setNextState(Game.GameState.shouldSkip, true);
    }
}
