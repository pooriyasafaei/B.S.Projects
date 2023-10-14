package logic.players;

import logic.cards.Card;
import logic.cards.CardType;
import logic.game.Actions;
import logic.game.BoardGame;

public abstract class Player {

    protected final String name;
    protected final int playerNumber;

    protected final boolean robot;
    protected final int botAvgDelay = 2500;
    protected final int interval = 2500;

    protected Card[] cards = new Card[2];
    protected int coins = 2;

    protected static BoardGame game = BoardGame.getInstance();

    protected Player(String name, int playerNumber, boolean robot) {
        this.name = name;
        this.playerNumber = playerNumber;
        this.robot = robot;
    }

    public abstract void startCall() ;

    public abstract void challengeCall(Actions action) ;

    public abstract void blockCall(Actions action);

    public abstract void kill();

    public abstract void choose(Card card1, Card card2);

    public boolean hasCard(CardType cardType){
        if(cards[0].getCardType() == cardType && !cards[0].isDead())
            return true;
        return cards[1].getCardType() == cardType && !cards[1].isDead();
    }

    public boolean isDead(){
        return cards[0].isDead() && cards[1].isDead();
    }

    public String getName() {
        return name;
    }

    public boolean isRobot() {
        return robot;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Card[] getCards() {
        return cards;
    }

    public int getBotAvgDelay() {
        return botAvgDelay;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public BoardGame getGame() {
        return game;
    }

    public void setGame(BoardGame game) {
        Player.game = game;
    }
}
