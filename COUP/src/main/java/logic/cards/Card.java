package logic.cards;

public class Card {

    private final CardType cardType;
    private boolean dead = false;

    public Card(CardType cardType) {
        this.cardType = cardType;
        this.dead = false;
    }

    public void kill(){
        this.dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public CardType getCardType() {
        return cardType;
    }
}
