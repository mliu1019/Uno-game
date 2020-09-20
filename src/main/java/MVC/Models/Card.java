package MVC.Models;

public class Card {
    private CardPackage.Card.Color color;
    private CardPackage.Card.Effect effect;
    private int number;

    public CardPackage.Card.Color getColor() {
        return color;
    }

    public void setColor(CardPackage.Card.Color color) {
        this.color = color;
    }

    public CardPackage.Card.Effect getEffect() {
        return effect;
    }

    public void setEffect(CardPackage.Card.Effect effect) {
        this.effect = effect;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
