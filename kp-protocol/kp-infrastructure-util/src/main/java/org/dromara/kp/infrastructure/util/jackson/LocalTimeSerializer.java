
package org.dromara.kp.infrastructure.util.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间类型序列化工具
 *
 * @author but
 */
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {
    public static final LocalTimeSerializer INSTANCE = new LocalTimeSerializer();

    private LocalTimeSerializer() {
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER_MS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.format(DATE_TIME_FORMATTER_MS));
    }
}
