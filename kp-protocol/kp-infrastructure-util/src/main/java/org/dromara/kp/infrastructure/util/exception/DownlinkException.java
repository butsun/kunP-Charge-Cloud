
package org.dromara.kp.infrastructure.util.exception;

/**
 * @author but
 */
public class DownlinkException extends RuntimeException {

    public DownlinkException(String message) {
        super(message);
    }

    public DownlinkException(String message, Throwable cause) {
        super(message, cause);
    }
}
