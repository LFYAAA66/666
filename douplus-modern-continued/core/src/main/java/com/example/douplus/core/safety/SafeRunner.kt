package com.example.douplus.core.safety

import com.example.douplus.core.logging.LogCenter
import com.example.douplus.core.result.AppResult
import com.example.douplus.core.result.FailReason

class SafeRunner(private val logCenter: LogCenter) {
    fun run(tag: String, block: () -> Unit): SafeRunResult {
        return try {
            block()
            SafeRunResult.Success
        } catch (t: Throwable) {
            logCenter.e("SafeRunner[$tag] failed", t)
            SafeRunResult.Failure(t)
        }
    }

    fun <T> runValue(tag: String, block: () -> T): AppResult<T> {
        return try {
            AppResult.Success(block())
        } catch (t: Throwable) {
            logCenter.e("SafeRunner[$tag] failed", t)
            AppResult.Failure(FailReason.Message("safe-runner:$tag"), t)
        }
    }

    fun <T> runOrDefault(tag: String, defaultValue: T, block: () -> T): T {
        return when (val result = runValue(tag, block)) {
            is AppResult.Success -> result.value
            is AppResult.Failure -> defaultValue
        }
    }
}

sealed interface SafeRunResult {
    data object Success : SafeRunResult
    data class Failure(val throwable: Throwable) : SafeRunResult
}
