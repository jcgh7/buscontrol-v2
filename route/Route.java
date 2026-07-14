package route;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import route.control.HeadwayState;
import route.control.interfaces.ExpressController;
import route.control.interfaces.HoldController;
import route.control.interfaces.Terminal;
import route.entity.Bus;
import route.entity.Stop;
import telemetry.HeadwayStateCollector;
import telemetry.PassengerCollector;

public class Route {
    private ArrayList<Stop> stops;
    private ArrayList<Bus> buses = new ArrayList<>();

    private ArrayList<Double> stopPositions = new ArrayList<>();

    private final long seed;
    private Random routeRNG;

    private ExpressController expressController;
    private HoldController holdController;
    private PassengerCollector passengerCollector;
    private HeadwayStateCollector headwayStateCollector;
    private Terminal terminal;

    private int internalTime = 0;

    public Route(ArrayList<Stop> stops, long seed, ExpressController expressController, HoldController holdController, PassengerCollector passengerCollector, Terminal terminal, HeadwayStateCollector headwayStateCollector){
        this.stops = stops;
        this.seed = seed;
        this.routeRNG = new Random(seed);
        this.expressController = expressController;
        this.passengerCollector = passengerCollector;
        this.terminal = terminal;
        this.holdController = holdController;
        this.headwayStateCollector = headwayStateCollector;
        for(Stop s : stops){
            stopPositions.add(s.getLocation());
        }
    }

    public long getSeed(){
        return seed;
    }

    public ArrayList<Double> getStopLocations(){
        return stopPositions;
    }

    public ArrayList<Bus> getBuses(){
        return buses;
    }

    public ArrayList<Stop> getStops(){
        return stops;
    }

    /**
     * 
     * @param id 
     * @return ID of a random Stop after the Stop with the passed in ID
     */
    public int getRandomStopAfterId(int id){
        ArrayList<Stop> possibleStops = new ArrayList<>();
        for(Stop s : stops){
            if(s.getId() > id){
                possibleStops.add(s);
            }
        }
        return possibleStops.get(routeRNG.nextInt(possibleStops.size())).getId();
    }

    public Optional<Stop> getStopFromId(int id){
        for(Stop s : stops){
            if(s.getId() == id){
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    public void tick(){
        internalTime++;
        // Stops just need to be ticked, they do not need anything fancy
        for(Stop s : stops){ 
            if(s.getId()*120 < internalTime){
                s.tick();
            }
        }

        // buses require all of the logic to be done for them
        buses.removeIf(bus -> {
            Optional<Stop> nextStop = getStopFromId(bus.getNextStopId()); 
            if(nextStop.isPresent()){ // check if a Stop with the Bus's nextStopID exists, and if it doesn't stop the program
                if(bus.getLocation() >= nextStop.get().getLocation()){ // if the Bus is at the next Stop
                    Stop currentStop = nextStop.get(); // we are now at that Stop
                    if(!currentStop.isTerminal()){ // if it is not a terminal
                        bus.setNextStopId(expressController.getNextStopIdForBus(bus, this)); // allow the expresscontroller to figure out what the next Stop is
                        bus.boardPassengers(currentStop.getPassengersForBoarding(bus.getNextStopId())); // board Passengers that are going to or after the next served Stop, this adds dwell time automatically
                        passengerCollector.collect(bus.deboardPassengersWithDestinationId(currentStop.getId())); // collect deboarding passengers
                        bus.addDwellTicks(holdController.getExtraDwellTimeTicks(bus, this)); // allow the holdcontroller to add extra time
                    }
                    else{
                        passengerCollector.collect(bus.deboardPassengersWithDestinationId(currentStop.getId())); // collect deboarding passengers
                        terminal.terminate(bus); // hand off the bus to the terminal
                        return true; // remove it from active buses
                    }
                }
            }
            else{
                System.err.println("A Bus had a next Stop ID that did not correspond to a real stop. Quitting..."); // shouldn't be possible, but we want to catch it if it happens
                assert false;
            }
            bus.tick();
            return false;
        });

        // check if the terminal has a bus for us
        Optional<Bus> toDispatch = terminal.getBusForDispatchIfExists(this);
        if(toDispatch.isPresent()){
            buses.add(toDispatch.get());
        }

        terminal.tick();

        headwayStateCollector.collect(new HeadwayState(this)); 
    }
}
