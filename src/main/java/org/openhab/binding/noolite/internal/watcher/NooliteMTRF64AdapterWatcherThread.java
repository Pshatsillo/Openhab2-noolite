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
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.noolite.handler.NooliteMTRF64BridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Petr Shatsillo - Initial contribution
 *
 */
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
                if (in.read(data) > 0) {
                    logger.debug("Received data: {}", DatatypeConverter.printHexBinary(data));
                    short count = 0;
                    byte sum = 0;
                    for (int i = 0; i <= 14; i++) {
                        count += (data[i] & 0xFF);
                    }
                    sum = (byte) (count & 0xFF);

                    // logger.debug("sum is {} CRC must be {} receive {}", count, sum, data[15]);

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
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
    }
}
