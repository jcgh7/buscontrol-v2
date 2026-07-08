package route.control.interfaces;

import route.Route;
import route.entity.Bus;

public interface ExpressController {
    public int getNextStopIdForBus(Bus b, Route r);
}
