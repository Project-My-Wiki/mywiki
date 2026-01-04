package com.yhproject.mywiki.metrics

interface MetricsFacade {
    fun incrementBookmarkCreated()
    fun incrementBookmarkRead(status: String)
    fun incrementSummaryCreated()
    fun recordMetadataFetchDuration(status: String, durationNanos: Long)
    fun incrementMetadataFetchErrors()
}
