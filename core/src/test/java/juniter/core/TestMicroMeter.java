package juniter.core;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class TestMicroMeter {

    @Test
    public void givenGlobalRegistry_whenIncrementAnywhere_thenCounted() {
        class CountedObject {
            private CountedObject() {
                Metrics.counter("objects.instance").increment(1.0);
            }
        }
        Metrics.addRegistry(new SimpleMeterRegistry());

        Metrics.counter("objects.instance").increment();
        new CountedObject();

        Counter counterOptional = Metrics.globalRegistry .find("objects.instance").counter();
        assertTrue(counterOptional != null);
        assertTrue(counterOptional.count() == 2.0);
    }
}
