package com.example.douplus.entry

import com.example.douplus.core.logging.LogCenter
import com.example.douplus.core.safety.SafeRunner
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface

/**
 * 运行位置：宿主进程
 * 注意：该类依赖 libxposed/api，需用户自行准备依赖工件。
 */
class DouPlusModule(base: io.github.libxposed.api.XposedInterface, private val moduleLoadedParam: XposedModuleInterface.ModuleLoadedParam) : XposedModule(base, moduleLoadedParam) {

    private val logCenter = LogCenter(::log)
    private val safeRunner = SafeRunner(logCenter)
    private val frameworkBridge = FrameworkServiceBridge(this, logCenter)
    private val lifecycleCoordinator = LifecycleCoordinator(
        logCenter = logCenter,
        scopeGate = ScopeGate(),
        frameworkBridge = frameworkBridge,
        hostSessionFactory = HostSessionFactory(logCenter),
        featureBootstrapper = FeatureBootstrapper(logCenter, frameworkBridge),
        safeRunner = safeRunner,
    )

    override fun onModuleLoaded() {
        lifecycleCoordinator.onModuleLoaded(moduleLoadedParam)
    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        lifecycleCoordinator.onPackageLoaded(param)
    }

    override fun onPackageReady(param: XposedModuleInterface.PackageReadyParam) {
        lifecycleCoordinator.handlePackageReady(
            packageName = param.packageName,
            processName = param.processName,
            classLoader = param.classLoader,
            hookBridge = LibXposedHookBridge(this),
        )
    }
}
