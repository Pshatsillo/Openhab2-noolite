package org.openhab.binding.noolite.handler;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NooliteMTRF64BridgeHandler extends BaseBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(NooliteMTRF64BridgeHandler.class);
    private String comport;

    public NooliteMTRF64BridgeHandler(Bridge bridge) {

        super(bridge);
        comport = bridge.getConfiguration().get("comport").toString();
        logger.debug("comport is {}", comport);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

    }

    @Override
    public void dispose() {
        logger.debug("Dispose Megad bridge handler {}", this.toString());
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE, "Close bridge");
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Megad bridge handler {}", this.toString());

    }
}
