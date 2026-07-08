package route.entity;

public class Passenger {
    private final int destinationStopId;

    private int ticksSpentWaiting = 0; // ticks spent waiting at the stop
    private int ticksSpentTraveling = 0; // ticks spent both waiting at the stop and on the bus
    private boolean isOnBus = false;

    public Passenger(int destinationStopId){
        this.destinationStopId = destinationStopId;
    }

    public void tick(){
        if(!isOnBus){
            ticksSpentWaiting++;
        }
        ticksSpentTraveling++;
    }

    /**
     * 
     * @param b value to set the Passenger's isOnBus flag to
     */
    public void setOnBus(boolean b){
        isOnBus = b;
    }

    public int getTicksSpentWaiting(){
        return ticksSpentWaiting;
    }

    public int getTicksSpentTraveling(){
        return ticksSpentTraveling;
    }

    public int getDestinationStopId(){
        return destinationStopId;
    }
}
