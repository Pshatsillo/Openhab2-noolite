/**
 *
 */
package org.openhab.binding.noolite.internal.connector;

/**
 * @author Petr Shatsillo
 *
 */
public interface NooliteEventsListener {

    /**
     * Receive raw data from noolite usb controller.
     *
     * @param data
     * 
     */
    void packetReceived(byte[] data);

}
