package CardPackage;

import GamePackage.Game;

public class UnoCard extends Card{
    private int number;
    public UnoCard(int n, Color c) {
        color = c;
        number = n;
        effect = Effect.NONE;
    }

    @Override
    public void causeEffect(Game g) {
        g.setNextState(Game.GameState.nextColor, color);
        g.setNextState(Game.GameState.nextNumber, number);
        g.setNextState(Game.GameState.nextEffect, effect);
    }

    public boolean isNumber(int n) {
        return number == n;
    }

    public int getNumber() {
        return number;
    }
}
