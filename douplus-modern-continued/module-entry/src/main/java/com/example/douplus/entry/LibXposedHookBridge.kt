package com.example.douplus.entry

import com.example.douplus.hookapi.HookBridge
import io.github.libxposed.api.XposedInterface
import java.lang.reflect.Method

class LibXposedHookBridge(
    private val module: DouPlusModule,
) : HookBridge {
    override fun installMethodHook(method: Method, hookerClass: Class<*>): Any? {
        @Suppress("UNCHECKED_CAST")
        val safeHooker = hookerClass as? Class<out XposedInterface.Hooker>
            ?: return null

        return runCatching {
            module.hook(method, safeHooker)
        }.getOrElse {
            val fallback = module.javaClass.methods.firstOrNull { m ->
                m.name == "hookMethod" && m.parameterTypes.size == 2
            } ?: return null
            fallback.isAccessible = true
            fallback.invoke(module, method, safeHooker)
        }
    }

    override fun deoptimize(method: Method): Boolean {
        return runCatching {
            module.deoptimize(method)
        }.getOrElse {
            val fallback = module.javaClass.methods.firstOrNull { m ->
                m.name == "deoptimizeMethod" && m.parameterTypes.size == 1
            } ?: return false
            fallback.isAccessible = true
            (fallback.invoke(module, method) as? Boolean) ?: false
        }
    }
}
