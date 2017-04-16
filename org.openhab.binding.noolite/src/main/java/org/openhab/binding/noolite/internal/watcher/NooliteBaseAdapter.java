package org.openhab.binding.noolite.internal.connector;

import java.io.IOException;

import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NooliteBaseConnector implements NooliteConnectorInterface {

    private static final Logger logger = LoggerFactory.getLogger(NooliteBaseConnector.class);

    @Override
    public void connect(NooliteBridgeConfiguration config) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendData(byte[] data) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void addEventListener(NooliteEventsListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeEventListener(NooliteEventsListener listener) {
        // TODO Auto-generated method stub

    }

}
