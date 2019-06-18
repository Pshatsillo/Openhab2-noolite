package org.openhab.binding.noolite.internal.watcher;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;

/**
 * The {@link NooliteMTRF64Adapter} is for init usb stick
 *
 * @author Petr Shatsillo - Initial contribution
 */
public class NooliteMTRF64Adapter {

    private static final Logger logger = LoggerFactory.getLogger(NooliteMTRF64Adapter.class);

    Thread watcherThread = null;
    private static SerialPort comPort;

    public void connect(NooliteBridgeConfiguration config) {

        comPort = SerialPort.getCommPort(config.serial);
        comPort.openPort();

        if (!comPort.isOpen()) {
            logger.error("Port is busy");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Sleep error {}", e.getLocalizedMessage());
            }
            connect(config);
        }

        watcherThread = new NooliteMTRF64AdapterWatcherThread(comPort);
        watcherThread.start();
    }

    public void connect(String string) {

        watcherThread = new NooliteFakeAdapterWatcherThread("fake");
        watcherThread.start();

    }

    public void disconnect() {

        if (watcherThread != null) {
            watcherThread.interrupt();
        }

        if (comPort != null) {
            logger.debug("Close serial port");
            comPort.closePort();
        }

        watcherThread = null;

    }

    public void sendData(byte[] data) throws IOException {
        logger.debug("Sending {} bytes: {}", data.length, DatatypeConverter.printHexBinary(data));

        comPort.writeBytes(data, data.length);
    }

}
