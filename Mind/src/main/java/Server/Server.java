package Server;

import Server.Logic.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public final static List<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerStatus status = ServerStatus.WAITING;
    public static List<Game> games = new ArrayList<>();
    private InetAddress IP;
    private int port;

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public void init() throws IOException {
        System.out.println("Server is running...");

        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("ServerFile");
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            try {
                String ip = jsonObject.get("IP").toString();
                this.IP=InetAddress.getByName(ip);
            }
            catch (NullPointerException n){
                this.IP=InetAddress.getByName("localhost");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

         jsonParser = new JSONParser();
         reader = new FileReader("ServerFile");
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            try {
                String port = jsonObject.get("port").toString();
                this.port = Integer.parseInt(port);
            }
            catch (NullPointerException n){
                this.port = 8000;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true) {
                System.out.println("Waiting for a connection...");
                Socket socket = serverSocket.accept();

                addNewClientHandler(socket);
                System.out.println("====> " + clientHandlers.size() + " clients has been connected to server!");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewClientHandler(Socket socket) throws IOException{
        ClientHandler clientHandler = new ClientHandler(clientHandlers.size(), socket);
        if (status == ServerStatus.WAITING) {
            System.out.println("New connection accepted!");

            clientHandlers.add(clientHandler);
            new Thread(clientHandler).start();

            if (clientHandlers.size() >= 500) {
                StopAccept();
            }
        }
        else {
            clientHandler.sendMessage("Game is full!");
            clientHandler.kill();
        }

    }

    private void StopAccept() {
        status = ServerStatus.STARTED;
    }

    enum ServerStatus {
        WAITING, STARTED
    }

}
