package route.control;

import java.util.ArrayList;

import route.Route;
import route.entity.Bus;
import route.entity.Stop;

public class HeadwayState {
    private boolean hasInfo = true;
    private ArrayList<Integer> headways = new ArrayList<>();
    private ArrayList<Integer> terminalArrivalListBasedHeadways = new ArrayList<>();

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
        ArrayList<Stop> stops = route.getStops();
        for(int i = 0; i < sortedBuses.size()-1; i++){
            double headwayTicks = 0;
            double distance = sortedBuses.get(i+1).getLocation() - sortedBuses.get(i).getLocation();
            headwayTicks += distance / sortedBuses.get(i).getDistancePerTick();
            for(Stop stop : stops){
                if(stop.getLocation() > sortedBuses.get(i).getLocation() && stop.getLocation() < sortedBuses.get(i+1).getLocation()){
                    // TODO refine this
                    //headwayTicks+=25; // untuned value - could go based on actual pax counts but a) im lazy and b) that is unavailable info irl
                    headwayTicks+=stop.numPassengers()*sortedBuses.get(i).getPassengerBoardTimeTicks() + 15;
                }
            }
            headways.add((int)Math.round(headwayTicks));
            // if(headwayTicks < 31){
            //     System.out.println("Logged a headway of " + (int)Math.round(headwayTicks) + " ticks. The bus postions are " + sortedBuses.get(i).getLocation() + " and " + sortedBuses.get(i+1).getLocation());
            // }
        }

        // do the terminal arrival time based headways for fingerprinting
        if(route.getInternalTime()%60!=0){
            return;
        }
        double terminalLocation = stopPositions.get(stopPositions.size()-1);
        for(int i = 0; i < sortedBuses.size()-1; i++){
            // TODO refine this
            double threshold = terminalLocation - 20*60*sortedBuses.get(i).getDistancePerTick(); // once again an arbitrary value, this time 20 minutes away in pure distance (so that arrivals are ~30 min away or less incl passenger boarding time)
            if(sortedBuses.get(i).getLocation() > threshold && sortedBuses.get(i+1).getLocation() > threshold){
                if(headways.get(i) < 30*60){
                    terminalArrivalListBasedHeadways.add(headways.get(i));
                }
            }
        }
    }

    public ArrayList<Integer> getOrderedTickHeadways(){
        return headways;
    }

    public ArrayList<Integer> getTerminalArrivalListBasedHeadways(){
        return terminalArrivalListBasedHeadways;
    }

    public boolean hasInfo(){
        return hasInfo;
    }
}
