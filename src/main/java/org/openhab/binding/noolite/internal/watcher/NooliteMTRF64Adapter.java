/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.noolite.internal.watcher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.noolite.handler.NooliteMTRF64BridgeHandler;
import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 *
 * @author Petr Shatsillo - Initial contribution
 *
 */
public class NooliteMTRF64Adapter {

    private static final Logger logger = LoggerFactory.getLogger(NooliteMTRF64Adapter.class);
    DataInputStream in = null;
    DataOutputStream out = null;
    Thread watcherThread = null;
    private static OutputStream output;
    SerialPort serial;

    public void connect(NooliteBridgeConfiguration config) throws Exception {

        logger.debug("Opening serial connection to port {} with baud rate 9600...", config.serial);

        CommPortIdentifier portIdentifier;

        portIdentifier = CommPortIdentifier.getPortIdentifier(config.serial);
        serial = portIdentifier.open("openhab", 3000);

        serial.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        in = new DataInputStream(serial.getInputStream());
        output = serial.getOutputStream();
        serial.addEventListener(new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent event) {
                try {
                    byte[] data = new byte[17];
                    if (in.read(data) > 0) {
                        logger.debug("Received data: {}", DatatypeConverter.printHexBinary(data));
                        short count = 0;
                        byte sum = 0;
                        for (int i = 0; i <= 14; i++) {
                            count += (data[i] & 0xFF);
                        }
                        sum = (byte) (count & 0xFF);

                        logger.debug("sum is {} CRC must be {} receive {}", count, sum, data[15]);

                        if (((data[0] & 0xFF) == 0b10101101) && ((data[16] & 0xFF) == 0b10101110)) {
                            logger.debug("sum is {} CRC must be {} receive {}", count, sum, data[15]);
                            if (sum == data[15]) {
                                logger.debug("CRC is OK");

                                logger.debug("Updating values...");
                                NooliteMTRF64BridgeHandler.updateValues(data);
                            } else {
                                logger.debug("CRC is WRONG");
                            }
                        } else {
                            logger.debug("Start/stop bits is wrong");

                        }
                    }
                } catch (IOException ex) {
                    logger.debug("Error reading from serial port!", ex);
                }
            }
        });
        serial.notifyOnDataAvailable(true);
    }

    public void disconnect() {
        /*
         * if (serial != null) {
         * serial.removeEventListener();
         * }
         *
         * if (watcherThread != null) {
         * watcherThread.interrupt();
         * }
         *
         * if (out != null) {
         * logger.debug("Close serial out stream");
         * IOUtils.closeQuietly(out);
         * }
         * if (in != null) {
         * logger.debug("Close serial in stream");
         * IOUtils.closeQuietly(in);
         * }
         *
         * if (serial != null) {
         * logger.debug("Close serial port");
         * serial.close();
         * }
         */
        if (serial != null) {
            serial.notifyOnDataAvailable(false);
            serial.removeEventListener();
            serial.close();
        }
        in = null;
        out = null;

    }

    public void sendData(byte[] data) throws IOException {
        logger.debug("Sending {} bytes: {}", data.length, DatatypeConverter.printHexBinary(data));

        // OutputStreamWriter w = new OutputStreamWriter(serial.getOutputStream());
        output.write(25);
        output.flush();
    }

}
