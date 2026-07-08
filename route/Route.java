package route;

import java.util.ArrayList;

import route.entity.Bus;
import route.entity.Stop;

public class Route {
    private ArrayList<Stop> stops;
    private ArrayList<Bus> buses = new ArrayList<>();

    public Route(ArrayList<Stop> stops){
        this.stops = stops;
    }

    public int getRandomStopAfterId(int id){
        return 0;
    }
}
