package ru.t1.daev.bishopstarter.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AndroidMetrics {

    private final AtomicInteger queueSize = new AtomicInteger(0);
    private final Map<String, Counter> commandCounters = new ConcurrentHashMap<>();
    private final MeterRegistry meterRegistry;

    public AndroidMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        setupMetrics();
    }

    private void setupMetrics() {
        Gauge.builder("android.queue.size", queueSize, AtomicInteger::get)
                .description("Current command queue size")
                .register(meterRegistry);
    }

    public void updateQueueSize(int size) {
        queueSize.set(size);
    }

    public void incrementCommandCount(String author) {
        commandCounters.computeIfAbsent(author, key ->
                Counter.builder("android.command.count")
                        .tag("author", author)
                        .description("Commands executed by author")
                        .register(meterRegistry)
        ).increment();
    }
}