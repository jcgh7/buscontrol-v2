package route.control.defaultControl;

import route.Route;
import route.control.interfaces.ExpressController;
import route.entity.Bus;

public class DefaultExpressController implements ExpressController{
    public DefaultExpressController(){}
    
    public int getNextStopIdForBus(Bus b, Route r){
        return b.getNextStopId() + 1;
    }
}
