import java.util.ArrayList;
import java.util.Arrays;

import route.control.defaultControl.DefaultExpressController;
import route.control.defaultControl.DefaultHoldController;
import route.control.defaultControl.DefaultScheduleTerminal;

public class Main {
    public static void main(String args[]){
        // define a tick as a second, and define distance as in meters
        long seed = 1;
        int ticksToRunFor = 42000;

        ArrayList<Integer> schedule = new ArrayList<>(Arrays.asList(0, 600, 1200, 1800, 2400, 3000));
        int numStartingBuses = 10;
        double distancePerTick = 8.94; // 20mph 

        int numStops = 60;
        double minStopDistance = 152.4; // 500ft
        double maxStopDistance = 228.6; // 750ft

        double minPassengerGenPerTick = 0.0013; // 0.8 pax/10min
        double maxPassengerGenPerTick = 0.013; // 8.0 pax/10min

        DefaultScheduleTerminal defaultScheduleTerminal = new DefaultScheduleTerminal(schedule, numStartingBuses, distancePerTick);
        DefaultHoldController defaultHoldController = new DefaultHoldController();
        DefaultExpressController defaultExpressController = new DefaultExpressController();

        Simulation simulation = new Simulation(seed, ticksToRunFor, defaultScheduleTerminal, defaultHoldController, defaultExpressController, numStops, minStopDistance, maxStopDistance, minPassengerGenPerTick, maxPassengerGenPerTick);
        simulation.run();
    }
}
