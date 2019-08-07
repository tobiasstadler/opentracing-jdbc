package com.github.tobiasstadler.opentracing.jdbc.util;


import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import javax.enterprise.inject.spi.CDI;

public final class TracerProvider {

    private TracerProvider() {
    }

    public static Tracer getTracer() {
        try {
            return CDI.current().select(Tracer.class).get();
        } catch (Exception e) {
            return GlobalTracer.get();
        }
    }
}
