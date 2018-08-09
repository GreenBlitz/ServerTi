
import java.net.Socket;
import java.util.ArrayList;

public class Scorer {

    private int baseline;
    private int value;
    private int total;
    private boolean capped;
    private static Scorer instance;

    private Scorer(){

    }

    public static Scorer init(){
        if(instance == null)
            instance = new Scorer();
        return instance;
    }

    public int getTotalCargoPoints(ArrayList<ArrayList<Cargo>> data) {
        //first iteration - iterated after a strack is done
        total = 0;
        for (int stackNum = 0; stackNum < data.size(); stackNum++) {
            //second iteration - within a stack
            if(data.get(stackNum).size() != 1) {
                for (int stackLevel = 0; stackLevel < data.get(stackNum).size(); stackLevel++) {
                    //start baseline
                    baseline = 0;
                    capped = false;
                    switch(data.get(stackNum).get(stackLevel)) {
                        case Barrel:
                            baseline+=6;
                            break;
                        case Alliance:
                            baseline+=4;
                            break;
                        case Box:
                            baseline+=8;
                            break;
                        case Crate:
                            baseline+=10;
                            break;
                        case Treasure:
                            baseline+=20;
                            capped = true;
                            break;
                    }
                }
                //end baseline
                //start multiplying everything except treasure
                for (int stackLevel = 1; stackLevel < data.get(stackNum).size(); stackLevel++) {
                    switch(data.get(stackNum).get(stackLevel)) {
                        case Barrel:
                            baseline*=1.4;
                            break;
                        case Alliance:
                            baseline*=1.6;
                            break;
                        case Box:
                            baseline*=1.8;
                            break;
                        case Crate:
                            baseline*=2.0;
                            break;
                        case Treasure:
                            break;
                    }
                }
                //end mul
                if(capped) {
                    //treasure
                    int temp = baseline-20;
                    baseline+=temp*2.5;
                }
                //add to total
                total+=baseline;
            }
        }
        return total;
    }

    public int getCargoVaules(ArrayList<ArrayList<Cargo>> data) {
        total = 0;
        for (int stackNum = 0; stackNum < data.size(); stackNum++) {
            if(data.get(stackNum).size() == 1) {
                switch(data.get(stackNum).get(0)) {
                    case Barrel:
                        total+=6;
                        break;
                    case Alliance:
                        total+=4;
                        break;
                    case Box:
                        total+=8;
                        break;
                    case Crate:
                        total+=10;
                        break;
                    case Treasure:
                        total+=20;
                        break;
                }
            }
        }
        return total;
    }

    public int getCannonValues(int data) {
        return data*20;
    }

    public int getAnchorValues(int data) {
        return data*5;
    }

    public int getPenaltyValues(int penalty, int foul) {
        return -((foul*20)+(penalty*5));
    }

    public int getRunTimeCargoValue(int[] cargo){
        int score = 0;
        for(int i = 0; i< cargo.length; i++){
            switch(cargo[i]) {
                case 1:
                    score+=6;
                    break;
                case 0:
                    score+=4;
                    break;
                case 2:
                    score+=8;
                    break;
                case 3:
                    score+=10;
                    break;
                case 4:
                    score+=20;
                    break;
            }
        }
        return score;
    }
}