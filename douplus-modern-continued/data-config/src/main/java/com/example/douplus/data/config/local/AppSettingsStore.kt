package com.example.douplus.data.config.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.douplus.data.config.model.FeatureSettingsSnapshot
import com.example.douplus.data.config.model.SettingKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "douplus_settings")

class AppSettingsStore(private val context: Context) {
    companion object {
        private val FEATURE_SETTINGS = booleanPreferencesKey(SettingKeys.FEATURE_SETTINGS)
        private val FEATURE_VIDEO = booleanPreferencesKey(SettingKeys.FEATURE_VIDEO)
        private val FEATURE_COMMENT = booleanPreferencesKey(SettingKeys.FEATURE_COMMENT)
        private val FEATURE_WARD = booleanPreferencesKey(SettingKeys.FEATURE_WARD)
        private val FEATURE_BACKUP = booleanPreferencesKey(SettingKeys.FEATURE_BACKUP)
        private val FEATURE_CHAT = booleanPreferencesKey(SettingKeys.FEATURE_CHAT)
        private val COMMENT_TIME_FORMAT = stringPreferencesKey(SettingKeys.COMMENT_TIME_FORMAT)
        private val VIDEO_SHOW_TIME = booleanPreferencesKey(SettingKeys.VIDEO_SHOW_TIME)
        private val VIDEO_SHOW_LOCATION = booleanPreferencesKey(SettingKeys.VIDEO_SHOW_LOCATION)
        private val VIDEO_TEXT_SIZE = intPreferencesKey(SettingKeys.VIDEO_TEXT_SIZE)
        private val VIDEO_TEXT_COLOR = stringPreferencesKey(SettingKeys.VIDEO_TEXT_COLOR)
        private val VIDEO_COLORFUL = booleanPreferencesKey(SettingKeys.VIDEO_COLORFUL)
        private val COMMENT_COPY_REMOVE_AT = booleanPreferencesKey(SettingKeys.COMMENT_COPY_REMOVE_AT)
        private val COMMENT_PAUSE_VIDEO = booleanPreferencesKey(SettingKeys.COMMENT_PAUSE_VIDEO)
        private val COMMENT_SHOW_TIME = booleanPreferencesKey(SettingKeys.COMMENT_SHOW_TIME)
        private val REMOTE_RULES = booleanPreferencesKey(SettingKeys.REMOTE_RULES)
        private val BACKUP_AUTO = booleanPreferencesKey(SettingKeys.BACKUP_AUTO)
        private val WEBDAV_URL = stringPreferencesKey(SettingKeys.WEBDAV_URL)
        private val WEBDAV_USER = stringPreferencesKey(SettingKeys.WEBDAV_USER)
        private val WEBDAV_DIR = stringPreferencesKey(SettingKeys.WEBDAV_DIR)
    }

    suspend fun updateFeature(feature: String, enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            when (feature) {
                "settings" -> prefs[FEATURE_SETTINGS] = enabled
                "video" -> prefs[FEATURE_VIDEO] = enabled
                "comment" -> prefs[FEATURE_COMMENT] = enabled
                "ward" -> prefs[FEATURE_WARD] = enabled
                "backup" -> prefs[FEATURE_BACKUP] = enabled
                "chat" -> prefs[FEATURE_CHAT] = enabled
            }
        }
    }

    suspend fun updateBooleanByUiKey(key: String, enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            when (key) {
                SettingKeys.VIDEO_SHOW_TIME -> prefs[VIDEO_SHOW_TIME] = enabled
                SettingKeys.VIDEO_SHOW_LOCATION -> prefs[VIDEO_SHOW_LOCATION] = enabled
                SettingKeys.REMOTE_RULES -> prefs[REMOTE_RULES] = enabled
                SettingKeys.COMMENT_COPY_REMOVE_AT -> prefs[COMMENT_COPY_REMOVE_AT] = enabled
                SettingKeys.COMMENT_PAUSE_VIDEO -> prefs[COMMENT_PAUSE_VIDEO] = enabled
                SettingKeys.COMMENT_SHOW_TIME -> prefs[COMMENT_SHOW_TIME] = enabled
                SettingKeys.BACKUP_AUTO -> prefs[BACKUP_AUTO] = enabled
            }
        }
    }

    fun currentEnabledForUiKey(snapshot: FeatureSettingsSnapshot, key: String, fallback: Boolean): Boolean {
        return when (key) {
            SettingKeys.VIDEO_SHOW_TIME -> snapshot.videoShowPublishTime
            SettingKeys.VIDEO_SHOW_LOCATION -> snapshot.videoShowPublishLocation
            SettingKeys.REMOTE_RULES -> snapshot.remoteRuleUpdateEnabled
            SettingKeys.COMMENT_COPY_REMOVE_AT -> snapshot.commentCopyRemoveAt
            SettingKeys.COMMENT_PAUSE_VIDEO -> snapshot.commentPauseVideo
            SettingKeys.COMMENT_SHOW_TIME -> true
            SettingKeys.BACKUP_AUTO -> snapshot.backupAutoEnabled
            else -> fallback
        }
    }

    suspend fun setCommentTimeFormat(pattern: String) {
        context.settingsDataStore.edit { it[COMMENT_TIME_FORMAT] = pattern }
    }

    suspend fun setVideoTextStyle(sizeSp: Int, colorHex: String, colorful: Boolean) {
        context.settingsDataStore.edit {
            it[VIDEO_TEXT_SIZE] = sizeSp
            it[VIDEO_TEXT_COLOR] = colorHex
            it[VIDEO_COLORFUL] = colorful
        }
    }

    suspend fun setWebDavConfig(baseUrl: String?, username: String?, remoteDir: String?) {
        context.settingsDataStore.edit {
            if (baseUrl != null) it[WEBDAV_URL] = baseUrl
            if (username != null) it[WEBDAV_USER] = username
            if (remoteDir != null) it[WEBDAV_DIR] = remoteDir
        }
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        context.settingsDataStore.edit { prefs -> prefs[booleanPreferencesKey(key)] = value }
    }

    suspend fun putString(key: String, value: String) {
        context.settingsDataStore.edit { prefs -> prefs[stringPreferencesKey(key)] = value }
    }

    suspend fun putInt(key: String, value: Int) {
        context.settingsDataStore.edit { prefs -> prefs[intPreferencesKey(key)] = value }
    }

    fun observeSnapshot(): Flow<FeatureSettingsSnapshot> {
        return context.settingsDataStore.data.map { prefs ->
            FeatureSettingsSnapshot(
                enabledFeatures = mapOf(
                    "settings" to (prefs[FEATURE_SETTINGS] ?: true),
                    "video" to (prefs[FEATURE_VIDEO] ?: true),
                    "comment" to (prefs[FEATURE_COMMENT] ?: true),
                    "ward" to (prefs[FEATURE_WARD] ?: true),
                    "backup" to (prefs[FEATURE_BACKUP] ?: true),
                    "chat" to (prefs[FEATURE_CHAT] ?: true),
                ),
                commentTimeFormat = prefs[COMMENT_TIME_FORMAT] ?: "yyyy-MM-dd HH:mm:ss",
                remoteRuleUpdateEnabled = prefs[REMOTE_RULES] ?: true,
                videoShowPublishTime = prefs[VIDEO_SHOW_TIME] ?: true,
                videoShowPublishLocation = prefs[VIDEO_SHOW_LOCATION] ?: true,
                videoTextSizeSp = prefs[VIDEO_TEXT_SIZE] ?: 14,
                videoTextColorHex = prefs[VIDEO_TEXT_COLOR] ?: "#FFFFFF",
                videoColorfulDisplay = prefs[VIDEO_COLORFUL] ?: false,
                commentCopyRemoveAt = prefs[COMMENT_COPY_REMOVE_AT] ?: true,
                commentPauseVideo = prefs[COMMENT_PAUSE_VIDEO] ?: true,
                backupAutoEnabled = prefs[BACKUP_AUTO] ?: false,
                webDavBaseUrl = prefs[WEBDAV_URL],
                webDavUserName = prefs[WEBDAV_USER],
                webDavRemoteDir = prefs[WEBDAV_DIR],
            )
        }
    }

    suspend fun currentSnapshot(): FeatureSettingsSnapshot = observeSnapshot().first()
}
