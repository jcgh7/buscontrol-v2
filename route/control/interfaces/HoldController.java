package route.control.interfaces;

import route.Route;
import route.entity.Bus;

public interface HoldController {
    public int getExtraDwellTimeTicks(Bus b, Route r);
}
