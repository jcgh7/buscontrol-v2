package route.control.defaultControl;

import route.Route;
import route.control.interfaces.HoldController;
import route.entity.Bus;

public class DefaultHoldController implements HoldController{
    public int getExtraDwellTimeTicks(Bus b, Route r){
        return 0;
    }
}
