package Server;

import Server.Logic.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class ClientHandler implements Runnable {
    private final int id;
    private final Socket socket;
    private final PrintWriter out;
    private final String authCode;

    ClientHandler(int id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream());
        authCode = Hashing.generateHash(String.valueOf((new SecureRandom()).nextInt() + id));
    }

    public String getMessage(Scanner scanner){
        try {
            String[] message = scanner.nextLine().split(" ", 2);
            if (!message[0].equals(authCode)) {
                sendMessage("Auth code is invalid. For security, server will close the connection.");
                kill();
                return Hashing.generateHash("invalid user");
            }
            return message[1];
        } catch (Exception e){
            System.out.println("Clienthandler "+ id +" lost connection with client");
            Server.clientHandlers.remove(this);
            return "null";
        }
    }

    private boolean doesGameExist(){
        for (Game game: Game.games) {
            if(game.isHasCapacity()) return true;
        }
        return false;
    }

    @Override
    public void run() {
        try {
            System.out.println("New ClientHandler is running...");
            sendMessage("Auth code: " + authCode);
            sendMessage("Enter your name");
            Scanner scanner = null;

            try {
                scanner = new Scanner(this.socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (scanner == null) {
                return;
            }

            String ClientName = getMessage(scanner);

            if (!doesGameExist()) {
                int currentGameIndex = newGame(ClientName, scanner);

                sendMessage("ClientHandler is waiting for starting game message!(maximum number of real players)");
                String StartString = getMessage(scanner);

                StartGame(Integer.parseInt(StartString), currentGameIndex);
            } else {
                sendMessage("Do you want to create a new game? Enter yes or no");
                String newReq = getMessage(scanner);

                if (newReq.equals("yes")) {
                    int currentGameIndex = newGame(ClientName, scanner);

                    sendMessage("ClientHandler is waiting for starting game message!(maximum number of real players)");
                    String StartString = getMessage(scanner);

                    StartGame(Integer.parseInt(StartString), currentGameIndex);
                } else {
                    if (newReq.equals("no")) {
                        addToGame(ClientName, scanner);
                    } else {
                        //kill the thread
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Something went wrong with connection on client handler id: " + id);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public void kill() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int newGame(String ClientName, Scanner scanner) {
        try {
            sendMessage("Enter number of players");
            String playersNum = getMessage(scanner);
            int num = Integer.parseInt(playersNum);
            Human human = new Human(new ArrayList<>(), PlayerType.host, ClientName, socket, authCode);
            Server.games.add(new Game(new ArrayList<>(List.of(human)), 0, 2, num));
            return Server.games.size() - 1;
        }catch (Exception e){
            System.out.println("could not make a game(invalid client)");
            return -1;
        }
    }

    public void addToGame(String ClientName, Scanner scanner) {
        List<Game> gamesWithCapacity = new ArrayList<>();
        for (Game g : Server.games) {
            if (g.isHasCapacity()) {
                gamesWithCapacity.add(g);
            }
        }

        sendMessage("Enter a number between 1 and (" + gamesWithCapacity.size() + ") to add you to a running game.");
        String num = getMessage(scanner);
        int number = Integer.parseInt(num);

        Player bot = null;

        for (Player player1: gamesWithCapacity.get(number - 1).getPlayers()) {
            if(player1 instanceof Bot){
                bot = player1;
                break;
            }
        }

        assert bot != null;
        Human player = new Human(bot.getHand(), PlayerType.guest, ClientName, socket, authCode);
        gamesWithCapacity.get(number - 1).getPlayers().add(player);
        gamesWithCapacity.get(number - 1).getPlayers().remove(bot);
        gamesWithCapacity.get(number - 1).broadcast("Player " + player.getPlayerName() + " joined the game.");
        player.sendMessage("You joined the game. your current hand: " + player.getHand());

        Thread t = new Thread(player);
        t.start();
    }

    public List<Bot> CreateBots(int botNum, int GameIndex) {
        List<Bot> ans = new ArrayList<>();
        if (botNum == 0) {
            return null;
        } else {
            for (int i = 0; i < botNum; i++) {
                List<Integer> cards = spreadCards(GameIndex);
                ans.add(new Bot(cards, PlayerType.guest, "Bot" + i));
            }
        }
        return ans;
    }

    public void StartGame(int players, int currentGameIndex) {
        int BotNumber = players - 1;

        for (Bot bot : CreateBots(BotNumber, currentGameIndex)) {
            Server.games.get(currentGameIndex).getPlayers().add(bot);
        }

        for (Player p : Server.games.get(currentGameIndex).getPlayers()) {
            if (p instanceof Human) {
                p.setHand(spreadCards(currentGameIndex));
            }
        }
        Server.games.get(currentGameIndex).setHearts(Server.games.get(currentGameIndex).getPlayers().size());
        new Thread(Server.games.get(currentGameIndex)).start();
    }

    public List<Integer> spreadCards(int GameIndex) {
        List<Integer> cards = new ArrayList<>();
        for (int j = 0; j < Server.games.get(GameIndex).getCurrentLevel(); j++) {
            int k = ThreadLocalRandom.current().nextInt(0, Server.games.get(GameIndex).getAllCardsOut().size() - 1);

            Server.games.get(GameIndex).getAllCardsOut().remove(k);
            cards.add(k);
        }
        return cards;
    }
}
