package telemetry;

import java.util.ArrayList;

public class Datapoint {
    public ArrayList<Integer> headwayDistribution = new ArrayList<>();
    public int averageWaitTime;
    public int averageTravelTime;
    public double distributionIntegral;
    public int averageHeadway;

    public Datapoint(ArrayList<Integer> headwayDistribution, int averageWaitTime, int averageTravelTime, double distributionIntegral, int averageHeadway){
        this.headwayDistribution = headwayDistribution;
        this.averageTravelTime = averageTravelTime;
        this.averageWaitTime = averageWaitTime;
        this.distributionIntegral = distributionIntegral;
        this.averageHeadway = averageHeadway;
    }
}
