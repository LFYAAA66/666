package com.example.douplus.core.safety

import com.example.douplus.adapter.report.AdaptationReport
import com.example.douplus.core.logging.LogCenter

class HookIsolationRunner(
    private val logCenter: LogCenter
) {
    fun guarded(
        featureName: String,
        resolverName: String,
        targetType: String,
        report: AdaptationReport,
        block: () -> Unit
    ): Boolean {
        return try {
            block()
            true
        } catch (t: Throwable) {
            logCenter.e("Hook isolation failed: $featureName/$resolverName/$targetType", t)
            report.recordFailure(featureName, resolverName, targetType, "Hook isolation failed", t)
            false
        }
    }

    fun <T> guardedValue(
        featureName: String,
        resolverName: String,
        targetType: String,
        report: AdaptationReport,
        fallback: T,
        block: () -> T
    ): T {
        return try {
            block()
        } catch (t: Throwable) {
            logCenter.e("Hook isolation value failed: $featureName/$resolverName/$targetType", t)
            report.recordFailure(featureName, resolverName, targetType, "Hook isolation value failed", t)
            fallback
        }
    }
}
