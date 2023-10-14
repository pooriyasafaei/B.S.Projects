package logic.players.bots;

import logic.cards.Card;
import logic.cards.CardType;
import logic.game.Actions;
import logic.players.Player;

import java.util.Random;

public class CouperAndroid extends Player {

    public CouperAndroid(String name, int playerNumber, Card card1, Card card2) {
        super(name, playerNumber, true);
        getCards()[0] = card1;
        getCards()[1] = card2;
    }

    @Override
    public void startCall() {

        int sleep = (new Random()).nextInt(-interval, interval);
        try {
            Thread.sleep(botAvgDelay + sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(coins >= 7){
            Card playThis;
            if(!cards[1].isDead()) {
                playThis = cards[1];
            } else {
                playThis = cards[0];
            }
            game.callBack(playerNumber, Actions.coup, game.nextPlayer(), playThis);
            return;
        }

        Card playThis;
        if(cards[1].isDead()){
            playThis = cards[0];
            game.callBack(playerNumber, Actions.take3, this.playerNumber, playThis);
            return;
        }

        if(cards[0].isDead() || ((cards[1].getCardType() == CardType.duke && !cards[1].isDead()))){
            playThis = cards[1];
        } else {
            playThis = cards[0];
        }
        game.callBack(playerNumber, Actions.take3, this.playerNumber, playThis);
    }

    @Override
    public void challengeCall(Actions action) {

        int sleep = (new Random()).nextInt(-interval, interval);
        try {
            Thread.sleep(botAvgDelay + sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Card playThis;
        if (!cards[1].isDead()) {
            playThis = cards[1];
        }
        else {
            playThis = cards[0];
        }
        game.callBack(playerNumber, Actions.skip, this.playerNumber, playThis);
    }

    @Override
    public void blockCall(Actions action) {

        int sleep = (new Random()).nextInt(-interval, interval);
        try {
            Thread.sleep(botAvgDelay + sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Card playThis;

        if(action == Actions.steal){

            if (this.hasCard(CardType.ambassador)) {
                if (cards[0].getCardType() == CardType.ambassador && !cards[0].isDead()) {
                    playThis = cards[0];
                } else {
                    playThis = cards[1];
                }

                game.callBack(playerNumber, Actions.block, this.playerNumber, playThis);
                return;
            }

            if (this.hasCard(CardType.captain)) {
                if (cards[0].getCardType() == CardType.captain && !cards[0].isDead()) {
                    playThis = cards[0];
                } else {
                    playThis = cards[1];
                }

                game.callBack(playerNumber, Actions.block, this.playerNumber, playThis);
                return;
            }

            if (!cards[1].isDead()) {
                playThis = cards[1];
            }
            else {
                playThis = cards[0];
            }
            game.callBack(playerNumber, Actions.skip, this.playerNumber, playThis);
        }

        if(action == Actions.take2){
            if (this.hasCard(CardType.duke)) {
                if (cards[0].getCardType() == CardType.duke && !cards[0].isDead()) {
                    playThis = cards[0];
                } else {
                    playThis = cards[1];
                }

                game.callBack(playerNumber, Actions.block, this.playerNumber, playThis);
                return;
            }

            if (!cards[1].isDead()) {
                playThis = cards[1];
            }
            else {
                playThis = cards[0];
            }
            game.callBack(playerNumber, Actions.skip, this.playerNumber, playThis);
        }

        if(action == Actions.murder){
            if (this.hasCard(CardType.contessa)) {
                if (cards[0].getCardType() == CardType.contessa  && !cards[0].isDead()) {
                    playThis = cards[0];
                } else {
                    playThis = cards[1];
                }

                game.callBack(playerNumber, Actions.block, this.playerNumber, playThis);
                return;
            }

            if (!cards[1].isDead()) {
                playThis = cards[1];
            }
            else {
                playThis = cards[0];
            }
            game.callBack(playerNumber, Actions.skip, this.playerNumber, playThis);
        }
    }

    @Override
    public void kill() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (cards[1].isDead()) {
            game.killed(playerNumber, cards[0]);
            return;
        }

        if (cards[0].isDead() || (!cards[0].isDead() && cards[1].getCardType() != CardType.duke)) {
            game.killed(playerNumber, cards[1]);
        } else {
            game.killed(playerNumber, cards[0]);

        }
    }

    @Override
    public void choose(Card card1, Card card2) {

    }
}
