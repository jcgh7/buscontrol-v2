package route.control.interfaces;

import java.util.Optional;

import route.entity.Bus;

public interface Terminal {
    public void terminate(Bus b);
    public Optional<Bus> getBusForDispatchIfExists();
}
