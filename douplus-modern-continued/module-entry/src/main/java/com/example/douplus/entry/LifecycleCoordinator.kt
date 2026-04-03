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

    fun onPackageLoaded(snapshot: PackageParamReader.LoadedSnapshot) {
        logCenter.i("Package loaded callback: ${snapshot.packageName}/${snapshot.processName}")
    }

    fun handlePackageReady(
        packageName: String,
        processName: String,
        classLoader: ClassLoader,
        versionName: String? = null,
        versionCode: Long = -1L,
        hookBridge: LibXposedHookBridge,
    ) {
        val resolvedVersionName = versionName ?: if (packageName == "com.ss.android.ugc.aweme") "38.2.0" else null
        val resolvedVersionCode = if (versionCode > 0) versionCode else if (packageName == "com.ss.android.ugc.aweme") 380201L else -1L
        val report = AdaptationReport(
            hostPackage = packageName,
            hostVersionName = resolvedVersionName,
            hostVersionCode = resolvedVersionCode,
        )
        safeRunner.run("handlePackageReady") {
            if (!scopeGate.accept(packageName, processName)) return@run
            val hostSession = hostSessionFactory.create(
                packageName = packageName,
                processName = processName,
                classLoader = classLoader,
                versionName = resolvedVersionName,
                versionCode = resolvedVersionCode,
                hookBridge = hookBridge,
            )
            runBlocking {
                featureBootstrapper.installAll(hostSession, report)
            }
        }
        frameworkBridge.persistReport(report)
    }
}
