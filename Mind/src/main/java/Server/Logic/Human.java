package Server.Logic;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Human extends Player implements Runnable{
    private Socket socket;
    private final String authCode;
    private final PrintWriter out;

    private final String[] validEmojis = new String[]{":(", ":)", ":D", "D:", ":/", ":|", ">:(", ":O", ":')",
            ":'(",":{"};

    public Human(List<Integer> hand, PlayerType playerType, String playerName, Socket socket, String authCode) {
        super(hand, playerType, playerName);
        PrintWriter out1;
        this.socket=socket;
        this.authCode = authCode;
        try {
            out1 = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            out1 = null;
        }
        this.out = out1;
    }

    public void play(boolean all){
        int i;
        if(all){
            int size = this.hand.size();
            for (int j = 0; j < size; j++) {
                i=min(this.hand);
                this.hand.remove((Object)i);

                memberOf(this).setBurnt(i);
                print();
            }
        }
        else {
            if(getHand().size() != 0) {
                i = min(this.hand);
                this.hand.remove((Object) i);
                memberOf(this).setBurnt(i);
                print();

                memberOf(this).punish(i);
            } else {
                sendMessage("You have no cards in your hand to burn!");
            }
        }
    }

    @Override
    public void run() {
        try {
            while (!hand.isEmpty()) {
                sendMessage("Enter play to play lowest card or enter an emoji or write 'ninja' to play a star card:");
                    Scanner scanner = new Scanner(socket.getInputStream());
                    String s = getMessage(scanner);

                    if(s.equalsIgnoreCase("ninja")){
                        memberOf(this).broadcast(playerName + " attempts to use a Ninja card...");
                        memberOf(this).useNinja();
                        continue;
                    }

                    if (s.equalsIgnoreCase("play")) {
                        play(false);
                    } else {
                        if (Arrays.asList(validEmojis).contains(s)) {
                            this.memberOf(this).broadcast(this.playerName + " reacted " + s);
                        }
                    }
                }
        }catch (Exception e) {
            //TODO
            System.out.println("Connection lost with client " + playerName);
            Game game = memberOf(this);
            if(game != null){
                game.getPlayers().remove(this);
                game.broadcast("player " + playerName + " left the game.(replaced with a bot)");
                Bot bot = new Bot(hand, PlayerType.guest, playerName + "(Bot)");
                game.getPlayers().add(bot);

                Thread t = new Thread(bot);
                t.start();
            }
            // we should replace player with a bot
        }
    }

    private String getMessage(Scanner scanner){
        String[] message = scanner.nextLine().split(" ", 2);
        if(!message[0].equals(authCode)){
            sendMessage("Auth code is invalid. For security, server will close the connection.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Hashing.generateHash("invalid user");
        }
        return message[1];
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public Socket getSocket() {
        return socket;
    }
}
