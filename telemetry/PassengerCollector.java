package telemetry;

import java.util.ArrayList;

import route.entity.Passenger;

public class PassengerCollector {
    private int totalTicksSpentTraveling = 0;
    private int totalTicksSpentWaiting = 0;
    private int totalPassengersCollected = 0;
    private int maxWaitTime = -100;
    private int minWaitTime = 100;
    private int maxTravelTime = -100;
    private int minTravelTime = 100;

    public PassengerCollector(){}

    public void collect(ArrayList<Passenger> passengers){
        for(Passenger p : passengers){
            totalTicksSpentTraveling += p.getTicksSpentTraveling();
            totalTicksSpentWaiting += p.getTicksSpentWaiting();
            totalPassengersCollected++;
            if(p.getTicksSpentTraveling() > maxTravelTime){
                maxTravelTime = p.getTicksSpentTraveling();
            }
            if(p.getTicksSpentTraveling() < minTravelTime){
                minTravelTime = p.getTicksSpentTraveling();
            }
            if(p.getTicksSpentWaiting() > maxWaitTime){
                maxWaitTime = p.getTicksSpentWaiting();
            }
            if(p.getTicksSpentWaiting() < minWaitTime){
                minWaitTime = p.getTicksSpentWaiting();
            }
        }
    }

    public int getAverageTravelTime(){
        return (int)Math.round(((double)totalTicksSpentTraveling)/totalPassengersCollected);
    }

    public int getAverageWaitTime(){
        return (int)Math.round(((double)totalTicksSpentWaiting)/totalPassengersCollected);
    }

    public int getMaxWaitTime(){
        return maxWaitTime;
    }

    public int getMaxTravelTime(){
        return maxTravelTime;
    }

    public int getMinWaitTime(){
        return minWaitTime;
    }

    public int getMinTravelTime(){
        return minTravelTime;
    }
}
