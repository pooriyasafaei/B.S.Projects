package logic.config;

import com.google.gson.Gson;
import logic.cards.Card;
import logic.cards.CardType;
import logic.players.Player;
import logic.players.RealPlayer;
import logic.players.bots.CautiousAssassinAndroid;
import logic.players.bots.CouperAndroid;
import logic.players.bots.ExtortionistAndroid;
import logic.players.bots.ParanoidAndroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Config {

    private String playerInfo = "General:duke,assassin,0";
    private String player2Info = "CouperAndroid:ambassador,captain,1";
    private String player3Info = "CautiousAssassinAndroid:contessa,assassin,2";
    private String player4Info = "ParanoidAndroid:contessa,ambassador,3";

    private static Config config = null;
    private static final String configPass = "src\\main\\java\\logic\\config\\config.json";

    private static RealPlayer player;

    private static Player player2;

    private static Player player3;

    private static Player player4;

    public static Config getInstance(){
        if(config == null) config = readConfig();
        return config;
    }

    private static Config readConfig(){
        Gson gson = new Gson();

        try {
            File f = new File(configPass);
            Scanner scan = new Scanner(f);
            String json = scan.nextLine();

            config = gson.fromJson(json, Config.class);
            setPlayers();
            return config;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveConfig(){
        Gson gson = new Gson();
        try {
            FileWriter fw = new FileWriter(configPass);
            gson.toJson(config, fw);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Card> makeDeck(){
        int ambassador = 0, assassin = 0, captain = 0, contessa = 0, duke = 0;

        if(player.getCards()[0].getCardType() == CardType.ambassador)
            ambassador++;
        if(player.getCards()[0].getCardType() == CardType.assassin)
            assassin++;
        if(player.getCards()[0].getCardType() == CardType.captain)
            captain++;
        if(player.getCards()[0].getCardType() == CardType.contessa)
            contessa++;
        if(player.getCards()[0].getCardType() == CardType.duke)
            duke++;
        if(player.getCards()[1].getCardType() == CardType.ambassador)
            ambassador++;
        if(player.getCards()[1].getCardType() == CardType.assassin)
            assassin++;
        if(player.getCards()[1].getCardType() == CardType.captain)
            captain++;
        if(player.getCards()[1].getCardType() == CardType.contessa)
            contessa++;
        if(player.getCards()[1].getCardType() == CardType.duke)
            duke++;

        if(player2.getCards()[0].getCardType() == CardType.ambassador)
            ambassador++;
        if(player2.getCards()[0].getCardType() == CardType.assassin)
            assassin++;
        if(player2.getCards()[0].getCardType() == CardType.captain)
            captain++;
        if(player2.getCards()[0].getCardType() == CardType.contessa)
            contessa++;
        if(player2.getCards()[0].getCardType() == CardType.duke)
            duke++;
        if(player2.getCards()[1].getCardType() == CardType.ambassador)
            ambassador++;
        if(player2.getCards()[1].getCardType() == CardType.assassin)
            assassin++;
        if(player2.getCards()[1].getCardType() == CardType.captain)
            captain++;
        if(player2.getCards()[1].getCardType() == CardType.contessa)
            contessa++;
        if(player2.getCards()[1].getCardType() == CardType.duke)
            duke++;

        if(player3.getCards()[0].getCardType() == CardType.ambassador)
            ambassador++;
        if(player3.getCards()[0].getCardType() == CardType.assassin)
            assassin++;
        if(player3.getCards()[0].getCardType() == CardType.captain)
            captain++;
        if(player3.getCards()[0].getCardType() == CardType.contessa)
            contessa++;
        if(player3.getCards()[0].getCardType() == CardType.duke)
            duke++;
        if(player3.getCards()[1].getCardType() == CardType.ambassador)
            ambassador++;
        if(player3.getCards()[1].getCardType() == CardType.assassin)
            assassin++;
        if(player3.getCards()[1].getCardType() == CardType.captain)
            captain++;
        if(player3.getCards()[1].getCardType() == CardType.contessa)
            contessa++;
        if(player3.getCards()[1].getCardType() == CardType.duke)
            duke++;

        if(player4.getCards()[0].getCardType() == CardType.ambassador)
            ambassador++;
        if(player4.getCards()[0].getCardType() == CardType.assassin)
            assassin++;
        if(player4.getCards()[0].getCardType() == CardType.captain)
            captain++;
        if(player4.getCards()[0].getCardType() == CardType.contessa)
            contessa++;
        if(player4.getCards()[0].getCardType() == CardType.duke)
            duke++;
        if(player4.getCards()[1].getCardType() == CardType.ambassador)
            ambassador++;
        if(player4.getCards()[1].getCardType() == CardType.assassin)
            assassin++;
        if(player4.getCards()[1].getCardType() == CardType.captain)
            captain++;
        if(player4.getCards()[1].getCardType() == CardType.contessa)
            contessa++;
        if(player4.getCards()[1].getCardType() == CardType.duke)
            duke++;


        if(ambassador > 3 || assassin > 3 || captain > 3 || contessa > 3 || duke > 3){
            System.out.println("not a valid distribution for cards");
            return null;
        }

        ArrayList<Card> cards = new ArrayList<>();

        for (int i = 0; i < 3 - ambassador; i++) {
            cards.add(new Card(CardType.ambassador));
        }

        for (int i = 0; i < 3 - assassin; i++) {
            cards.add(new Card(CardType.assassin));
        }

        for (int i = 0; i < 3 - captain; i++) {
            cards.add(new Card(CardType.captain));
        }

        for (int i = 0; i < 3 - contessa; i++) {
            cards.add(new Card(CardType.contessa));
        }

        for (int i = 0; i < 3 - duke; i++) {
            cards.add(new Card(CardType.duke));
        }

        return cards;
    }

    public static void setPlayers(){
        player = (RealPlayer) makePlayerByConfig(config.playerInfo);
        player2 =  makePlayerByConfig(config.player2Info);
        player3 =  makePlayerByConfig(config.player3Info);
        player4 =  makePlayerByConfig(config.player4Info);

    }

    public static Player makePlayerByConfig(String data){

        String[] info = data.split(":");

        String[] cardsName = info[1].split(",");

        Card card1 = null;
        Card card2 = null;

        if(cardsName[0].equals("ambassador")) card1 = new Card(CardType.ambassador);
        if(cardsName[0].equals("assassin")) card1 = new Card(CardType.assassin);
        if(cardsName[0].equals("captain")) card1 = new Card(CardType.captain);
        if(cardsName[0].equals("contessa")) card1 = new Card(CardType.contessa);
        if(cardsName[0].equals("duke")) card1 = new Card(CardType.duke);

        if(cardsName[1].equals("ambassador")) card2 = new Card(CardType.ambassador);
        if(cardsName[1].equals("assassin")) card2 = new Card(CardType.assassin);
        if(cardsName[1].equals("captain")) card2 = new Card(CardType.captain);
        if(cardsName[1].equals("contessa")) card2 = new Card(CardType.contessa);
        if(cardsName[1].equals("duke")) card2 = new Card(CardType.duke);

        if(card1 == null || card2 == null){
            System.out.println("Not valid cards");
            return null;
        }

        switch (info[0]){
            case "CouperAndroid":
                return new CouperAndroid(info[0], Integer.parseInt(cardsName[2]), card1, card2);

            case "CautiousAssassinAndroid":
                return new CautiousAssassinAndroid(info[0], Integer.parseInt(cardsName[2]), card1, card2);

            case "ParanoidAndroid":
                return new ParanoidAndroid(info[0], Integer.parseInt(cardsName[2]), card1, card2);

            case "ExtortionistAndroid":
                return new ExtortionistAndroid(info[0], Integer.parseInt(cardsName[2]), card1, card2);

            default:
                return new RealPlayer(info[0], Integer.parseInt(cardsName[2]), card1, card2);
        }
    }

    public String getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(String playerInfo) {
        this.playerInfo = playerInfo;
    }

    public String getPlayer2Info() {
        return player2Info;
    }

    public void setPlayer2Info(String player2Info) {
        this.player2Info = player2Info;
    }

    public String getPlayer3Info() {
        return player3Info;
    }

    public void setPlayer3Info(String player3Info) {
        this.player3Info = player3Info;
    }

    public String getPlayer4Info() {
        return player4Info;
    }

    public void setPlayer4Info(String player4Info) {
        this.player4Info = player4Info;
    }

    public static Config getConfig() {
        return config;
    }

    public static void setConfig(Config config) {
        Config.config = config;
    }

    public static RealPlayer getPlayer() {
        setPlayers();
        return player;
    }

    public static void setPlayer(RealPlayer player) {
        Config.player = player;
    }

    public static Player getPlayer2() {
        setPlayers();
        return player2;
    }

    public static void setPlayer2(Player player2) {
        Config.player2 = player2;
    }

    public static Player getPlayer3() {
        setPlayers();
        return player3;
    }

    public static void setPlayer3(Player player3) {
        Config.player3 = player3;
    }

    public static Player getPlayer4() {
        setPlayers();
        return player4;
    }

    public static void setPlayer4(Player player4) {
        Config.player4 = player4;
    }
}
