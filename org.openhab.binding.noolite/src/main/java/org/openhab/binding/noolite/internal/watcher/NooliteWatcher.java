/**
 *
 */
package org.openhab.binding.noolite.internal.watcher;

/**
 * @author Petr Shatsillo
 *
 */
public interface NooliteWatcher {

    void dataReceived(byte[] data);

}
