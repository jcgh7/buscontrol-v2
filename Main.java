import java.util.ArrayList;
import java.util.Arrays;

import route.control.defaultControl.DefaultExpressController;
import route.control.defaultControl.DefaultHoldController;
import route.control.defaultControl.DefaultScheduleTerminal;
import route.control.reactiveControl.ReactiveHoldController;
import telemetry.Datapoint;

public class Main {
    public static void main(String args[]){
        // double bestDistributionIntegral = 1000;
        // int bestHoldTime = 0;
        // int bestHoldThreshold = 0;
        // int bestWaitTime = 15;
        // for(int holdTime = 0; holdTime < 5; holdTime++){
        //     for(int holdThreshold = 0; holdThreshold < 5; holdThreshold++){
        //         Datapoint datapoint = scheduleTerminalReactiveParams(holdTime, holdThreshold, 10);
        //         if(datapoint.averageWaitTime < bestWaitTime){
        //             bestDistributionIntegral = datapoint.distributionIntegral;
        //             bestHoldThreshold = holdThreshold;
        //             bestHoldTime = holdTime;
        //             bestWaitTime = datapoint.averageWaitTime;
        //         }
        //     }
        // }
        // System.out.println("Best distributionIntegral: " + bestDistributionIntegral);
        // System.out.println("Best wait time: " + bestWaitTime);
        // System.out.println("Hold for " + bestHoldTime + " min at threshold " + bestHoldThreshold);

        //scheduleTerminalReactiveParams(0, 0, 1000);

        Datapoint d = scheduleTerminalReactiveParams(0, 0, 5);
        for(int count : d.headwayDistribution){
            System.out.println(count);
        }
        System.out.println("Average headway: " + d.averageHeadway);
        System.out.println("Average travel time: " + d.averageTravelTime);
        System.out.println("Average wait time: " + d.averageWaitTime);
    }

    public static Datapoint scheduleTerminalReactiveParams(int holdTimeMin, int holdThresholdMin, int trials){
        // define a tick as a second, and define distance as in meters
        int ticksToRunFor = 54000;

        ArrayList<Integer> schedule = new ArrayList<>(Arrays.asList(0, 600, 1200, 1800, 2400, 3000));
        int numStartingBuses = 7;
        double distancePerTick = 8.94; // 20mph (8.94m/s)

        int numStops = 60;
        double minStopDistance = 152.4; // 500ft
        double maxStopDistance = 228.6; // 750ft

        double minPassengerGenPerTick = 0.0013; // 0.8 pax/10min
        double maxPassengerGenPerTick = 0.013; // 8.0 pax/10min

        ReactiveHoldController holdController = new ReactiveHoldController(holdThresholdMin, holdTimeMin);
        DefaultExpressController defaultExpressController = new DefaultExpressController();

        ArrayList<Integer> averageTotalCounts = new ArrayList<Integer>();

        int totalPassengerWaitTime = 0;
        int totalPassengerTravelTime = 0;
        double totalDistributionIntegral = 0.0;
        int totalAverageHeadway = 0;

        for(int i = 0; i < 30; i++){
            averageTotalCounts.add(0);
        }

        for(int i = 0; i < trials; i++){
            //System.out.println(Math.round(i*100/trials) + "% complete");
            DefaultScheduleTerminal terminal = new DefaultScheduleTerminal(schedule, numStartingBuses, distancePerTick);
            Simulation simulation = new Simulation((long)i, ticksToRunFor, terminal, holdController, defaultExpressController, numStops, minStopDistance, maxStopDistance, minPassengerGenPerTick, maxPassengerGenPerTick);
            System.out.println("----------NEW SIMULATION----------");
            simulation.run();

            // data parsing
            ArrayList<Integer> headwayCounts = simulation.getHeadwayStateCollector().getAllCounts();
            for(int headway = 0; headway < 30; headway++){
                averageTotalCounts.set(headway, averageTotalCounts.get(headway) + headwayCounts.get(headway));
            }

            totalPassengerWaitTime += simulation.getPassengerCollector().getAverageWaitTime();
            totalPassengerTravelTime += simulation.getPassengerCollector().getAverageTravelTime();
            totalDistributionIntegral += simulation.getHeadwayStateCollector().distributionIntegral();
            totalAverageHeadway += simulation.getHeadwayStateCollector().getAverageHeadway();
            //System.out.println(terminal.getNumDispatchedBuses());
        }

        for(int i = 0; i < 30; i++){
            averageTotalCounts.set(i, averageTotalCounts.get(i)/trials);
        }

        totalPassengerTravelTime/=trials;
        totalPassengerWaitTime/=trials;
        totalPassengerTravelTime/=60;
        totalPassengerWaitTime/=60;
        totalDistributionIntegral/=trials;
        totalAverageHeadway/=trials;
        totalAverageHeadway/=60;

        return new Datapoint(averageTotalCounts, totalPassengerWaitTime, totalPassengerTravelTime, totalDistributionIntegral, totalAverageHeadway);
        
    }
}
