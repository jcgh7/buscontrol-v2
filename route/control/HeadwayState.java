package route.control;

import java.util.ArrayList;
import java.util.Arrays;

import route.Route;
import route.entity.Bus;
import route.entity.Stop;

public class HeadwayState {
    // stupidest class ever written
    private Integer[] orderedTickHeadwaysArray = new Integer[50];
    private ArrayList<Integer> orderedTickHeadways = new ArrayList<>(); // ordered such that for a Bus in a Route's ArrayList<Bus>, orderedTickHeadways.get(i) corresponds to the headway in front of Bus buses.get(i)
    private boolean hasInfo = true;

    public HeadwayState(Route route){
        for(int i = 0; i < 50; i++){
            orderedTickHeadwaysArray[i] = -1;
        }
        ArrayList<Bus> buses = route.getBuses(); // ordered oldest to newest
        ArrayList<Double> stopPositions = route.getStopLocations();
        ArrayList<Stop> stops = route.getStops();
        if(buses.size() < 2){
            hasInfo = false;
            return;
        }
        for(int i = buses.size()-1; i > 0; i--){
            Bus front = buses.get(i-1);
            Bus back = buses.get(i);
            double headwayTicks = 0;
            double frontLocation = front.getLocation();
            double backLocation = back.getLocation();
            headwayTicks += (frontLocation - backLocation) / back.getDistancePerTick();
            for(int j = 0; j < stopPositions.size(); j++){
                if(stopPositions.get(i) >= backLocation && stopPositions.get(i) <= frontLocation){
                    headwayTicks += stops.get(i).numPassengers()*back.getPassengerBoardTimeTicks();
                }
            }
            orderedTickHeadwaysArray[i] = (int)Math.round(headwayTicks);
        }

    }

    public ArrayList<Integer> getOrderedTickHeadways(){
        return new ArrayList<Integer>(Arrays.asList(orderedTickHeadwaysArray));
    }

    public boolean hasInfo(){
        return hasInfo;
    }
}
