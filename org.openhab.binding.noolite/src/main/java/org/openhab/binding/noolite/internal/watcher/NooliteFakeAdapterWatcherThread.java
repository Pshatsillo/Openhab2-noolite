package org.openhab.binding.noolite.internal.watcher;

import java.io.DataInputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.noolite.handler.NooliteMTRF64BridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NooliteFakeAdapterWatcherThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(NooliteFakeAdapterWatcherThread.class);

    private boolean stopped = false;
    private NooliteMTRF64Adapter base;
    DataInputStream in;

    public NooliteFakeAdapterWatcherThread(NooliteMTRF64Adapter nooliteMTRF64Adapter, DataInputStream in) {
        base = nooliteMTRF64Adapter;
        this.in = in;
    }

    public NooliteFakeAdapterWatcherThread(String string) {

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

        /*
         * byte[] data = { (byte) 0b10101101, (byte) 0b00000001, (byte) 0b00000000, (byte) 0b00101101, (byte)
         * 0b00000000,
         * (byte) 0b00001111, (byte) 0b00000001, (byte) 0b00000010, (byte) 0b00100001, (byte) 0b00010011,
         * (byte) 0b11111111, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000, (byte) 0b00000000,
         * (byte) 0b00100000, (byte) 0b10101110 };
         */

        int sum = 0;
        for (int i = 0; i < 14; i++) {
            sum += data[i];
        }
        logger.debug("crc is {}", sum);

        try {
            logger.debug("Starting data listener");
            while (stopped != true) {
                // if (data.length == 17) {
                if (in.read(data) == 17) {
                    logger.debug("Received data: {}", DatatypeConverter.printHexBinary(data));
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
