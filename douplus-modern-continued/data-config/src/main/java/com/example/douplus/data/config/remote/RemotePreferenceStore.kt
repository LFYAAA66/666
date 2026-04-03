package com.example.douplus.data.config.remote

import android.content.SharedPreferences
import com.example.douplus.data.config.model.RemotePreferenceSnapshot
import io.github.libxposed.api.XposedInterface

/**
 * 宿主进程只读视角的 Remote Preferences 抽象封装。
 *
 * 当前说明：
 * - 优先通过 Modern API Remote Preferences 读取；
 * - 若运行环境不具备远程读取条件，则降级为空快照（fallback）；
 * - 这是最小可用实现，不代表完整运行时行为。
 */
class RemotePreferenceStore(
    private val xposed: XposedInterface? = null,
    private val groupName: String = "main"
) {
    private val prefs: SharedPreferences? by lazy {
        xposed?.getRemotePreferences(groupName)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean = prefs?.getBoolean(key, defaultValue) ?: defaultValue
    fun getString(key: String, defaultValue: String? = null): String? = prefs?.getString(key, defaultValue) ?: defaultValue
    fun getInt(key: String, defaultValue: Int = 0): Int = prefs?.getInt(key, defaultValue) ?: defaultValue
    fun rawSnapshot(): Map<String, *> = prefs?.all ?: emptyMap<String, Any>()

    fun snapshot(): RemotePreferenceSnapshot {
        val source = if (prefs == null) "fallback" else "remote_preferences"
        val all = rawSnapshot()
        return RemotePreferenceSnapshot(
            booleans = all.filterValues { it is Boolean }.mapValues { it.value as Boolean },
            strings = all.filterValues { it is String }.mapValues { it.value as String },
            ints = all.filterValues { it is Int }.mapValues { it.value as Int },
            source = source,
        )
    }
}
