package logic.game;

import gui.MainFrame;
import gui.parts.Board;
import logic.config.Config;
import logic.players.Player;
import logic.cards.Card;
import logic.cards.CardType;
import logic.players.RealPlayer;

import java.io.IOException;
import java.util.*;

public class BoardGame {

    private final int numberOfPlayers = 4;

    private List<Card> cards = new ArrayList<>();
    private final Player[] players = new Player[numberOfPlayers];

    private final boolean[] confirmation = new boolean[numberOfPlayers];

    private String message = "Game has been started!";

    private int functor;

    private boolean receipting = true;
    private boolean functorPassed = false;

    private boolean startPhase = true;
    private boolean challengePhase = false;
    private boolean blockPhase = false;
    private boolean giveUpPhase = false;

    private final Actions[] actions = new Actions[4];
    private final Player[] challengers = new Player[4];
    private final Card[] playingCards = new Card[4];

    private MainFrame gui;

    private static BoardGame mainGame;

    public static BoardGame getInstance(){
        if(mainGame == null)
            mainGame = new BoardGame();
        return mainGame;
    }

    public void startGame(){
          receipting = true;
          functorPassed = false;

          startPhase = true;
          challengePhase = false;
          blockPhase = false;

          functor = numberOfPlayers - 1;

          Config.getInstance();

          players[0] = Config.getPlayer();
          players[1] = Config.getPlayer2();
          players[2] = Config.getPlayer3();
          players[3] = Config.getPlayer4();

        try {
            cards = Config.getInstance().makeDeck();

            Collections.shuffle(cards);

            gui = new MainFrame(this);
            gui.setMessage("Game Started");
            gui.noticeEvent("Game started");
            ((RealPlayer) players[0]).setFrame(gui);

            startRound();

        }catch (Exception e){
            cards = new ArrayList<>();

            gui = new MainFrame(this);
            gui.setMessage("Game Started");
            gui.noticeEvent("Game started");
            ((RealPlayer) players[0]).setFrame(gui);

            setMessage("Distribution of cards are not possible. change it in Settings...");
            noticeEvent("Distribution of cards are not possible. change it in Settings...");

        }

    }

    public int nextPlayer(){
        if(!players[(functor + 1) % 4].isDead()) return (functor + 1) % 4;
        if(!players[(functor + 2) % 4].isDead()) return (functor + 2) % 4;
        if(!players[(functor + 3) % 4].isDead()) return (functor + 3) % 4;
        return (functor + 4) % 4;
    }

    public void startRound() {
        receipting = false;
        gui.getBoard().refresh();

        functorPassed = false;
        startPhase = true;
        challengePhase = false;
        blockPhase = false;
        functor = nextPlayer();

        if(isGameEnded()){
            startPhase = false;
            challengePhase = false;
            blockPhase = false;
            receipting = false;
            return;
        }

        Arrays.fill(confirmation, true);
        confirmation[functor] = false;

        Arrays.fill(playingCards, null);
        Arrays.fill(actions, null);
        Arrays.fill(challengers, null);

        noticeEvent("\nnext round is started: ");

        receipting = true;

        caller();
    }

    public void startChallengePhase(){
        receipting = false;

        startPhase = false;
        challengePhase = true;
        blockPhase = false;

        Arrays.fill(confirmation, false);

        if(functorPassed)
            confirmation[challengers[1].getPlayerNumber()] = true;
        else confirmation[challengers[0].getPlayerNumber()] = true;

        //confirmation[functor] = true;
        for (int i = 0; i < numberOfPlayers; i++)
            if(players[i].isDead())
                confirmation[i] = true;

        if(situationPassed()){
            if(functorPassed)
                startRound();
            else if(actions[0].isBlocking()) startBlockPhase();
                    else doAction();
            return;
        }

        noticeEvent("challenge phase started for the action: ");
        receipting = true;

        caller();
    }

    public void startBlockPhase(){
        receipting = false;

        functorPassed = true;

        startPhase = false;
        challengePhase = false;
        blockPhase = true;

        Arrays.fill(confirmation, true);

        if(actions[0] == Actions.take2){
            Arrays.fill(confirmation, false);
            confirmation[functor] = true;
        }else {
            Arrays.fill(confirmation, true);
            confirmation[challengers[1].getPlayerNumber()] = false;
        }

        for (int i = 0; i < numberOfPlayers; i++)
            if(players[i].isDead())
                confirmation[i] = true;

        if(situationPassed()){
            doAction();
            return;
        }

        noticeEvent("block phase started for the action: ");
        receipting = true;

        caller();
    }

    public void startMurderPhase(){
        noticeEvent(challengers[0].getName() + " killed a card of " + challengers[1].getName());
        receipting = false;


        giveUpPhase = true;
        startPhase = false;
        challengePhase = false;
        blockPhase = false;

        challengers[1].kill();

    }

    public void startChoosePhase() {
        receipting = false;

        startPhase = false;
        challengePhase = false;
        blockPhase = false;

        challengers[0].choose(cards.get(0), cards.get(1));
    }

    public void giveMe(int playerNumber, int selectedCard, int playerSelectedCard) {

        if(selectedCard < 0){
            noticeEvent(challengers[0].getName() + " saw 2 cards but didn't changed any cards");
            Collections.shuffle(cards);
            startRound();
            return;
        }

        if(players[playerNumber].getCards()[playerSelectedCard].isDead()){
            setMessage("you can't change a dead card");
            return;
        }

        Card playerCard = players[playerNumber].getCards()[playerSelectedCard];
        players[playerNumber].getCards()[playerSelectedCard] = cards.get(selectedCard);
        cards.remove(selectedCard);
        cards.add(playerCard);
        Collections.shuffle(cards);

        noticeEvent(challengers[0].getName() + " changed a card.");

        startRound();
    }

    public void verify() {
        receipting = false;

        int accused;
        if(functorPassed) accused = 1;
        else accused = 0;

        if(doesCartSupportAction(playingCards[accused], actions[accused])){

            playingCards[accused + 2].kill();

            noticeEvent(challengers[accused].getName() + " had the card fo the action and will" +
                    " change it with a card on deck and "
            + challengers[accused + 2].getName() + " lost the challenge.");

            if(challengers[accused + 2].isDead())
                noticeEvent(challengers[accused + 2].getName() + " is out of game!");

            if(!functorPassed) {
                if(actions[0].isBlocking()) {

                    if(playingCards[accused].getCardType() != CardType.ambassador) {
                        if (playingCards[accused] == challengers[accused].getCards()[0]) {
                            playingCards[accused] = change(playingCards[accused]);
                            challengers[accused].getCards()[0] = playingCards[accused];
                        } else {
                            playingCards[accused] = change(playingCards[accused]);
                            challengers[accused].getCards()[1] = playingCards[accused];
                        }
                    }

                    startBlockPhase();
                    return;
                }

                else {
                    if(playingCards[accused].getCardType() != CardType.ambassador) {
                        if (playingCards[accused] == challengers[accused].getCards()[0]) {
                            playingCards[accused] = change(playingCards[accused]);
                            challengers[accused].getCards()[0] = playingCards[accused];
                        } else {
                            playingCards[accused] = change(playingCards[accused]);
                            challengers[accused].getCards()[1] = playingCards[accused];
                        }
                    }

                    doAction();
                    return;
                }
            }
            startRound();
        }

        else {
            playingCards[accused].kill();
            noticeEvent(challengers[accused].getName() + " didn't had the card fo the action and "
                    + challengers[accused + 2].getName() + " won the challenge.");

            if(challengers[accused].isDead())
                noticeEvent(challengers[accused].getName() + " is out of game!");

            if(functorPassed) doAction();
             else startRound();
        }
    }

    public void killed(int playerNumber,Card card)  {

        if(!giveUpPhase){
            setMessage("Why should give up your card in this situation?!");
            return;
        }

        if(card.isDead()){
            setMessage("you can't surround a dead card");
            players[playerNumber].kill();
            return;
        }

        card.kill();

        if(players[playerNumber].isDead()){
            noticeEvent(players[playerNumber].getName() + " is out of game!");
        }

        giveUpPhase = false;
        startRound();
    }

    public Card change(Card card){
        cards.add(card);
        Collections.shuffle(cards);

        return cards.remove(0);
    }

    public void caller(){
        Thread[] threads = new Thread[numberOfPlayers];

         for (int i = 0; i < numberOfPlayers; i++){

                if(!players[i].isDead() && startPhase && !confirmation[i]){
                    threads[i] = new Thread(players[i]::startCall);
                    threads[i].start();
                }

                if(!players[i].isDead() && challengePhase && !confirmation[i]){
                    int finalI = i;
                    if(!functorPassed) {
                        threads[i] = new Thread(() -> players[finalI].challengeCall(actions[0]));
                    } else {
                        threads[i] = new Thread(() -> players[finalI].challengeCall(actions[1]));
                    }
                    threads[i].start();
                }

                if(!players[i].isDead() && blockPhase && !confirmation[i]){
                    int finalI = i;
                    threads[i] = new Thread(() -> players[finalI].blockCall(actions[0]));

                    threads[i].start();
                }
         }
    }

    public boolean situationPassed(){

        for (boolean bool: confirmation)
            if (!bool) return false;
        return true;
    }

    public void callBack(int playerNumber, Actions action, int targetNumber, Card card) {

        if(!receipting){
            setMessage("no actions allowed in this situation");
            return;
        }

        if(players[targetNumber].isDead()){
            setMessage("can't do action on a dead player!");
            return;
        }

        if(startPhase){
            if(playerNumber != functor){
                setMessage("it's not your turn " + players[playerNumber].getName() + "!");
                caller();
                return;
            }

            if(players[functor].getCoins() >= 10 && action != Actions.coup){
                setMessage(players[functor].getName() + " has more than 10 coins and only must coup!");
                caller();
                return;
            }

            switch (action){

                case coup:
                    if(players[functor].getCoins() < 7){
                        setMessage(players[functor].getName() + " doesn't have enough coin for a coup!");
                        caller();
                        return;
                    }
                    if(players[targetNumber].isDead()){
                        setMessage("you can't do action on a dead player!");
                        caller();
                        return;
                    }
                    players[playerNumber].setCoins(players[playerNumber].getCoins() - 7);
                    challengers[0] = players[playerNumber];
                    challengers[1] = players[targetNumber];

                    actions[0] = action;

                    receipting = false;
                    noticeEvent(challengers[0].getName() + " coups against " + challengers[1].getName());
                    doAction();
                    return;

                case take1:
                    challengers[0] = players[playerNumber];

                    players[playerNumber].setCoins(players[playerNumber].getCoins() + 1);
                    actions[0] = action;

                    receipting = false;
                    noticeEvent(challengers[0].getName() + " took a coin from bank");
                    startRound();
                    return;

                case change:
                    if(players[functor].getCoins() < 1){
                        setMessage(players[functor] + " doesn't have enough coin to change a cart!");
                        caller();
                        return;
                    }

                    if(card.isDead()){
                        setMessage(players[functor] + ", you can't change a cart that is dead!");
                        caller();
                        return;
                    }

                    challengers[0] = players[playerNumber];
                    actions[0] = action;
                    playingCards[0] = card;

                    receipting = false;
                    noticeEvent(challengers[0].getName() + " paid a coin and changed a card from deck");
                    doAction();
                    return;

                case take2:

                    challengers[0] = players[playerNumber];
                    actions[0] = action;
                    receipting = false;
                    noticeEvent(challengers[0].getName() + " attempts to take foreign aid");
                    startBlockPhase();
                    return;

                case murder:

                    if(players[functor].getCoins() < 3){
                        setMessage(players[functor] + " doesn't have enough coin to murder!");
                        caller();
                        return;
                    }

                    if(card.isDead()){
                        setMessage(players[functor] + ", you can't use a cart that is dead!");
                        caller();
                        return;
                    }

                    if(players[targetNumber].isDead()){
                        setMessage("you can't do action on a dead player!");
                        caller();
                        return;
                    }

                    challengers[0] = players[playerNumber];
                    challengers[1] = players[targetNumber];
                    actions[0] = action;
                    playingCards[0] = card;

                    players[playerNumber].setCoins(players[playerNumber].getCoins() - 3);
                    receipting = false;
                    noticeEvent(challengers[0].getName() + " attempts to kill " + challengers[1].getName());
                    break;

                case steal:
                    if(players[targetNumber].getCoins() < 1){
                        setMessage(players[functor] + " doesn't have any coins to steal!");
                        caller();
                        return;
                    }

                    if(players[targetNumber].isDead()){
                        setMessage("you can't do action on a dead player!");
                        caller();
                        return;
                    }
                    if(card.isDead()){
                        setMessage(players[functor] + ", you can't use a card that is dead!");
                        caller();
                        return;
                    }

                    receipting = false;
                    noticeEvent(players[playerNumber].getName() + " attempts to steal coins from "
                            + players[targetNumber].getName());
                    break;

                case take3:

                    if(card.isDead()){
                        setMessage(players[functor] + ", you can't use a card that is dead!");
                        caller();
                        return;
                    }

                    receipting = false;
                    noticeEvent(players[playerNumber].getName() + " attempts to take 3 coins from bank");
                    break;

                case choose:
                    if(card.isDead()){
                        setMessage(players[functor] + ", you can't use a card that is dead!");
                        caller();
                        return;
                    }

                    receipting = false;

                    noticeEvent(players[playerNumber].getName() + " attempts to choose and change from two cards on deck");
                    break;

                default:
                    setMessage("not a valid move in this situation");
                    return;
            }

            challengers[0] = players[playerNumber];
            challengers[1] = players[targetNumber];
            playingCards[0] =  card;
            actions[0] = action;
            startChallengePhase();
            return;
        }

        if(challengePhase){

            switch (action){
                case skip:
                    confirmation[playerNumber] = true;
                    noticeEvent(players[playerNumber].getName() + " skips challenge.");

                    if(situationPassed()){
                        noticeEvent("challenge phase skipped.");
                        if(functorPassed){
                            startRound();
                        } else {

                            if(actions[0].isBlocking()){
                                functorPassed = true;
                                startBlockPhase();

                            } else {
                                doAction();
                            }
                        }
                    }
                    return;

                case challenge:
                    if(card.isDead()){
                        setMessage(players[functor].getName() + ", you can't use a cart that is dead!");
                        players[functor].startCall();
                        return;
                    }
                    receipting = false;
                    noticeEvent(players[playerNumber].getName() + " challenges the action");

                    int accused;
                    if(functorPassed)
                        accused = 1;
                    else accused = 0;
                    playingCards[accused + 2] = card;
                    challengers[accused + 2] = players[playerNumber];
                    actions[accused + 2] = action;
                    verify();
                    return;

                default:
                    setMessage("not a valid move in this situation");
                    caller();
                    return;
            }
        }

        if(blockPhase){
            switch (action){
                case skip:
                    confirmation[playerNumber] = true;
                    noticeEvent(players[playerNumber].getName() + " doesn't block the action.");

                    if(situationPassed()) {
                        doAction();
                    }

                    return;

                case block:
                    if(card.isDead()){
                        setMessage(players[functor].getName() + ", you can't use a cart that is dead!");
                        caller();
                        return;
                    }
                    receipting = false;
                    noticeEvent(players[playerNumber].getName() + " blocks the action");

                    playingCards[1] = card;
                    challengers[1] = players[playerNumber];
                    actions[1] = action;

                    functorPassed = true;
                    startChallengePhase();
                    return;

                default:
                    setMessage("not a valid move in this situation");
                    caller();
                    return;
            }
        }
    }

    public boolean isGameEnded(){
        int counter = 0;
        Player winner = null;

        for (Player player: players)
            if(player.isDead()) counter++;
            else  winner = player;

        if(counter >= numberOfPlayers - 1) {
            setMessage("Game is ended");

            noticeEvent("\n\nGame is over and " + winner.getName() + " won the match!");
            return true;
        }
        return false;
    }

    public void doAction(){
        receipting = false;

        switch (actions[0]){

            case murder:
                if(!challengers[1].isDead()){
                    startMurderPhase();
                    return;
                }

                break;

            case take2:
                challengers[0].setCoins(challengers[0].getCoins() + 2);
                noticeEvent(challengers[0].getName() + " took foreign aid.");

                break;

            case take3:
                challengers[0].setCoins(challengers[0].getCoins() + 3);
                noticeEvent(challengers[0].getName() + " took 3 coins.");
                break;

            case steal:
                if(challengers[1].getCoins() > 1) {
                    challengers[0].setCoins(challengers[0].getCoins() + 2);
                    challengers[1].setCoins(challengers[1].getCoins() - 2);
                    noticeEvent(challengers[0].getName() + " stole 2 coins from " + challengers[1].getName());

                }else {
                    challengers[0].setCoins(challengers[0].getCoins() + 1);
                    challengers[1].setCoins(challengers[1].getCoins() - 1);

                    noticeEvent(challengers[0].getName() + " stole 1 coin from " + challengers[1].getName());

                }
                break;

            case coup:
                startMurderPhase();
                return;

            case change:
                challengers[0].setCoins(challengers[0].getCoins() - 1);
                if(playingCards[0] == challengers[0].getCards()[0]){
                    playingCards[0] = change(playingCards[0]);
                    challengers[0].getCards()[0] =playingCards[0];

                } else {
                    playingCards[0] = change(playingCards[0]);
                    challengers[0].getCards()[1] = playingCards[0];
                }

                noticeEvent(challengers[0].getName() + " changed a card.");

                startRound();
                return;

            case choose:
                noticeEvent("functor choose between 2 cards and changes a card.");
                startChoosePhase();

                return;
        }

        startRound();
    }

    public boolean doesCartSupportAction(Card card, Actions action){
        switch (action){

            case choose:
                return card.getCardType() == CardType.ambassador;

            case murder:
                return card.getCardType() == CardType.assassin;

            case steal:
                return card.getCardType() == CardType.captain;

            case take3:
                return card.getCardType() == CardType.duke;

            case block:
                switch (actions[0]){
                    case take2:
                        return card.getCardType() == CardType.duke;

                    case murder:
                        return card.getCardType() == CardType.contessa;

                    case steal:
                        return card.getCardType() == CardType.ambassador || card.getCardType() == CardType.captain;
                }
        }

        return false;
    }

    public void noticeEvent(String message){
        gui.noticeEvent(message);
    }

    public void setMessage(String message) {
        this.message = message;
        gui.setMessage(message);
    }

    public Player[] getPlayers() {
        return players;
    }

    public boolean isGiveUpPhase() {
        return giveUpPhase;
    }

    public Player[] getChallengers() {
        return challengers;
    }
}
