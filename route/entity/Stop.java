package route.entity;

import java.util.ArrayList;

import route.Route;

public class Stop {
    private final int id;

    private ArrayList<Passenger> passengers = new ArrayList<>();
    private double passengersGeneratedPerTick;
    private double passengerGenerationCounter = 0;

    private final double location;
    private final boolean isTerminal;

    private Route route;

    public Stop(int id, double passengersGeneratedPerTick, double location, boolean isTerminal){
        this.id = id; // note: stop ids must be in ascending order, in increments of one
        this.passengersGeneratedPerTick = passengersGeneratedPerTick;
        this.location = location;
        this.isTerminal = isTerminal;
    }

    public Stop(Stop s){
        this.id = s.getId();
        this.passengersGeneratedPerTick = s.getPassengersGeneratedPerTick();
        this.passengerGenerationCounter = s.getPassengerGenerationCounter();
        this.location = s.getLocation();
        this.isTerminal = s.isTerminal();
        for(Passenger p : s.getPassengers()){
            this.passengers.add(new Passenger(p));
        }
    }

    public double getPassengerGenerationCounter(){
        return passengerGenerationCounter;
    }

    public double getPassengersGeneratedPerTick(){
        return passengersGeneratedPerTick;
    }

    public void initRoute(Route r){
        this.route = r;
    }

    public int getId(){
        return id;
    }

    public boolean isTerminal(){
        return isTerminal;
    }

    public double getLocation(){
        return location;
    }

    public int numPassengers(){
        return passengers.size();
    }

    /**
     * 
     * @param p Passenger to add to this Stop's waiting Passengers
     */
    public void addPassenger(Passenger p){
        passengers.add(p);
    }

    /**
     * 
     * @param nextServedStopID The ID of the next Stop the Bus is serving 
     * 
     * @return A List of Passengers to board onto the Bus at this Stop; calling this method also removes those Passengers from the list
     */
    public ArrayList<Passenger> getPassengersForBoarding(int nextServedStopID){
        ArrayList<Passenger> toBoard = new ArrayList<>();
        passengers.removeIf(p -> {
            if(p.getDestinationStopId() >= nextServedStopID){
                toBoard.add(p);
                p.setOnBus(true);
                return true;
            }
            return false;
        });
        return toBoard;
    }

    public ArrayList<Passenger> getPassengers(){
        return passengers;
    }

    public void tick(){
        for(Passenger p : passengers){
            p.tick();
        }
        passengerGenerationCounter+=passengersGeneratedPerTick;
        if(passengerGenerationCounter>=1){
            passengerGenerationCounter--;
            addPassenger(new Passenger(route.getRandomStopAfterId(id)));
        }
    }
}
