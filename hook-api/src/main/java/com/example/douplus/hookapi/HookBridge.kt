package com.example.douplus.hookapi

import io.github.libxposed.api.XposedInterface
import java.lang.reflect.Field
import java.lang.reflect.Method

interface HookBridge {
    fun installMethodHook(method: Method, hooker: Class<out XposedInterface.Hooker>): Any?
    fun deoptimize(method: Method): Boolean = false
}

data class ResolvedMethod(
    val ownerClass: Class<*>,
    val method: Method,
    val ref: MethodRef,
)

data class ResolvedField(
    val ownerClass: Class<*>,
    val field: Field,
    val ref: FieldRef,
)
