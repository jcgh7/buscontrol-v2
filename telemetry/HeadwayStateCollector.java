package telemetry;

import java.util.ArrayList;

import route.control.HeadwayState;

public class HeadwayStateCollector {
    // second dumbest class ever written
    private int minHeadwayTicks = 100;
    private int maxHeadwayTicks = -100;
    private int totalHeadwayTicks = 0;
    private int headwaysCollected = 0;
    
    public HeadwayStateCollector(){}

    public void collect(HeadwayState hs){
        if(!hs.hasInfo()){
            return;
        }
        ArrayList<Integer> orderedTickHeadways = hs.getOrderedTickHeadways();
        for(int headway : orderedTickHeadways){
            headwaysCollected++;
            if(headway < minHeadwayTicks){
                minHeadwayTicks = headway;
            }
            if(headway > maxHeadwayTicks){
                maxHeadwayTicks = headway;
            }
            totalHeadwayTicks += headway;
        }
    }

    public int getMinHeadway(){
        return minHeadwayTicks;
    }

    public int getMaxHeadway(){
        return maxHeadwayTicks;
    }

    public int getAverageHeadway(){
        return totalHeadwayTicks/headwaysCollected;
    }

    public int getHeadwayVariance(){
        return maxHeadwayTicks - minHeadwayTicks;
    }
}
