package CardPackage;

import GamePackage.Game;

public class ReverseCard extends Card {
    public ReverseCard(Color c) {
        color = c;
        effect = Effect.REVERSE;
    }

    @Override
    public void causeEffect(Game g) {
        g.setNextState(Game.GameState.turnRate, (int)g.getState(Game.GameState.turnRate) * -1);
    }
}
