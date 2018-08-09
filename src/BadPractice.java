/**
 * Created by ofeke on 8/8/2018.
 */
public class BadPractice extends Thread {
    private String name;
    private Thread thread;
    private int autoTime;
    private int finishTime;
    private int state;

    public BadPractice(){
        autoTime = 15;
        state = 0;
        finishTime = 210;
        name = "TimerChecker";
    }

    public int getThreadState(){
        return state;
    }

    @Override
    public void run(){
        while(ServerTimer.getInstance().getCurrentServerTime()/1000 < autoTime){}
        state = 1;
        System.out.println("tele state have been started");
        CommandPrompt.init().teleGame();
        while(ServerTimer.getInstance().getCurrentServerTime()/1000 < finishTime){}
        state = 2;
        System.out.println("post game have been started");
        CommandPrompt.init().postGame();
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }
}
