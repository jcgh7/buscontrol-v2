import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import route.control.defaultControl.DefaultExpressController;
import route.control.defaultControl.DefaultHoldController;
import route.control.defaultControl.DefaultScheduleTerminal;
import route.control.reactiveControl.ReactiveHoldController;

public class Main {
    public static void main(String args[]){
        // define a tick as a second, and define distance as in meters
        long seed = 3;
        int ticksToRunFor = 54000;

        ArrayList<Integer> schedule = new ArrayList<>(Arrays.asList(0, 600, 1200, 1800, 2400, 3000));
        int numStartingBuses = 10;
        double distancePerTick = 8.94; // 20mph (8.94)

        int numStops = 60;
        double minStopDistance = 152.4; // 500ft
        double maxStopDistance = 228.6; // 750ft

        double minPassengerGenPerTick = 0.0013; // 0.8 pax/10min
        double maxPassengerGenPerTick = 0.013; // 8.0 pax/10min

        DefaultHoldController holdController = new DefaultHoldController();
        DefaultExpressController defaultExpressController = new DefaultExpressController();

        LinkedList<Integer> averageTotalCounts = new LinkedList<Integer>();
        int trials = 10;

        for(int i = 0; i < 30; i++){
            averageTotalCounts.add(0);
        }

        for(int i = 0; i < trials; i++){
            System.out.println(Math.round(i*100/trials) + "% complete");
            DefaultScheduleTerminal defaultScheduleTerminal = new DefaultScheduleTerminal(schedule, numStartingBuses, distancePerTick);
            Simulation simulation = new Simulation((long)i, ticksToRunFor, defaultScheduleTerminal, holdController, defaultExpressController, numStops, minStopDistance, maxStopDistance, minPassengerGenPerTick, maxPassengerGenPerTick);
            simulation.run();

            // data parsing
            ArrayList<Integer> terminalBasedHeadwayCounts = simulation.getHeadwayStateCollector().getAllCounts();
            for(int headway = 0; headway < 30; headway++){
                averageTotalCounts.set(headway, averageTotalCounts.get(headway) + terminalBasedHeadwayCounts.get(headway));
            }
        }

        for(int i = 0; i < 30; i++){
            averageTotalCounts.set(i, averageTotalCounts.get(i)/trials);
            System.out.println(averageTotalCounts.get(i));
        }
    }
}
