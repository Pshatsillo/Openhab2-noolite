package org.openhab.binding.noolite.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;
import org.openhab.binding.noolite.internal.watcher.NooliteMTRF64Adapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NooliteMTRF64BridgeHandler extends BaseBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(NooliteMTRF64BridgeHandler.class);
    private String comport;
    NooliteMTRF64Adapter adapter;
    private NooliteBridgeConfiguration bridgeConfig;
    private ScheduledFuture<?> connectorTask;
    public static Map<String, NooliteHandler> thingHandlerMap = new HashMap<String, NooliteHandler>();
    static NooliteHandler nooliteHandler;

    public NooliteMTRF64BridgeHandler(Bridge bridge) {

        super(bridge);
        // comport = bridge.getConfiguration().get("comport").toString();
        // logger.debug("comport is {}", comport);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

    }

    @Override
    public void dispose() {
        logger.debug("Dispose Noolite bridge handler {}", this.toString());
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE, "Close bridge");
    }

    @Override
    public void initialize() {
        logger.debug("Initializing Noolite bridge handler {}", this.toString());
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE, "Initializing...");
        bridgeConfig = getConfigAs(NooliteBridgeConfiguration.class);

        if (connectorTask == null || connectorTask.isCancelled()) {
            connectorTask = scheduler.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    logger.debug("Checking Noolite adapter connection. {}", thing.getStatus());
                    if (thing.getStatus() != ThingStatus.ONLINE) {
                        connect();
                    }
                }

            }, 0, 60, TimeUnit.SECONDS);

        }
    }

    private void connect() {
        logger.debug("Connecting to Noolite adapter");
        try {
            if (bridgeConfig.serial != null) {
                if (adapter == null) {
                    adapter = new NooliteMTRF64Adapter();
                }
            }
            if (adapter != null) {
                adapter.disconnect();
                adapter.connect(bridgeConfig);
                // adapter.connect("fake");
                // adapter.addWatcher(watcher);

            }
            updateStatus(ThingStatus.ONLINE);
        } catch (Exception e) {
            updateStatus(ThingStatus.OFFLINE);
            e.printStackTrace();
        }
    }

    public void registerMegadThingListener(NooliteHandler thingHandler) {
        if (thingHandler == null) {
            throw new IllegalArgumentException("It's not allowed to pass a null ThingHandler.");
        } else {

            String thingID = thingHandler.getThing().getConfiguration().get("type").toString() + "."
                    + thingHandler.getThing().getConfiguration().get("port").toString();
            if (thingHandlerMap.get(thingID) != null) {
                thingHandlerMap.remove(thingID);
            }
            logger.debug("thingHandler for thing: '{}'", thingID);
            if (thingHandlerMap.get(thingID) == null) {
                thingHandlerMap.put(thingID, thingHandler);
                logger.debug("register thingHandler for thing: {}", thingHandler);
                updateThingHandlerStatus(thingHandler, this.getStatus());
                if (thingID.equals("localhost.")) {
                    updateThingHandlerStatus(thingHandler, ThingStatus.OFFLINE);
                }
            } else {
                logger.debug("thingHandler for thing: '{}' already registerd", thingID);
                updateThingHandlerStatus(thingHandler, this.getStatus());
            }

        }
    }

    public ThingStatus getStatus() {
        return getThing().getStatus();
    }

    private void updateThingHandlerStatus(NooliteHandler thingHandler, ThingStatus status) {
        thingHandler.updateStatus(status);
    }

    public static void updateValues(byte[] data) {

        String thingID = data[1] + "." + data[4];
        nooliteHandler = thingHandlerMap.get(thingID);
        if (nooliteHandler != null) {
            nooliteHandler.updateValues(data);
        }
    }

}
