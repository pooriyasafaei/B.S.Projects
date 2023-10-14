package Server.Logic;

import java.util.List;

public class Player implements Runnable{
    public static int playerCntr=0;
    protected int number;
    protected List<Integer> hand;
    protected PlayerType playerType;
    protected String playerName;
    protected Game game;

    public Player(List<Integer> hand,PlayerType playerType,String playerName) {
        playerCntr++;
        this.number=playerCntr;
        this.hand = hand;
        this.playerType=playerType;
        this.playerName=playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getNumber() {
        return number;
    }

    public List<Integer> getHand() {
        return hand;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setHand(List<Integer> hand) {
        this.hand = hand;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    public void burnLessThan(int burnt){

        if(hand.isEmpty()) return;
        int i = min(this.hand);

        while (i < burnt){
            this.hand.remove((Object) i);

            memberOf(this).setBurnt(i);
            print();

            if(hand.isEmpty()) return;
            i = min(this.hand);
        }
    }

    public void burnLeast(){

        if(hand.isEmpty()) return;
        int i = min(this.hand);

        hand.remove((Object)i);
        memberOf(this).setBurnt(i);
        print();
    }

    @Override
    public void run() {
    }

    protected Game memberOf(Player player){
        for (Game g: Game.games){
            if(g.getPlayers().contains(player)){
                return g;
            }
        }
        return null;
    }

    public int min(List<Integer> ints){
        int temp=ints.get(0);
        for (Integer i:ints){
            if(temp>i){
                temp=i;
            }
        }
        return temp;
    }

    protected void print(){
        String message = this.playerName+" CARDS :"+this.hand +"   "+
                "Hearts: "+memberOf(this).getHearts()+"   "+
                "Stars: "+memberOf(this).getStars()+"   "+
                "Last played card is: "+memberOf(this).getBurnt();
        System.out.println(message);

        message = this.playerName+" played" +"   "+
                "Hearts: "+memberOf(this).getHearts()+"   "+
                "Stars: "+memberOf(this).getStars()+"   "+
                "and played card is: "+memberOf(this).getBurnt();
        memberOf(this).broadcast(message);
    }
}
