package CardPackage;

import GamePackage.*;

public class Card {
    public enum Color {
        RED("Red"), YELLOW("Yellow"), GREEN("Green"), BLUE("Blue"), NONE("");

        private final String colorString;

        Color(String val) {
            colorString = val;
        }

        @Override
        public String toString() {
            return colorString;
        }
    }

    public enum Effect {
        WILD, WILD4, SKIP, REVERSE, DRAW2, NONE
    }

    protected Color color = Color.NONE;
    protected Effect effect = Effect.NONE;

    public Card() { }

    public Card(Effect e, Color c) {
        color = c;
        effect = e;
    }

    public void causeEffect(Game g) {

    }

    public boolean isWildType() {
        return effect.equals(Effect.WILD) || effect.equals(Effect.WILD4);
    }

    public boolean isColor(Color c) {
        return color.equals(c);
    }

    public Effect getEffect() {
        return effect;
    }

    public Color getColor() {
        return color;
    }
}

