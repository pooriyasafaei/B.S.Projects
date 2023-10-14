package Client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable{

    private InetAddress IP;
    private int port;
    private String authCode = "0";
    private Socket socket;

    private List<Thread> threads = new LinkedList<>();

    private void init() throws IOException {

        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("ClientFile");
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
         reader = new FileReader("ClientFile");
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

        socket = new Socket(this.IP, this.port);

        Thread t1 = new Thread(this::receiver);
        Thread t2 = new Thread(this::sender);
        threads.add(t1);
        threads.add(t2);

        t1.start();
        t2.start();
    }

    public void sender(){
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
       try {
            while (true) {
                Scanner scanner1 = new Scanner(System.in);
                String message = scanner1.nextLine();
                assert printWriter != null;
                sendMessage(message, printWriter);
            }
        }catch (Exception e){
           System.out.println("socket is closed");
           System.exit(2);
       }
    }

    public void receiver(){
        Scanner scanner = null;
        try {
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while (true) {
                assert scanner != null;
                String input = scanner.nextLine();
                if (input.toLowerCase().contains("auth code: ")) {
                    authCode = input.substring(11);
                }
                System.out.println("Message from server: " + input);
            }
        }catch (Exception e){
            System.out.println("socket is closed");
            System.exit(2);
        }
    }

    public void sendMessage(String message, PrintWriter out) {
        out.println(authCode + " " + message);
        out.flush();
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
