package Client;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        Config config = new Config("8070","localhost");

        Gson gson = new GsonBuilder().create();
        String string = gson.toJson(config);

        try(FileWriter fw1=new FileWriter("ClientFile")) {
            fw1.write(string);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        Client client = new Client();
        new Thread(client).start();
    }
}
