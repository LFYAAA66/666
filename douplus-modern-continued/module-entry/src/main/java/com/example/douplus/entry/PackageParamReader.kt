package com.example.douplus.entry

import io.github.libxposed.api.XposedModuleInterface

/**
 * 运行位置：宿主进程。
 *
 * 对 PackageLoadedParam / PackageReadyParam 做多访问器读取，兼容不同 API 形态。
 */
object PackageParamReader {

    data class LoadedSnapshot(
        val packageName: String,
        val processName: String,
    )

    data class ReadySnapshot(
        val packageName: String,
        val processName: String,
        val classLoader: ClassLoader,
        val versionName: String? = null,
        val versionCode: Long = -1L,
    )

    fun readLoaded(param: XposedModuleInterface.PackageLoadedParam): LoadedSnapshot {
        val packageName = readString(param, "getPackageName", "packageName").orEmpty()
        val processName = readString(param, "getProcessName", "processName").orEmpty()
        return LoadedSnapshot(
            packageName = packageName,
            processName = processName.ifBlank { packageName },
        )
    }

    fun readReady(param: XposedModuleInterface.PackageReadyParam): ReadySnapshot {
        val packageName = readString(param, "getPackageName", "packageName").orEmpty()
        val processName = readString(param, "getProcessName", "processName").orEmpty()
        val classLoader = readClassLoader(param, "getClassLoader", "classLoader") ?: error("ClassLoader unavailable")
        val versionName = readVersionName(param)
        val versionCode = readVersionCode(param)
        return ReadySnapshot(
            packageName = packageName,
            processName = processName.ifBlank { packageName },
            classLoader = classLoader,
            versionName = versionName,
            versionCode = versionCode,
        )
    }

    private fun readVersionName(param: Any): String? {
        val appInfo = readObject(param, "getAppInfo", "appInfo")
        val packageInfo = readObject(param, "getPackageInfo", "packageInfo")
        return readString(appInfo, "versionName") ?: readString(packageInfo, "versionName")
    }

    private fun readVersionCode(param: Any): Long {
        val appInfo = readObject(param, "getAppInfo", "appInfo")
        val packageInfo = readObject(param, "getPackageInfo", "packageInfo")
        val appVersion = readLong(appInfo, "longVersionCode", "versionCode")
        val pkgVersion = readLong(packageInfo, "longVersionCode", "versionCode")
        return appVersion ?: pkgVersion ?: -1L
    }

    private fun readString(target: Any?, vararg names: String): String? {
        return names.firstNotNullOfOrNull { name ->
            when (val value = readAny(target, name)) {
                is String -> value
                else -> null
            }
        }
    }

    private fun readLong(target: Any?, vararg names: String): Long? {
        return names.firstNotNullOfOrNull { name ->
            when (val value = readAny(target, name)) {
                is Long -> value
                is Int -> value.toLong()
                else -> null
            }
        }
    }

    private fun readClassLoader(target: Any?, vararg names: String): ClassLoader? {
        return names.firstNotNullOfOrNull { name ->
            readAny(target, name) as? ClassLoader
        }
    }

    private fun readObject(target: Any?, vararg names: String): Any? {
        return names.firstNotNullOfOrNull { name -> readAny(target, name) }
    }

    private fun readAny(target: Any?, name: String): Any? {
        if (target == null) return null
        val method = target.javaClass.methods.firstOrNull {
            it.name == name && it.parameterTypes.isEmpty()
        }
        if (method != null) {
            return runCatching { method.invoke(target) }.getOrNull()
        }
        val field = runCatching { target.javaClass.getDeclaredField(name) }.getOrNull() ?: return null
        return runCatching {
            field.isAccessible = true
            field.get(target)
        }.getOrNull()
    }
}
