import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by ofeke on 8/7/2018.
 */
public class AppGetter extends Thread {
    private String name;
    private static AppGetter instance;
    private Thread thread;
    private int time;
    private ArrayList<AppServer> apps;
    private ServerSocket serverSocket;
    private int port;
    private boolean initPiles;
    private boolean working;
    private int count;
    private ArrayList<ArrayList<Integer>> bluePiles;
    private ArrayList<ArrayList<Integer>> redPiles;
    private int[] blueCargo;
    private int[] redCargo;
    private ArrayList<Boolean> gotPile;
    private int redCannon;
    private int blueCannon;
    private int blueFouls;
    private int redFouls;
    private int bluePenaltys;
    private int redPenaltys;
    private ArrayList<ArrayList<ArrayList<Integer>>> redPileStorage;
    private ArrayList<ArrayList<ArrayList<Integer>>> bluePileStorage;
    private boolean havePiles;
    private int pileSended;
    private boolean debbuging = true;

    private AppGetter() {
        gotPile = new ArrayList<Boolean>();
        redPileStorage = new ArrayList<ArrayList<ArrayList<Integer>>>();
        bluePileStorage = new ArrayList<ArrayList<ArrayList<Integer>>>();
        havePiles = false;
        redFouls = 0;
        redCannon = 0;
        redPenaltys = 0;
        blueFouls = 0;
        bluePenaltys = 0;
        blueCannon = 0;
        working = true;
        port = 8768;
        gotPile = new ArrayList<Boolean>();
        count = 0;
        initPiles = false;
        time = 0;
        bluePiles = new ArrayList<ArrayList<Integer>>();
        pileSended = 0;
        redPiles = new ArrayList<ArrayList<Integer>>();
        name = "AppGetter";
        apps = new ArrayList<AppServer>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException x) {
            x.printStackTrace();
        }
        int cargoNumber = 5;
        redCargo = new int[cargoNumber];
        blueCargo = new int[cargoNumber];
        for(int i = 0;i < cargoNumber; i++){
            redCargo[i] = 0;
            blueCargo[i] = 0;
        }
    }

    public int getNumberRef(){
        return apps.size();
    }

    public static AppGetter init() {
        if (instance == null) {
            instance = new AppGetter();
        }
        return instance;
    }

    @Override
    public void run(){
        print("app getter is starting");
        count = 0;
        ArrayList<ArrayList<Integer>> helper;
        apps.add(new AppServer(serverSocket, 0));
        apps.get(0).start();
        print("app getter have started the first server socket");

        while(working){

            if(apps.get(apps.size()-1).isAccepted()) {
                gotPile.add(false);
                apps.add(new AppServer(serverSocket, apps.size()));
                apps.get(apps.size()-1).start();
                print("another app have been added");
            }

            time = getTime();
            for(int i = 0; i< apps.size(); i++)
                apps.get(i).setTime(time);

            try {
                Thread.sleep(10);
            }catch(InterruptedException x){
                x.printStackTrace();
            }
           // print("done loop");
        }
        try{
            for(int i = 0; i< apps.size();i++)
                apps.get(i).kill();
            while(!appsAreDead()){}
            serverSocket.close();
            print("app getter is dead");
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    public boolean doesHavePiles(){
        if(apps.size() < 2)
            return false;
        for(int i = 0;i < apps.size() -1; i++){
            if(!apps.get(i).hasPiles())
                return false;
        }
        return true;
    }

    public boolean isPilesInit(){
        if(apps.size() == 0)
            initPiles = false;
        return initPiles;
    }

    public int[] getCargoArray(boolean isRed){
        if(isRed)
            return redCargo;
        return blueCargo;
    }

    public void addCargo(int type, boolean added, boolean isRed){
        if(added){
            if (isRed)
                redCargo[type] ++;
            else
                blueCargo[type] ++;
        }
        else{
            if (isRed)
                redCargo[type] --;
            else
                blueCargo[type] --;
        }
    }

    public int getCargo(boolean isRed, Cargo type){
        int[] cargo;
        if(isRed)
            cargo = redCargo;
        else
            cargo = blueCargo;
        int i = -1;
        switch (type){
            case Alliance:
                i = 0;
                break;
            case Barrel:
                i = 1;
                break;
            case Box:
                i = 2;
                break;
            case Crate:
                i = 3;
                break;
            case Treasure:
                i = 4;
                break;
        }
        if(i == -1)
            return 0;
        return cargo[i];
    }

    public int getTime(){
        return (int)(ServerTimer.getInstance().getCurrentServerTime()/1000);
    }

    public void kill(){
        working = false;
    }

    public boolean appsAreDead(){
        for(int i = 0 ;i< apps.size();i++)
            if(!apps.get(i).isDead())
                return false;
        return true;
    }

    public boolean isDebbuging(){
        debbuging =false;
        return debbuging;
    }

    public void start() {
        if (thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }

    public void setPiles(){
        //System.out.println("Starting piles");
      //  System.out.println(redPileStorage.size()+"   sidjvsfdijfd "+ bluePileStorage.size());
        ArrayList<Integer> helper;
        for(int i = 0; i < redPileStorage.size(); i++){
            for(int x = 0; x < redPileStorage.get(i).size(); x++){
                helper = new ArrayList<Integer>();
                for(int y = 0; y < redPileStorage.get(i).get(x).size(); y++){
                    helper.add(redPileStorage.get(i).get(x).get(y));
                }
                redPiles.add(helper);
            }
        }
        for(int i = 0; i < bluePileStorage.size(); i++){
            for(int x = 0; x < bluePileStorage.get(i).size(); x++){
                helper = new ArrayList<Integer>();
                for(int y = 0; y < bluePileStorage.get(i).get(x).size(); y++){
                    helper.add(bluePileStorage.get(i).get(x).get(y));
                }
                bluePiles.add(helper);
            }
        }
        initPiles =true;
       // System.out.println("finish setting piles");
    }

    public ArrayList<ArrayList<Cargo>> getPilesCargo(boolean isRed){
        if (doesHavePiles()&& initPiles){
            ArrayList<ArrayList<Cargo>> piles = new ArrayList<ArrayList<Cargo>>();
            ArrayList<ArrayList<Integer>> curPile = bluePiles;
            if(isRed)
                curPile = redPiles;
         //   System.out.println("WTF"+redPiles.size());
            ArrayList<Cargo> helper;
            for(int i = 0; i < curPile.size(); i++){
                print("fuck");
                helper = new ArrayList<Cargo>();
                for(int x = 0; x< curPile.get(i).size(); x++){
                    print("real shit");
                    switch (curPile.get(i).get(x)){
                        case 0:
                            helper.add(Cargo.Alliance);
                            break;
                        case 1:
                            helper.add(Cargo.Barrel);
                            break;
                        case 2:
                            helper.add(Cargo.Box);
                            break;
                        case 3:
                            helper.add(Cargo.Crate);
                            break;
                        case 4:
                            helper.add(Cargo.Treasure);
                            break;
                    }
                }
                piles.add(helper);
            }
            return piles;
        }
        return null;
    }

    public <T> void print(T t){
        debbuging =false;
        if(debbuging)
            System.out.println(t);
    }

    public void addPileToStorage(ArrayList<ArrayList<Integer>> arr, boolean isRed){
        if (isRed)
            redPileStorage.add(arr);
        else
            bluePileStorage.add(arr);
        print("piles are here in the storage");
    }

    public void addCannon(boolean isRed){
        if(isRed && redCannon < 2)
            redCannon ++;
        else if(!isRed && blueCannon < 2)
            blueCannon ++;
    }

    public int getCannon(boolean isRed){
        if(isRed)
            return redCannon;
        return blueCannon;
    }

    public void addFoul(boolean isRed){
        if(isRed)
            redFouls++;
        else
            blueFouls++;
    }

    public void removeFoul(boolean isRed){
        if(isRed)
            redFouls --;
        else
            blueFouls --;
    }

    public int getFouls(boolean isRed){
        if(isRed)
            return redFouls;
        return blueFouls;
    }

    public void addPenaltys(boolean isRed){
        if(isRed)
            redPenaltys++;
        else
            bluePenaltys++;
    }

    public void removePenaltys(boolean isRed){
        if(isRed)
            redPenaltys --;
        else
            bluePenaltys --;
    }

    public int getPenaltys(boolean isRed){
        if(isRed)
            return redPenaltys;
        return bluePenaltys;
    }

    public boolean getAnchor1(){
        //TODO
        return false;
    }

    public boolean getAnchor2(){
        //TODO
        return false;
    }
}
