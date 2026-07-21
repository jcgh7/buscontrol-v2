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
            if((int)Math.round(headway/60.0) < 30){
                allCounts.set((int)Math.round(headway/60.0), allCounts.get((int)Math.round(headway/60.0)) + 1);
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

    public double distributionIntegral(){
        ArrayList<Integer> data = new ArrayList<>(allCounts);
        ArrayList<Double> redistributed = new ArrayList<>(30);
        for(int i = 0; i < 30; i++){
            redistributed.add(data.get(i)/(double)(30-i));
        }
        double max = 0.0;
        for(double value : redistributed){
            if(value > max){
                max = value;
            }
        }
        ArrayList<Double> normalized = new ArrayList<>(30);
        double total = 0;
        for(double value : redistributed){
            normalized.add(value/max);
            total += value/max;
        }
        return total;
    }
}
