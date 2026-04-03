package com.example.douplus.hookapi

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 运行位置：宿主进程。
 *
 * 说明：
 * - 该抽象保持对 libxposed API 的最小耦合，便于在缺少真实工件时也能完成编译与静态检查。
 * - hookerClass 仅作为桥接参数传递给具体 HookBridge 实现，不在 hook-api 层声明框架类型。
 */
interface HookBridge {
    fun installMethodHook(method: Method, hookerClass: Class<*>): Any?
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
