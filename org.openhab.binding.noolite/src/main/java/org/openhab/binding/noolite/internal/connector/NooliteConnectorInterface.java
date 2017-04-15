package org.openhab.binding.noolite.internal.connector;

import java.io.IOException;

import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;

public interface NooliteConnectorInterface {
    /**
     * Procedure for connecting to noolite controller.
     *
     * @param device
     *            Controller connection parameters.
     */
    public void connect(NooliteBridgeConfiguration config) throws Exception;

    /**
     * Procedure for disconnecting of noolite controller.
     *
     */
    public void disconnect();

    /**
     * Procedure for send data to noolite controller.
     *
     * @param data
     *            raw bytes.
     */
    public void sendData(byte[] data) throws IOException;

    /**
     * Procedure for register event listener.
     *
     * @param listener
     *            Event listener instance to handle events.
     */
    public void addEventListener(String /* RFXComEventListener */ listener);

    /**
     * Procedure for remove event listener.
     *
     * @param listener
     *            Event listener instance to remove.
     */
    public void removeEventListener(String/* RFXComEventListener */ listener);
}
