package org.openhab.binding.noolite.internal.watcher;

import java.io.DataInputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NooliteWatcherThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(NooliteWatcherThread.class);

    private boolean stopped = false;
    private NooliteBaseAdapter base;
    DataInputStream in;

    public NooliteWatcherThread(NooliteMTRF64Adapter nooliteMTRF64Adapter, DataInputStream in) {
        base = nooliteMTRF64Adapter;
        this.in = in;
    }

    @Override
    public void interrupt() {
        stopped = true;
        super.interrupt();
        try {
            in.close();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

    }

    @Override
    public void run() {
        try {
            byte[] data = new byte[17];
            logger.debug("Starting data listener");
            while (stopped != true) {

                if (in.read(data) == 17) {
                    logger.debug("Received data: {}", DatatypeConverter.printHexBinary(data));
                    base.sendMsgToWatchers(data);
                }

            }
        } catch (IOException e) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e1) {
            }
        }
    }
}
