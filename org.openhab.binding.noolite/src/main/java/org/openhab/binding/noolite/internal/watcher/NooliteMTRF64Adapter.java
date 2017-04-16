package org.openhab.binding.noolite.internal.watcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NRSerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class NooliteMTRF64Adapter extends NooliteBaseAdapter implements SerialPortEventListener {

    private static final Logger logger = LoggerFactory.getLogger(NooliteMTRF64Adapter.class);
    DataInputStream in = null;
    DataOutputStream out = null;
    Thread watcherThread = null;
    NRSerialPort serial;

    @Override
    public void connect(NooliteBridgeConfiguration config) throws Exception {

        serial = new NRSerialPort(config.serial, 9600);
        serial.connect();

        in = new DataInputStream(serial.getInputStream());
        out = new DataOutputStream(serial.getOutputStream());
        out.flush();

        serial.addEventListener(this);
        serial.notifyOnDataAvailable(true);

        watcherThread = new NooliteWatcherThread(this, in);
        watcherThread.start();
    }

    @Override
    public void serialEvent(SerialPortEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnect() {
        if (serial != null) {
            serial.removeEventListener();
        }

        if (watcherThread != null) {
            watcherThread.interrupt();
        }

        if (out != null) {
            logger.debug("Close serial out stream");
            IOUtils.closeQuietly(out);
        }
        if (in != null) {
            logger.debug("Close serial in stream");
            IOUtils.closeQuietly(in);
        }

        if (serial != null) {
            logger.debug("Close serial port");
            serial.disconnect();
        }

        in = null;
        out = null;
        watcherThread = null;

    }

    @Override
    public void sendData(byte[] data) throws IOException {
        logger.trace("Sending {} bytes: {}", data.length, DatatypeConverter.printHexBinary(data));
        out.write(data);
        out.flush();
    }

}
