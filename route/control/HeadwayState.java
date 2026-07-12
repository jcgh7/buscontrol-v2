package route.control;

import java.util.ArrayList;

import route.Route;
import route.entity.Bus;
import route.entity.Stop;

public class HeadwayState {
    private ArrayList<Integer> orderedTickHeadways = new ArrayList<>(); // ordered such that for a Bus in a Route's ArrayList<Bus>, orderedTickHeadways.get(i) corresponds to the headway in front of Bus buses.get(i)

    public HeadwayState(Route route){
        ArrayList<Bus> buses = route.getBuses(); // ordered oldest to newest
        ArrayList<Double> stopPositions = route.getStopLocations();
        ArrayList<Stop> stops = route.getStops();
        for(int i = buses.size(); i > 0; i--){
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
            orderedTickHeadways.add(i, (int)Math.round(headwayTicks));
        }

    }

    public ArrayList<Integer> getOrderedTickHeadways(){
        return orderedTickHeadways;
    }
}
