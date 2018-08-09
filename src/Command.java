/**
 * Created by ofeke on 8/5/2018.
 */
public enum Command {
    SETANCHORS, GETFINALSCORE, GETGAMESTATE, GETSCORE, PREGAME, GETREFFERYS, TELESTATE, STARTGAME, FINISHGAME, GETTEAMRANKS,REMOVETEAM, AUTO, ROBOTBATTERY, TELE, ENTERTEAM, POSTGAME,ENABLE, DISABLE, EXIT, HELP, ROBOTCONNECTION, GETANCHORSTATE;

    public final static int length = 22;

    public static Command toEnum(int str){
        switch (str){
            case 0:
                return HELP;
            case 1:
                return EXIT;
            case 2:
                return POSTGAME;
            case 3:
                return ENTERTEAM;
            case 4:
                return ENABLE;
            case 5:
                return DISABLE;
            case 6:
                return FINISHGAME;
            case 7:
                return STARTGAME;
            case 8:
                return REMOVETEAM;
            case 9:
                return GETTEAMRANKS;
            case 10:
                return ROBOTCONNECTION;
            case 11:
                return GETANCHORSTATE;
            case 12:
                return AUTO;
            case 13:
                return TELE;
            case 14:
                return ROBOTBATTERY;
            case 15:
                return PREGAME;
            case 16:
                return TELESTATE;
            case 17:
                return GETREFFERYS;
            case 18:
                return GETSCORE;
            case 19:
                return GETGAMESTATE;
            case 20:
                return GETFINALSCORE;
            case 21:
                return SETANCHORS;
        }
        return HELP;
    }

    @Override
    public String toString(){
        switch (this){
            case HELP:
                return "help";
            case EXIT:
                return "exit";
            case POSTGAME:
                return "postgame";
            case ENTERTEAM:
                return "enterteam";
            case ENABLE:
                return "enable";
            case DISABLE:
                return "disable";
            case FINISHGAME:
                return "finishgame";
            case STARTGAME:
                return "startgame";
            case REMOVETEAM:
                return "removeteam";
            case GETTEAMRANKS:
                return "getteamranks";
            case GETGAMESTATE:
                return "getgamestate";
            case ROBOTCONNECTION:
                return "robotconnection";
            case GETANCHORSTATE:
                return "getanchorstate";
            case AUTO:
                return "auto";
            case TELE:
                return "tele";
            case ROBOTBATTERY:
                return "robtobattery";
            case PREGAME:
                return "pregame";
            case TELESTATE:
                return "telestate";
            case GETREFFERYS:
                return "getrefferys";
            case GETSCORE:
                return "getscore";
            case GETFINALSCORE:
                return "getfinalscore";
            case SETANCHORS:
                return "setanchors";
        }
        return "NONE";
    }
}
