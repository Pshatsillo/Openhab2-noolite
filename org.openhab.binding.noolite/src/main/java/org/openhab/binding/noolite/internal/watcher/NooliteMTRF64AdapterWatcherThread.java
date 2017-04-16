package org.openhab.binding.noolite.internal.watcher;

import java.io.DataInputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.noolite.handler.NooliteMTRF64BridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NooliteMTRF64AdapterWatcherThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(NooliteMTRF64AdapterWatcherThread.class);

    private boolean stopped = false;
    private NooliteMTRF64Adapter base;
    DataInputStream in;

    public NooliteMTRF64AdapterWatcherThread(NooliteMTRF64Adapter nooliteMTRF64Adapter, DataInputStream in) {
        base = nooliteMTRF64Adapter;
        this.in = in;
    }

    public NooliteMTRF64AdapterWatcherThread(String string) {

    }

    @Override
    public void interrupt() {
        stopped = true;
        super.interrupt();
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }

    }

    @Override
    public void run() {
        byte[] data = new byte[17];

        try {
            logger.debug("Starting data listener");
            while (stopped != true) {
                // if (data.length == 17) {
                if (in.read(data) == 17) {
                    logger.debug("Received data: {}", DatatypeConverter.printHexBinary(data));
                    int sum = 0;
                    for (int i = 0; i < 14; i++) {
                        sum += data[i];
                    }
                    if (((data[0] & 0xFF) == 0b10101101) && (sum == data[15]) && ((data[16] & 0xFF) == 0b10101110)) {
                        logger.debug("crc correct");
                        NooliteMTRF64BridgeHandler.updateValues(data);
                    }

                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
