
package org.dromara.kp.infrastructure.util.jackson;

import java.time.format.DateTimeFormatter;

/**
 * Instant 序列化
 *
 * @author but
 */
public class InstantSerializer extends com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer {
    public static final InstantSerializer INSTANCE = new InstantSerializer();

    private InstantSerializer() {
        super(com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer.INSTANCE, true,false, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }

}
