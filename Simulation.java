import java.util.ArrayList;
import java.util.Random;

import route.Route;
import route.control.interfaces.ExpressController;
import route.control.interfaces.HoldController;
import route.control.interfaces.Terminal;
import route.entity.Stop;
import telemetry.HeadwayStateCollector;
import telemetry.PassengerCollector;

public class Simulation {
    private HeadwayStateCollector headwayStateCollector = new HeadwayStateCollector();
    private PassengerCollector passengerCollector = new PassengerCollector();

    private Random rng;
    private int numTicksToRunFor;

    private Route route;

    public Simulation(long seed, int numTicksToRunFor, Terminal terminal, HoldController holdController, ExpressController expressController, int numStops, double minStopDistance, double maxStopDistance, double minPassengerGenPerTick, double maxPassengerGenPerTick){
        // addressing parameters in order
        rng = new Random(seed); // init rng
        this.numTicksToRunFor = numTicksToRunFor; // init sim ticks
        // - terminal is passed directly to route
        // - holdcontroller is passed directly to route
        // - expresscontroller is passed directly to route
        ArrayList<Stop> stops = new ArrayList<>();
        double currentLocation = 0;
        for(int i = 0; i < numStops-1; i++){
            double passengersGeneratedPerTick = minPassengerGenPerTick + rng.nextDouble()*maxPassengerGenPerTick;
            stops.add(new Stop(i, passengersGeneratedPerTick, currentLocation, false));
            currentLocation += minStopDistance + maxStopDistance*rng.nextDouble();
        }
        stops.add(new Stop(numStops-1, 0, currentLocation, true));

        // init the route
        route = new Route(stops, seed, expressController, holdController, passengerCollector, terminal, headwayStateCollector);

        // because i suck at coding and am too lazy to fix, now we have to do this
        for(Stop stop : stops){
            stop.initRoute(route);
        }
    }

    public void run(){
        for(int i = 0; i < numTicksToRunFor; i++){
            route.tick();
        }
    }

    public HeadwayStateCollector getHeadwayStateCollector(){
        return headwayStateCollector;
    }

    public PassengerCollector getPassengerCollector(){
        return passengerCollector;
    }
}
