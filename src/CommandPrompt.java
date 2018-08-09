import java.io.*;
import java.lang.reflect.Executable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ofeke on 8/5/2018.
 */
public class CommandPrompt extends Thread{
    private String name;
    private String[] teams;
    private Thread thread;
    private static CommandPrompt instance;
    private Scanner scan;
    private ServerDS[] ds;
    private boolean working;
    private ServerTimer timer;
    private Scorer scorer;
    private ServerSocket dsSocket;
    private int blueAnchor;
    private int redAnchor;
    private BadPractice timerCheck;

    public static void main(String[] args) throws IOException{
        CommandPrompt prompt = CommandPrompt.init();
        prompt.start();
    }

    private CommandPrompt(){
        timer = ServerTimer.getInstance();
        blueAnchor = 0;
        redAnchor = 0;
        scorer = Scorer.init();
        scan = new Scanner(System.in);
        name = "CommandPrompt";
        working = true;
        teams = new String[4];
        timerCheck = new BadPractice();
        for(int i = 0; i< teams.length; i++)
            teams[i] = "";
    }

    public static CommandPrompt init(){
        if(instance == null){
            instance = new CommandPrompt();
        }
        return instance;
    }

    @Override
    public void run(){
        AppGetter geter = AppGetter.init();
        geter.start();
        String str;

        ds = new ServerDS[4];
        try {
            dsSocket = new ServerSocket(2212);
        }catch(IOException x){}

        for(int i = 0; i < ds.length; i++) {
            ds[i] = ServerDS.init(i, dsSocket);
            ds[i].start();
        }

        ds[0].setRobotState(Events.Auto);
        int robot = -1;

        while(working){
            System.out.println("Enter your command pls!");
            str = scan.next();
            str = str.toUpperCase();
            try {
                switch (Command.valueOf(str)) {
                    case HELP:
                        printHelp();
                        break;
                    case EXIT:
                        System.out.println("exiting program");
                        closeProgram();
                        working = false;
                        break;
                    case POSTGAME:
                        postGame();
                        //TODO
                        break;
                    case ENTERTEAM:
                        enterTeam();
                        break;
                    case ENABLE:
                        System.out.println("Choose robot to Enable from 0 to 3, 0 - Blue 1, 1 - Blue 2, 2 - Red 1, 3 - Red 2");
                        robot = getRobot();
                        if (robot != -1)
                            ds[robot].setRobotState(Events.Enable);
                        break;
                    case DISABLE:
                        System.out.println("Choose robot to Disable from 0 to 3, 0 - Blue 1, 1 - Blue 2, 2 - Red 1, 3 - Red 2");
                        robot = getRobot();
                        if (robot != -1)
                            ds[robot].setRobotState(Events.Disable);
                        break;
                    case FINISHGAME:
                        finishGame();
                        //TODO
                        break;
                    case STARTGAME:
                        startGame();
                        //TODO
                        break;
                    case REMOVETEAM:
                        removeTeam();
                        break;
                    case GETTEAMRANKS:
                        //TODO wtf
                        break;
                    case GETANCHORSTATE:
                        System.out.println("" + redAnchor + " are opened and there are " + blueAnchor + " blue anchors open");
                        //TODO wifi
                        break;
                    case AUTO:
                        System.out.println("Choose robot to start autonomus from 0 to 3, 0 - Blue 1, 1 - Blue 2, 2 - Red 1, 3 - Red 2");
                        robot = getRobot();
                        if (robot != -1)
                            ds[robot].setRobotState(Events.Auto);
                        break;
                    case TELE:
                        System.out.println("Choose robot to start Tele from 0 to 3, 0 - Blue 1, 1 - Blue 2, 2 - Red 1, 3 - Red 2");
                        robot = getRobot();
                        if (robot != -1)
                            ds[robot].setRobotState(Events.Tele);
                        break;
                    case ROBOTBATTERY:
                        System.out.println("Choose robot to get battery from from 0 to 3, 0 - Blue 1, 1 - Blue 2, 2 - Red 1, 3 - Red 2");
                        robot = getRobot();
                        if (robot != -1)
                            System.out.println("Robot number " + robot + " battery is " + ds[robot].getBattery());
                        break;
                    case GETREFFERYS:
                        System.out.println("Number of Refs is " + (AppGetter.init().getNumberRef() - 1));
                        break;
                    case GETSCORE:
                        getScore();
                        break;
                    case ROBOTCONNECTION:
                        System.out.println("Robot Connections");
                        for (int i = 0; i < ds.length; i++)
                            System.out.println("Robot number " + i + " connection is " + ds[i].getRobotCommunication());
                        break;
                    case PREGAME:
                        //preGame();
                        for(int i = 0; i<ds.length; i++)
                            ds[i].setRobotState(Events.PreGame);
                        //TODO
                        break;
                    case GETFINALSCORE:
                        getFinalScore();
                        break;
                    case SETANCHORS:
                        //TODO
                        try {
                            System.out.println("enter the number of the blue anchors raised");
                            blueAnchor = scan.nextInt();
                            System.out.println("enter the number of the red anchors raised");
                            redAnchor = scan.nextInt();
                        } catch (Exception x) {
                            System.out.println("you are dumb!");
                        }
                        break;
                    default:
                        printHelp();
                        break;
                }
            }catch (Throwable x){
                System.out.println("Weeeellllll yoouuu aarrrreee reeeaaallllllyyyyyyyyyyyyyyyyyyyyyy dummmmmmmmmmmmmmmmmme!");
            }
        }
    }

    public void something() throws Exception{
        BufferedWriter writer;
            writer = new BufferedWriter(new FileWriter("finalScore.txt", true));
            //PrintWriter writer = new PrintWriter("finalScore.txt", "UTF-8");
            writer.append("\n Red Piles : ");
            writer.flush();
            ArrayList<ArrayList<Cargo>> redCargo = AppGetter.init().getPilesCargo(true);
            System.out.println("good");
            for (int i = 0; i < redCargo.size(); i++) {
                // System.out.println(redCargo.get(i).toString());
                writer.append("\n pile number " + i + " is " );//+ redCargo.get(i).toString());
                for(int x = 0; x < redCargo.get(i).size();x ++){
                    writer.append(toString(redCargo.get(i).get(x))+", ");
                }
                writer.flush();
            }
            writer.append("\n Blue Piles : ");
            writer.flush();
            redCargo = AppGetter.init().getPilesCargo(false);
            for (int i = 0; i < redCargo.size(); i++) {
                writer.append("\n pile number " + i + " is " );//+ redCargo.get(i).toString());
                for(int x = 0; x < redCargo.get(i).size();x ++){
                    writer.append(toString(redCargo.get(i).get(x))+", ");
                }
                writer.flush();
            }
            writer.close();
    }

    public void getFinalScore() {
        try{
            int score = Scorer.init().getTotalCargoPoints(AppGetter.init().getPilesCargo(false)) + Scorer.init().getCannonValues(AppGetter.init().getCannon(false)) + Scorer.init().getPenaltyValues(AppGetter.init().getPenaltys(false), AppGetter.init().getFouls(false)) + Scorer.init().getAnchorValues(blueAnchor);
            System.out.println("Blue final score is " + score);
            score = Scorer.init().getTotalCargoPoints(AppGetter.init().getPilesCargo(true)) + Scorer.init().getCannonValues(AppGetter.init().getCannon(true)) + Scorer.init().getPenaltyValues(AppGetter.init().getPenaltys(true), AppGetter.init().getFouls(true)) + Scorer.init().getAnchorValues(redAnchor);
            System.out.println("Red final score is " + score);
        }catch (Exception x){
            System.out.println("FUN");
        }
    }

    public String toString(Cargo cargo){
        switch (cargo){
            case Box:
                return "Box";
            case Alliance:
                return "Alliance";
            case Treasure:
                return "Treasure";
            case Crate:
                return "Crate";
            case Barrel:
                return "Barrel";
        }
        return "";
    }

    public void getScore(){
        System.out.println("Which kind of score do you want? allCur, cargo, cannon, anchor, final");
        String type = scan.next().toLowerCase();
        switch (type){
            case "cannon":
                System.out.println("Blue Cannon score is "+scorer.getCannonValues(AppGetter.init().getCannon(false)));
                System.out.println("Red Cannon score is "+scorer.getCannonValues(AppGetter.init().getCannon(true)));
                break;
            case "anchor":
                System.out.println("Blue Anchor score is "+scorer.getAnchorValues(0));//TODO add anchors
                System.out.println("Red Anchor score is "+scorer.getAnchorValues(0));//TODO add anchors
                break;
            case "cargo":
                System.out.println("Blue Cargo score is "+scorer.getRunTimeCargoValue(AppGetter.init().getCargoArray(false)));
                System.out.println("Red Cargo score is "+scorer.getRunTimeCargoValue(AppGetter.init().getCargoArray(true)));
                break;
            case "allcur":
                int blue = scorer.getCannonValues(AppGetter.init().getCannon(false));
                blue += scorer.getAnchorValues(0) + scorer.getRunTimeCargoValue(AppGetter.init().getCargoArray(false));
                System.out.println("Blue total current score is "+blue);
                blue = scorer.getCannonValues(AppGetter.init().getCannon(true));
                blue += scorer.getAnchorValues(0) + scorer.getRunTimeCargoValue(AppGetter.init().getCargoArray(true));
                System.out.println("Red total current score is "+blue);
                break;
            case "final":
                getFinalScore();
                break;
        }
    }

    public void enterTeam(){
        String sure = "false";
        String name = "";
        while(sure != "true") {
            System.out.println("enter the name of the team you want to addd");
            name = scan.next().toUpperCase();
            System.out.println("Are you sure about this name?");
            sure = scan.next().toLowerCase();
        }
        sure = "false";
        int team = 0;
        while(sure != "true"){
            System.out.println("enter the number of the team (0 - 3) 0 red1, 1 red2, 2 blue1, 3 blue2");
            try{
                team = scan.nextInt();
                System.out.println("Are you sure about this number?");
                sure = scan.next().toLowerCase();
            }catch (Exception e){
                System.out.println("well you are retarded");
            }
        }
        teams[team] = name;
    }

    public void removeTeam(){
        String theUserIsStuiped = "false";
        String team;
        while(theUserIsStuiped !="true") {
            System.out.println("which team do you want to replace?");
            team = scan.next().toUpperCase();
            int helper = -1;
            for(int i = 0;i < teams.length; i++){
                if(teams[i].toUpperCase() == team)
                    helper = i;
            }
            if(helper != -1){
                System.out.println("what is the name of the new team");
                teams[helper] = scan.next().toUpperCase();
                theUserIsStuiped = "true";
            }
            else{
                System.out.println("well this is clear now that you are not the most sharp tea spoon in the basket, do you want to quit?");
                theUserIsStuiped = scan.next().toLowerCase();
            }
        }
    }

    public void printHelp(){
        System.out.println("Commands - ");
        for(int i = 0; i < Command.length; i++){
            System.out.println("Commands number "+i+": "+Command.toEnum(i).toString());
        }
    }

    public int getRobot(){
        String robot = scan.next();
        if(robot.equals("0") || robot.equals("1") || robot.equals("2") || robot.equals("3") ){
            return new Integer(robot);
        }
        return -1;
    }

    public void preGame(){
        //TODO
        for(int i = 0; i<ds.length; i++)
            ds[i].setRobotState(Events.PreGame);
        String sure = "false";
        for(int i = 0;i < teams.length;i ++){
            sure = "false";
            while(sure != "true") {
                System.out.println("enter the alliance (b or r) and than the name of the team number " + i);
                teams[i] = scan.next().toUpperCase();
                System.out.println("are you sure about the team name? true or false");
                sure = scan.next().toLowerCase();
                System.out.println(sure);
                if(sure == "sure")
                    break;
            }
        }

    }

    public void closeProgram(){
        working = false;
        for(int i = 0; i< ds.length; i++)
            ds[i].kill();
        AppGetter.init().kill();
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, name);
            thread.start();
        }
    }

    public void finishGame(){
        for(int i = 0;i< ds.length;i++)
            ds[i].setRobotState(Events.FinishGame);
        File file = new File("Score");

        //TODO
    }

    public void teleGame(){
        for(int i = 0; i< ds.length;i++)
            ds[i].setRobotState(Events.Tele);
    }

     public void startGame(){
        timer = ServerTimer.getInstance();
        for(int i = 0;i< ds.length;i++)
            ds[i].setRobotState(Events.Auto);
        timerCheck.start();
        //TODO
    }

    public void postGame(){
        for(int i = 0;i< ds.length;i++)
            ds[i].setRobotState(Events.PostGame);
        //TODO
    }


}
