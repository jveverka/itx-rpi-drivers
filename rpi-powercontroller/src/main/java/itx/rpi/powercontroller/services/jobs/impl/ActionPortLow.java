package itx.rpi.powercontroller.services.jobs.impl;

import itx.rpi.powercontroller.services.RPiService;
import itx.rpi.powercontroller.services.jobs.ActionParent;

public class ActionPortLow extends ActionParent {

    private final Integer port;
    private final RPiService rPiService;

    public ActionPortLow(Integer port, RPiService rPiService) {
        this.port = port;
        this.rPiService = rPiService;
   }

    @Override
    public String getType() {
        return "ActionPortLow";
    }

    @Override
    public String getDescription() {
        return "Switch OFF port " + port;
    }

    @Override
    public void taskBody() throws Exception {
        rPiService.setPortState(port, false);
    }

}
