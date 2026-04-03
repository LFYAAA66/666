package com.example.douplus.entry

import com.example.douplus.adapter.report.AdaptationReport
import com.example.douplus.core.logging.LogCenter
import com.example.douplus.core.safety.SafeRunner
import io.github.libxposed.api.XposedModuleInterface
import kotlinx.coroutines.runBlocking

class LifecycleCoordinator(
    private val logCenter: LogCenter,
    private val scopeGate: ScopeGate,
    private val frameworkBridge: FrameworkServiceBridge,
    private val hostSessionFactory: HostSessionFactory,
    private val featureBootstrapper: FeatureBootstrapper,
    private val safeRunner: SafeRunner,
) {
    fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        safeRunner.run("onModuleLoaded") {
            frameworkBridge.initialize()
            logCenter.i("Module loaded")
        }
    }

    fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        logCenter.i("Package loaded callback: ${param.packageName}/${param.processName}")
    }

    fun handlePackageReady(
        packageName: String,
        processName: String,
        classLoader: ClassLoader,
        hookBridge: LibXposedHookBridge,
    ) {
        val report = AdaptationReport(hostPackage = packageName)
        safeRunner.run("handlePackageReady") {
            if (!scopeGate.accept(packageName, processName)) return@run
            val hostSession = hostSessionFactory.create(packageName, processName, classLoader, versionName = if (packageName == "com.ss.android.ugc.aweme") "38.2.0" else null, versionCode = if (packageName == "com.ss.android.ugc.aweme") 380201L else -1L, hookBridge = hookBridge)
            runBlocking {
                featureBootstrapper.installAll(hostSession, report)
            }
        }
        frameworkBridge.persistReport(report)
    }
}
