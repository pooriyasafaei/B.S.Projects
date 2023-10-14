package logic.players.bots;

import logic.cards.Card;
import logic.cards.CardType;
import logic.game.Actions;
import logic.players.Player;

import java.util.Random;

public class ExtortionistAndroid extends Player {

    public ExtortionistAndroid(String name, int playerNumber, Card card1, Card card2) {
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

        if(coins >= 10){
            Card playThis;
            if(!cards[1].isDead()) {
                playThis = cards[1];
            } else {
                playThis = cards[0];
            }
            game.callBack(playerNumber, Actions.coup, game.nextPlayer(), playThis);
            return;
        }

        if(this.hasCard(CardType.duke)){
            Card playThis;
            if(cards[0].getCardType() == CardType.duke && !cards[0].isDead()){
                playThis = cards[0];
            } else playThis = cards[1];

            game.callBack(playerNumber, Actions.take3, this.playerNumber, playThis);
            return;
        }

        int target = nextRich();
        Card playThis;

        if (cards[1].isDead()) {
            playThis = cards[0];
        } else playThis = cards[1];

        if(target >= 0 ) {
            if (cards[1].isDead()) {
                playThis = cards[0];
                game.callBack(playerNumber, Actions.steal, target, playThis);
                return;
            }

            if (cards[0].isDead() || ((cards[1].getCardType() == CardType.captain && !cards[1].isDead()))) {
                playThis = cards[1];
            } else {
                playThis = cards[0];
            }
            game.callBack(playerNumber, Actions.steal, target, playThis);
        }
        else game.callBack(playerNumber, Actions.take2, this.playerNumber, playThis);
    }

    private int nextRich(){
        int target = -1;
        int max = 0;

        for (Player player: game.getPlayers()){
            if(player == this || player.isDead()) continue;

            if(player.getCoins() > max){
                max = player.getCoins();
                target = player.getPlayerNumber();
            }
        }
        return target;
    }

    @Override
    public void challengeCall(Actions action) {
        int sleep = (new Random()).nextInt(-interval, interval);
        try {
            Thread.sleep(botAvgDelay + sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!this.hasCard(CardType.captain)){
            if (action == Actions.steal) {

                if (cards[1].isDead()) {
                    game.callBack(playerNumber, Actions.challenge, this.playerNumber, cards[0]);
                    return;
                }

                if (cards[0].isDead() || (!cards[0].isDead() && cards[1].getCardType() != CardType.duke
                        && cards[1].getCardType() != CardType.captain)) {
                    game.callBack(playerNumber, Actions.challenge, this.playerNumber, cards[1]);
                    return;
                } else {
                    game.callBack(playerNumber, Actions.challenge, this.playerNumber, cards[0]);
                    return;
                }
            }
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
                if (cards[0].getCardType() == CardType.contessa && !cards[0].isDead()) {
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

        if (cards[0].isDead() || (!cards[0].isDead() && cards[1].getCardType() != CardType.duke
                || cards[1].getCardType() != CardType.captain)) {
            game.killed(playerNumber, cards[1]);
        } else {
            game.killed(playerNumber, cards[0]);

        }
    }

    @Override
    public void choose(Card card1, Card card2) {

    }
}
