import com.google.gson.Gson;
import com.sun.security.ntlm.Server;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ofeke on 8/4/2018.
 */
public class ServerDS extends Thread {
    private String name;
    private Thread thread;
    private int id;
    private Events robotState;
    private boolean communication;
    private boolean working;
    private String extra;
    private int battery;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintStream ps;
    private static ServerDS[] instance;
    private String realRobotState;

    private ServerDS(int id){
        this.id = id;
        name = "ServerDriverStation"+id;
        extra = "";
        working = true;
        battery = 0;
        robotState = Events.PreGame;
    }

    public static ServerDS init(int id, ServerSocket serverSocket){
        if(instance == null){
            instance = new ServerDS[4];
            instance[id] = new ServerDS(id);
            instance[id].serverSocket = serverSocket;
        }
        if(instance[id] == null){
            instance[id] = new ServerDS(id);
            instance[id].serverSocket = serverSocket;
        }
        return instance[id];
    }

    public static ServerDS init(int id){
        if(instance == null) {
            return null;
        }
        if(instance[id] == null){
            return null;
        }
        return instance[id];
    }


    public boolean getRobotCommunication(){
        return communication;
    }

    @Override
    public void run(){
        try {
            socket = serverSocket.accept();
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(output);
            Scanner scanner = new Scanner(input);
            JSONObject j;
            String s;

            while(!scanner.hasNext()){}

            while (working){

                if(scanner.hasNext()){
                    try {
                        s = scanner.next();
                        //System.out.println(s);
                        j = new JSONObject(s);
                     //   System.out.println();
                        battery = j.getInt("Battery");
                        communication = j.getBoolean("Connection");

                    }catch (JSONException x){
                        x.printStackTrace();
                    }
                }

                ps.println(DataDS.JSON_DSDATA.toJson(getMessage()));
                try {
                    Thread.sleep(100);
                }catch (InterruptedException x){
                    x.printStackTrace();
                }
            }

            scanner.close();
            ps.close();
            input.close();
            output.close();
            socket.close();
        }catch (IOException x){
            x.printStackTrace();
        }

    }

    public DataDS.Data getMessage(){
        DataDS.Data d = new DataDS.Data(id, robotState, extra);
        return d;
    }

    public void start(){
        if(thread == null){
            thread =new Thread(this, name);
            thread.start();
        }
    }

    public void setExtra(String str){
        extra = str;
    }

    public int getBattery(){
        return battery;
    }

    public void kill(){
        working = false;
        extra = "kill";
        try{
            ps.println(DataDS.JSON_DSDATA.toJson(getMessage()));
        }catch (Exception e){
            //e.printStackTrace();
        }
    }

    public void setRobotState(Events s){
        robotState = s;
    }

    public Events getRobotState(){
        return robotState;
    }
    public int getID(){
        return id;
    }
}
