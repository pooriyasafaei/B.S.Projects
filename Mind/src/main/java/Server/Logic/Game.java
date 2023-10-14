package Server.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Game implements Runnable{
    public static List<Game> games = new ArrayList<>();
    private List<Integer> allCardsOut = new ArrayList<>();
    private int GameNumber;
    private List<Player> players;
    private int lastBurnt;
    private int stars;
    private int hearts;
    private int CurrentLevel=1;
    private int numberOfPlayers;
    private static int gameCntr=0;
    private int printable=0;
    private boolean gameEnded = false;


    public Game(List<Player> players,int hearts,int stars,int numberOfPlayers) {
        for (int i = 0; i < 100; i++) {
            allCardsOut.add(i + 1);
        }
        gameCntr++;
        this.GameNumber =gameCntr;
        this.numberOfPlayers =numberOfPlayers;
        this.players = players;
        this.hearts=hearts;
        this.stars=stars;

        games.add(this);
    }

    public int getBurnt() {
        return lastBurnt;
    }

    public int getStars() {
        return stars;
    }

    public int getHearts() {
        return hearts;
    }

    public void setBurnt(int burnt) {
        this.lastBurnt = burnt;

    }

    public void punish(int burnt){
        if(isLost(burnt)){
            hearts--;
            broadcast("Oops, a card that is not minimum("+ burnt + ") was played. your team lost a heart and all " +
                    "cards less than the number played will burn.");

            for (Player player: players)
                player.burnLessThan(burnt);
        }
    }

    public boolean isLost(int burnt){
        for (Player player: players) {
            if(!player.getHand().isEmpty() && player.min(player.getHand()) < burnt) return true;
        }
        return false;
    }

    public void broadcast(String message){
        //TODO
        //sending status of game to every human player
        for (Player player: players){
            if(player instanceof Human){
                if(message.contains("and played card is:"))
                ((Human)player).sendMessage(message + " and your current hand is: " + player.hand);
                else ((Human)player).sendMessage(message);
            }
        }
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getGameNumber() {
        return GameNumber;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setGameNumber(int gameNumber) {
        GameNumber = gameNumber;
    }

    public void setPlayers(List<Player> players) {
        this.players =players;
    }

    public int getPrintable() {
        return printable;
    }

    public void setPrintable(int printable) {
        this.printable = printable;
    }

    public int getCurrentLevel() {
        return CurrentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        CurrentLevel = currentLevel;
        if(currentLevel==3 || currentLevel==6 || currentLevel==9){
            this.setHearts(this.getHearts()+1);
        }
        if(currentLevel==2 || currentLevel==5 || currentLevel==8){
            this.setStars(this.getStars()+1);
        }

        this.allCardsOut.clear();
        for (int i = 0; i < 100; i++) {
            allCardsOut.add(i+1);
        }

        for (Player player:this.getPlayers()){
            player.setHand(spreadCards());
        }
    }

    public boolean isHasCapacity() {
        if(gameEnded) return false;

        for (Player player: players) {
            if(player instanceof Bot) return true;
        }
        return false;
    }

    public List<Integer> getAllCardsOut() {
        return allCardsOut;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static int getGameCntr() {
        return gameCntr;
    }

    public void setAllCardsOut(List<Integer> allCardsOut) {
        this.allCardsOut = allCardsOut;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public static void setGameCntr(int gameCntr) {
        Game.gameCntr = gameCntr;
    }

    public void useNinja(){
        if(stars <= 0){
            broadcast("There is no ninja card to use!");
            return;
        }

        broadcast("Ninja card used! all cards with least number will be burn");
        stars--;
        for (Player player: players) {
            player.burnLeast();
        }
    }

    private boolean isLevelPassed(){
        for (Player player: players) {
            if(!player.hand.isEmpty()) return false;
        }
        return true;
    }

    private void endGame(){
        gameEnded = true;
        for (Player player: players) {
            if(player instanceof Human){
                ((Human)player).sendMessage("Have a great time. server closed the connection");
                try {
                    ((Human)player).getSocket().close();
                } catch (IOException e) {
                    System.out.println("Something went wrong while closing a socket");
                }
            }
        }
    }

    @Override
    public void run() {

        for (int i = 0; i < 12; i++) {
            printable = 0;
            System.out.println("Initializing hands for level "+getCurrentLevel());
            List<Thread> threads = new ArrayList<>();
            for (Player p:this.getPlayers()){
                System.out.println(p.playerName+" CARDS: "+ p.hand );
                if(p instanceof Human)
                    ((Human)p).sendMessage(p.playerName+" CARDS: "+ p.hand);
            }

            System.out.println("Level "+getCurrentLevel()+" started");
            broadcast("Level "+getCurrentLevel()+" started");
            for (Player p:this.getPlayers()){
                Thread t = new Thread(p);
                t.start();
                threads.add(t);
            }
            try {
               while(true){
                    Thread.sleep(200);
                    if(hearts <= 0){
                        System.out.println("Sorry, you lost the game and Numbers won! :)");
                        broadcast("Sorry, you lost the game and Numbers won! :)");
                        endGame();
                        return;
                    }
                    if(isLevelPassed()) break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setCurrentLevel(getCurrentLevel()+1);
        }
        broadcast("Game is over and players won!");
        endGame();
    }

    public List<Integer> spreadCards(){
        List<Integer> cards = new ArrayList<>();
        for (int j = 0; j < this.getCurrentLevel(); j++) {
            int k = ThreadLocalRandom.current().nextInt(0, this.getAllCardsOut().size()-1);

            this.getAllCardsOut().remove((Object)k);
            cards.add(k);
        }
        return cards;
    }

}

