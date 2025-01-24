
package org.dromara.kp.infrastructure.util.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * timestamp 序列化
 *
 * @author but
 */
public class TimestampSerializer extends StdSerializer<Timestamp> {
    public static final TimestampSerializer INSTANCE = new TimestampSerializer();

    private static final FastDateFormat FAST_DATE_FORMAT_MS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    private TimestampSerializer() {
        super(Timestamp.class);
    }

    @Override
    public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(FAST_DATE_FORMAT_MS.format(value));
    }
}
