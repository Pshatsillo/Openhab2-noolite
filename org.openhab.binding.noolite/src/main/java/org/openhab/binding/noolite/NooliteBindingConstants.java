/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.noolite;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link nooliteBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Petr Shatsillo - Initial contribution
 */
public class NooliteBindingConstants {

    public final static String BINDING_ID = "noolite";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_DEVICE = new ThingTypeUID(BINDING_ID, "devices");
    public final static ThingTypeUID THING_TYPE_BRIDGEMTRF64 = new ThingTypeUID(BINDING_ID, "bridgeMTRF64");

    // List of all Channel ids
    public final static String CHANNEL_SWITCH = "switch";
    public final static String CHANNEL_TEMPERATURE = "temperature";
    public final static String CHANNEL_HUMIDITY = "humidity";
    public final static String CHANNEL_BATTERY = "battery";
    public final static String CHANNEL_SENSOR_TYPE = "sensortype";
    public final static String CHANNEL_BINDCHANNEL = "bindchannel";
}
