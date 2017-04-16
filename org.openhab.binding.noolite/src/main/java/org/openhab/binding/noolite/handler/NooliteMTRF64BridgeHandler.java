package org.openhab.binding.noolite.handler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;
import org.openhab.binding.noolite.internal.watcher.NooliteAdaptersInterface;
import org.openhab.binding.noolite.internal.watcher.NooliteMTRF64Adapter;
import org.openhab.binding.noolite.internal.watcher.NooliteWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NooliteMTRF64BridgeHandler extends BaseBridgeHandler {

    private Logger logger = LoggerFactory.getLogger(NooliteMTRF64BridgeHandler.class);
    private String comport;
    NooliteAdaptersInterface adapter;
    private NooliteBridgeConfiguration bridgeConfig;
    private ScheduledFuture<?> connectorTask;
    private MessagesWatcher watcher;

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
                adapter.addWatcher(watcher);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class MessagesWatcher implements NooliteWatcher {

        @Override
        public void dataReceived(byte[] data) {
            logger.debug("Message received: {}", data);
        }

    }
}
