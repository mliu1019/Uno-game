package CardPackage;

import GamePackage.Game;

public class NumberCard extends Card{
    private int number;
    public NumberCard(int n, Color c) {
        color = c;
        number = n;
        effect = Effect.NONE;
    }


    @Override
    public void causeEffect(Game g) {
        g.setState(Game.GameState.nextColor, color);
        g.setState(Game.GameState.nextNumber, number);
        g.setState(Game.GameState.nextEffect, effect);
    }


    public boolean isNumber(int n) {
        return number == n;
    }


    public int getNumber() {
        return number;
    }
}
