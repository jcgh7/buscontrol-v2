package route.entity;

public class Passenger {
    private final int destinationStopId;

    private int ticksSpentWaiting = 0;
    private int ticksSpentTraveling = 0;
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
