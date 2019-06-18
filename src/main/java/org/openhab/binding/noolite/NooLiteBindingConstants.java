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
package org.openhab.binding.noolite;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link nooliteBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Petr Shatsillo - Initial contribution
 */
public class NooLiteBindingConstants {

    public static final String BINDING_ID = "noolite";

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
