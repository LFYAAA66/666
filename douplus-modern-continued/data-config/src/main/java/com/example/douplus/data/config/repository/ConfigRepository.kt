package com.example.douplus.data.config.repository

import com.example.douplus.data.config.local.AppSettingsStore
import com.example.douplus.data.config.model.FeatureSettingsSnapshot
import com.example.douplus.data.config.remote.RemotePreferenceStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ConfigRepository(
    private val appSettingsStore: AppSettingsStore? = null,
    private val remotePreferenceStore: RemotePreferenceStore? = null,
    private val migrationCoordinator: MigrationCoordinator = MigrationCoordinator(),
) {
    fun observeSnapshot(): Flow<FeatureSettingsSnapshot> {
        val sourceFlow = appSettingsStore?.observeSnapshot() ?: flowOf(defaultSnapshot())
        return sourceFlow.map { local ->
            mergeWithRemote(local)
        }
    }

    suspend fun currentSnapshot(): FeatureSettingsSnapshot {
        migrationCoordinator.ensureMigrated()
        val local = appSettingsStore?.currentSnapshot() ?: defaultSnapshot()
        return mergeWithRemote(local)
    }

    private fun defaultSnapshot(): FeatureSettingsSnapshot {
        return FeatureSettingsSnapshot.defaults()
    }

    private fun mergeWithRemote(local: FeatureSettingsSnapshot): FeatureSettingsSnapshot {
        val remote = remotePreferenceStore?.snapshot() ?: return local
        val mergedEnabled = local.enabledFeatures.toMutableMap()
        remote.booleans.forEach { (key, value) ->
            when (key) {
                "feature.settings.enabled" -> mergedEnabled["settings"] = value
                "feature.video.enabled" -> mergedEnabled["video"] = value
                "feature.comment.enabled" -> mergedEnabled["comment"] = value
                "feature.ward.enabled" -> mergedEnabled["ward"] = value
                "feature.backup.enabled" -> mergedEnabled["backup"] = value
                "feature.chat.enabled" -> mergedEnabled["chat"] = value
            }
        }
        return local.copy(enabledFeatures = mergedEnabled)
    }
}
