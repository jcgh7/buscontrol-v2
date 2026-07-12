package route.entity;

import java.util.ArrayList;

public class Bus {
    private int run;

    private ArrayList<Passenger> passengers = new ArrayList<>();
    private final int ticksPerPassengerBoardTime = 10;
    private int dwellTicks = 0;

    private double location = 0;
    private double distancePerTick;

    private int nextStopId;

    public Bus(int run, int nextStopId, double distancePerTick) {
        this.run = run;
        this.nextStopId = nextStopId;
        this.distancePerTick = distancePerTick;
    }

    public void reset(){
        location = 0;
        dwellTicks = 0;
        nextStopId = 0;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run){
        this.run = run;
    }

    public void setDistancePerTick(double dpt){
        distancePerTick = dpt;
    }

    public double getDistancePerTick(){
        return distancePerTick;
    }

    public int getPassengerBoardTimeTicks(){
        return ticksPerPassengerBoardTime;
    }

    public int getNextStopId(){
        return nextStopId;
    }

    public void setNextStopId(int id) {
        nextStopId = id;
    }

    public void increaseLocationBy(double amount) {
        location += amount;
    }

    public void setLocationTo(double location) {
        this.location = location;
    }

    public double getLocation(){
        return location;
    }

    public void addDwellTicks(int ticks) {
        dwellTicks += ticks;
    }

    /**
     * 
     * @param toBoard List of Passengers to add to this Bus's Passenger list. Adds
     *                time to dwellTicks based on ticksPerPassengerBoardTime
     */
    public void boardPassengers(ArrayList<Passenger> toBoard) {
        passengers.addAll(toBoard);
        addDwellTicks(toBoard.size() * ticksPerPassengerBoardTime);
    }

    /**
     * 
     * @param id Destination ID of Stop that Passengers to deboard are going to
     * @return List of Passengers that have deboarded
     */
    public ArrayList<Passenger> deboardPassengersWithDestinationId(int id) {
        ArrayList<Passenger> toDeboard = new ArrayList<>();

        passengers.removeIf(p -> {
            if (p.getDestinationStopId() == id) {
                toDeboard.add(p);
                return true; 
            }
            return false; 
        });

        return toDeboard;
    }

    public void tick(){
        for(Passenger p : passengers){
            p.tick();
        }
        if(dwellTicks > 0){
            dwellTicks--;
        }
        else{
            location += distancePerTick;
        }
    }
}
