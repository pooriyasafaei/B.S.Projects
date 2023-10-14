package Server;

import Client.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class ServerMain {
    public ServerMain(){
    }

    public static void main(String[] args) throws IOException {
        Client.Config config = new Config("8070","localhost");

        Gson gson = new GsonBuilder().create();
        String string=gson.toJson(config);

        try(FileWriter fw1=new FileWriter("ServerFile")){
            fw1.write(string);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        Server server=new Server();
        server.init();
    }
}
