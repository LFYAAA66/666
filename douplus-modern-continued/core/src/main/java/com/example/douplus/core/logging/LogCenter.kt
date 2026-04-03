package com.example.douplus.core.logging

/** 双进程可用的轻量日志中心。 */
class LogCenter(
    private val sink: LogSink = LogSink { _, _, _ -> }
) {
    fun d(message: String, tag: String = "DouPlus") = sink.log(LogLevel.DEBUG, tag, message, null)
    fun i(message: String, tag: String = "DouPlus") = sink.log(LogLevel.INFO, tag, message, null)
    fun w(message: String, tag: String = "DouPlus") = sink.log(LogLevel.WARN, tag, message, null)
    fun e(message: String, throwable: Throwable? = null, tag: String = "DouPlus") = sink.log(LogLevel.ERROR, tag, message, throwable)

    constructor(printer: (String, Throwable?) -> Unit) : this(
        LogSink { level, tag, message, throwable ->
            printer("[${level.short}] [$tag] $message", throwable)
        }
    )
}

enum class LogLevel(val short: String) {
    DEBUG("D"),
    INFO("I"),
    WARN("W"),
    ERROR("E")
}

fun interface LogSink {
    fun log(level: LogLevel, tag: String, message: String, throwable: Throwable?)
}
