package route.control.interfaces;

import java.util.Optional;

import route.Route;
import route.entity.Bus;

public interface Terminal {
    public void terminate(Bus b);
    public Optional<Bus> getBusForDispatchIfExists(Route r);
    public void addBusToTerminal(Bus b);
    public void removeBusFromTerminal();
}
