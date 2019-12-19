package com.github.icovn.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import net.logstash.logback.composite.JsonWritingUtils;
import net.logstash.logback.composite.loggingevent.StackTraceJsonProvider;

public class CustomStackTraceJsonProvider extends StackTraceJsonProvider {

  public CustomStackTraceJsonProvider() {
    super();
  }

  @Override
  public void writeTo(JsonGenerator generator, ILoggingEvent event) throws IOException {
    IThrowableProxy throwableProxy = event.getThrowableProxy();
    if (throwableProxy != null) {
      String msg = getThrowableConverter().convert(event);
      String[] lines = msg.split("\\n\\t");
      Map<String, String> lineMap = new LinkedHashMap<>();
      for(int i = 0; i < lines.length; i++){
        lineMap.put(String.valueOf(i+1), lines[i]);
      }
      JsonWritingUtils.writeMapStringFields(generator, getFieldName(), lineMap);
    }
  }
}