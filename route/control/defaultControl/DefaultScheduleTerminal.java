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
    
    public DefaultScheduleTerminal(ArrayList<Integer> schedule, int numStartingBuses, double distancePerTick){
        this.schedule = schedule;
        for(int i = 0; i < numStartingBuses; i++){
            addBusToTerminal(new Bus(i, 0, distancePerTick));
        }
    }

    public DefaultScheduleTerminal(DefaultScheduleTerminal dst){
        this.time = dst.time;
        this.schedule = dst.schedule;
        for(Bus b : dst.buses){
            this.buses.add(new Bus(b));
        }
    }

    public void terminate(Bus b){
        b.reset();
        buses.add(b);
    }

    public Optional<Bus> getBusForDispatchIfExists(Route r){
        if(buses.size() != 0 && schedule.indexOf(time) != -1){
            //System.out.println("[DefaultScheduleTerminal]: Dispatching bus " + buses.get(0).getRun() + " at " + time);
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

    public void tick(){
        time++;
        if(time == 3600){
            time = 0;
        }
    }
}
