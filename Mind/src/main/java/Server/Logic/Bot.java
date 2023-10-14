package Server.Logic;

import java.util.List;
import java.util.Random;

public class Bot extends Player implements Runnable{

    private final int delay = 7000;
    private final int interval = 2250;
    private final int precautionParameter = 500;

    public Bot(List<Integer> hand, PlayerType playerType, String playerName) {
        super(hand, playerType, playerName);
    }

    public void play(){
        int i;
        while (!hand.isEmpty()){
            try {
                if(memberOf(this) == null) return;
                int interval = (new Random()).nextInt(this.interval);
                Thread.sleep((long) (delay + precautionParameter * Math.sqrt(min(this.hand)) + interval));
                if(hand.isEmpty()) break;

                i = min(this.hand);
                this.hand.remove((Object)i);

                if(memberOf(this) == null) return;
                memberOf(this).setBurnt(i);
                print();

                memberOf(this).punish(i);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int min(List<Integer> ints){
        int temp = ints.get(0);
        for (Integer i:ints){
            if(temp>i){
                temp=i;
            }
        }
        return temp;
    }

    @Override
    public void run() {
//        memberOf(this).setPrintable(memberOf(this).getPrintable()+1);
        if(this.hand.size()==0){
            //close socket and threads
            return;
        }
        play();
    }

}

