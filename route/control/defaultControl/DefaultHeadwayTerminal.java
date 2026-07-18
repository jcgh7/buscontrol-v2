package route.control.defaultControl;

import java.util.ArrayList;
import java.util.Optional;

import route.Route;
import route.control.HeadwayState;
import route.control.interfaces.Terminal;
import route.entity.Bus;
import route.entity.Stop;

public class DefaultHeadwayTerminal implements Terminal {
    private ArrayList<Bus> buses = new ArrayList<>();
    private int headwayMin;

    public DefaultHeadwayTerminal(int numStartingBuses, double distancePerTick, int headwayMin) {
        for (int i = 0; i < numStartingBuses; i++) {
            addBusToTerminal(new Bus(i, 0, distancePerTick));
        }
        this.headwayMin = headwayMin;
    }

    public void terminate(Bus b) {
        b.reset();
        buses.add(b);
    }

    public Optional<Bus> getBusForDispatchIfExists(Route r) {
        HeadwayState hs = new HeadwayState(r);
        ArrayList<Bus> sortedBuses = hs.sortedBuses;
        if(sortedBuses.size() == 0){
            return Optional.of(buses.get(0));
        }
        double headway = sortedBuses.get(0).getLocation() / sortedBuses.get(0).getDistancePerTick();
        ArrayList<Stop> stops = r.getStops();
        for (Stop stop : stops) {
            if (stop.getLocation() < sortedBuses.get(0).getLocation()) {
                // TODO refine this
                headway += 20; // untuned value - could go based on actual pax counts but a) im lazy and b)
                               // that is unavailable info irl
                // headwayTicks+=stop.numPassengers()*sortedBuses.get(i).getPassengerBoardTimeTicks()
                // + 12;
                // System.out.println(stop.numPassengers()*sortedBuses.get(i).getPassengerBoardTimeTicks()
                // + 12);
            }
        }
        if(headway >= headwayMin*60 & buses.size() != 0){
            Bus toDispatch = buses.get(0);
            buses.remove(0);
            return Optional.of(toDispatch);
        }

        return Optional.empty();
    }

    public void addBusToTerminal(Bus b) {
        buses.add(b);
    }

    public void removeBusFromTerminal() {
        if (buses.size() != 0) {
            buses.remove(0);
        }
    }

    public void tick() {
    }
}
