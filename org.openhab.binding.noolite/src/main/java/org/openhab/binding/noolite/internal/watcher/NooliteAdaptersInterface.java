package org.openhab.binding.noolite.internal.watcher;

import java.io.IOException;

import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;

public interface NooliteAdaptersInterface {

    public void connect(NooliteBridgeConfiguration config) throws Exception;

    public void disconnect();

    public void sendData(byte[] data) throws IOException;

    public void addWatcher(NooliteWatcher watcher);

    public void removeWatcher(NooliteWatcher watcher);
}
