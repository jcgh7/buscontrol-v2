package route.control.defaultControl;

import java.util.ArrayList;
import java.util.Optional;

import route.Route;
import route.control.interfaces.Terminal;
import route.entity.Bus;

public class DefaultScheduleTerminal implements Terminal {
    private ArrayList<Bus> buses = new ArrayList<>();
    private int time = 0;
    private ArrayList<Integer> schedule;
    
    public DefaultScheduleTerminal(ArrayList<Integer> schedule){
        this.schedule = schedule;
    }

    public void terminate(Bus b){
        b.reset();
        buses.add(b);
    }

    public Optional<Bus> getBusForDispatchIfExists(Route r){
        if(buses.size() != 0 && schedule.indexOf(time) != -1){
            Bus toDispatch = buses.get(0);
            buses.remove(0);
            return Optional.of(toDispatch);
        }

        return Optional.empty();
    }

    public void addBusToTerminal(Bus b){
        buses.add(b);
    }

    public void removeBusFromTerminal(){
        if(buses.size() != 0){
            buses.remove(0);
        }
    }
}
