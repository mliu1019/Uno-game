package CardPackage;

import GamePackage.Game;

public class ReverseCard extends Card {
    public ReverseCard(Color c) {
        color = c;
        effect = Effect.REVERSE;
    }


    @Override
    public void causeEffect(Game g) {
        g.setState(Game.GameState.turnRate, (int)g.getState(Game.GameState.turnRate) * -1); /* reverse direction */
    }

    @Override
    public String toString() {
        return color + " REVERSE";
    }
}
