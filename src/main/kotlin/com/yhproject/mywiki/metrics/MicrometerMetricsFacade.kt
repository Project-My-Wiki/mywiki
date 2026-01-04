package com.yhproject.mywiki.metrics

import io.micrometer.core.instrument.MeterRegistry
import java.util.concurrent.TimeUnit
import org.springframework.stereotype.Component

@Component
class MicrometerMetricsFacade(private val meterRegistry: MeterRegistry) : MetricsFacade {
    private val bookmarkCreated = meterRegistry.counter("mywiki.bookmark.created")
    private val summaryCreated = meterRegistry.counter("mywiki.summary.created")
    private val metadataFetchErrors = meterRegistry.counter("mywiki.metadata.fetch.errors")

    override fun incrementBookmarkCreated() {
        bookmarkCreated.increment()
    }

    override fun incrementBookmarkRead(status: String) {
        meterRegistry.counter("mywiki.bookmark.read", "status", status).increment()
    }

    override fun incrementSummaryCreated() {
        summaryCreated.increment()
    }

    override fun recordMetadataFetchDuration(status: String, durationNanos: Long) {
        // We use a Timer builder or lookup here. Since tags vary, we can't pre-register a single
        // Timer object easily
        // without knowing all tag values or using the registry directly.
        // But for performance, it's better to register. Here I will look it up dynamically as
        // typical for status tags.
        meterRegistry
                .timer("mywiki.metadata.fetch.duration", "status", status)
                .record(durationNanos, TimeUnit.NANOSECONDS)
    }

    override fun incrementMetadataFetchErrors() {
        metadataFetchErrors.increment()
    }
}
