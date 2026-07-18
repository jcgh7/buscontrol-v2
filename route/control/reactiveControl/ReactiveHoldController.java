package route.control.reactiveControl;

import java.util.ArrayList;

import route.Route;
import route.control.HeadwayState;
import route.control.interfaces.HoldController;
import route.entity.Bus;

public class ReactiveHoldController implements HoldController{
    public int holdThresholdMin = 5;
    public int holdAmountMin = 3;

    public ReactiveHoldController(int holdThresholdMin, int holdAmountMin){
        this.holdAmountMin = holdAmountMin;
        this.holdThresholdMin = holdThresholdMin;
    }

    public int getExtraDwellTimeTicks(Bus b, Route r){
        HeadwayState hs = new HeadwayState(r);
        ArrayList<Bus> sortedBuses = hs.sortedBuses;
        if(sortedBuses.size() < 2){
            return 0;
        }
        for(int i = 0; i < sortedBuses.size()-1; i++){
            Bus bus = sortedBuses.get(i);
            if(b.getRun() == bus.getRun()){
                if(Math.round(hs.getOrderedTickHeadways().get(sortedBuses.indexOf(b))/60) <= holdThresholdMin){
                    return holdAmountMin*60;
                }
            }
        }
        return 0;
    }
}
