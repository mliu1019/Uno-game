package CardPackage;

import GamePackage.*;

/*
 * This is an abstract class for Uno Cards.
 */
public class Card {

    /*
     * Each card is assigned a color. For Wild Cards and Wild Draw Four Cards, the color is set to NONE.
     * For all the other cards, the color is set as displayed as they are.
     */
    public enum Color {
        RED("Red"), YELLOW("Yellow"), GREEN("Green"), BLUE("Blue"), NONE("");

        private final String colorString;

        Color(String val) {
            colorString = val;
        }

        /*
         * Returns the color in the form of a character string.
         */
        @Override
        public String toString() {
            return colorString;
        }
    }

    /*
     * Each card is assigned an effect. For Number Cards, the effect is set to NONE.
     * For all the other cards, the effect is set as displayed as they are.
     */
    public enum Effect {
        SKIP, REVERSE, DRAW2, WILD, WILD4, NONE
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

