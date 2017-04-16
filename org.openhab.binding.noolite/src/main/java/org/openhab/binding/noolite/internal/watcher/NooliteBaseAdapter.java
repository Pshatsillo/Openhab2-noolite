package org.openhab.binding.noolite.internal.watcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NooliteBaseAdapter implements NooliteAdaptersInterface {

    private static final Logger logger = LoggerFactory.getLogger(NooliteBaseAdapter.class);
    private static List<NooliteWatcher> watchers = new ArrayList<NooliteWatcher>();

    @Override
    public void addWatcher(NooliteWatcher watcher) {
        if (!watchers.contains(watcher)) {
            watchers.add(watcher);
        }
    }

    @Override
    public void removeWatcher(NooliteWatcher watcher) {
        watchers.remove(watcher);

    }

    void sendMsgToWatchers(byte[] msg) {
        try {
            Iterator<NooliteWatcher> iterator = watchers.iterator();

            while (iterator.hasNext()) {
                iterator.next().dataReceived(msg);
            }

        } catch (Exception e) {
            logger.error("Event listener invoking error", e);
        }
    }
}
