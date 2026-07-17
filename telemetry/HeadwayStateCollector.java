package telemetry;

import java.util.ArrayList;

import route.control.HeadwayState;

public class HeadwayStateCollector {
    // second dumbest class ever written
    private int minHeadwayTicks = 100;
    private int maxHeadwayTicks = -100;
    private int totalHeadwayTicks = 0;
    private int headwaysCollected = 0;

    private ArrayList<Integer> counts = new ArrayList<>();
    private ArrayList<Integer> allCounts = new ArrayList<>();
    
    public HeadwayStateCollector(){
        for(int i = 0; i < 30; i++){
            counts.add(0);
            allCounts.add(0);
        }
    }

    public void collect(HeadwayState hs){
        if(!hs.hasInfo()){
            return;
        }
        ArrayList<Integer> orderedTickHeadways = hs.getOrderedTickHeadways();
        for(int headway : orderedTickHeadways){
            if(headway/60 < 30){
                allCounts.set(headway/60, allCounts.get(headway/60) + 1);
            }
            headwaysCollected++;
            if(headway < minHeadwayTicks){
                minHeadwayTicks = headway;
            }
            if(headway > maxHeadwayTicks){
                maxHeadwayTicks = headway;
            }
            totalHeadwayTicks += headway;
        }

        ArrayList<Integer> terminalBasedHeadways = hs.getTerminalArrivalListBasedHeadways();
        for(int headway : terminalBasedHeadways){
            counts.set(headway/60, counts.get(headway/60) + 1);
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

    public ArrayList<Integer> getTerminalHeadwayCounts(){
        return counts;
    }

    public ArrayList<Integer> getAllCounts(){
        return allCounts;
    }
}
