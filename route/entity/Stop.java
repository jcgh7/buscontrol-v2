package route.entity;

import java.util.ArrayList;

import route.Route;

public class Stop {
    private final int id;

    private ArrayList<Passenger> passengers = new ArrayList<>();
    private double passengersGeneratedPerTick;
    private double passengerGenerationCounter = 0;

    private Route route;

    public Stop(Route route, int id, double passengersGeneratedPerTick){
        this.id = id;
        this.passengersGeneratedPerTick = passengersGeneratedPerTick;
        this.route = route;
    }

    public int getId(){
        return id;
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
     * @return A List of Passengers to board onto the Bus at this Stop; calling this method also clears this Stop's list of Passengers
     */
    public ArrayList<Passenger> getPassengersForBoarding(){
        ArrayList<Passenger> toBoard = new ArrayList<>(passengers);
        passengers.clear();
        return toBoard;
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
