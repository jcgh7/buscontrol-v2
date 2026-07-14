package route.control;

import java.util.ArrayList;

import route.Route;
import route.entity.Bus;
import route.entity.Stop;

public class HeadwayState {
    private boolean hasInfo = true;
    private ArrayList<Integer> headways = new ArrayList<>();

    public HeadwayState(Route route){
        ArrayList<Bus> buses = route.getBuses();
        ArrayList<Bus> sortedBuses = new ArrayList<>();
        if(buses.size() < 2){
            hasInfo = false;
            return;
        }
        sortedBuses.add(buses.get(0));
        for(int i = 1; i < buses.size(); i++){
            boolean added = false;
            for(int j = 0; j < sortedBuses.size(); j++){
                if(buses.get(i).getLocation() < sortedBuses.get(j).getLocation()){
                    sortedBuses.add(j, buses.get(i));
                    added = true;
                    break;
                }
            }
            if(!added){
                sortedBuses.add(buses.get(i));
            }
        }

        ArrayList<Double> stopPositions = route.getStopLocations();
        for(int i = 0; i < sortedBuses.size()-1; i++){
            System.out.println(sortedBuses.get(i).getLocation());
            double headwayTicks = 0;
            double distance = sortedBuses.get(i+1).getLocation() - sortedBuses.get(i).getLocation();
            headwayTicks += distance / sortedBuses.get(i).getDistancePerTick();
            for(Double position : stopPositions){
                if(position > sortedBuses.get(i).getLocation() && position < sortedBuses.get(i+1).getLocation()){
                    headwayTicks+=20; // untuned value - could go based on actual pax counts but a) im lazy and b) that is unavailable info irl
                }
            }
            headways.add((int)Math.round(headwayTicks));
        }

    }

    public ArrayList<Integer> getOrderedTickHeadways(){
        return headways;
    }

    public boolean hasInfo(){
        return hasInfo;
    }
}
