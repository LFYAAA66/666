package com.example.douplus.data.config.model

object SettingKeys {
    const val FEATURE_SETTINGS = "feature.settings.enabled"
    const val FEATURE_VIDEO = "feature.video.enabled"
    const val FEATURE_COMMENT = "feature.comment.enabled"
    const val FEATURE_WARD = "feature.ward.enabled"
    const val FEATURE_BACKUP = "feature.backup.enabled"
    const val FEATURE_CHAT = "feature.chat.enabled"
    const val COMMENT_TIME_FORMAT = "comment.time.format"
    const val VIDEO_SHOW_TIME = "video.show.time"
    const val VIDEO_SHOW_LOCATION = "video.show.location"
    const val VIDEO_TEXT_SIZE = "video.text.size"
    const val VIDEO_TEXT_COLOR = "video.text.color"
    const val VIDEO_COLORFUL = "video.colorful"
    const val COMMENT_COPY_REMOVE_AT = "comment.copy.remove.at"
    const val COMMENT_PAUSE_VIDEO = "comment.pause.video"
    const val COMMENT_SHOW_TIME = "comment.show.time"
    const val REMOTE_RULES = "remote.rules.enabled"
    const val BACKUP_AUTO = "backup.auto.enabled"
    const val WEBDAV_URL = "webdav.url"
    const val WEBDAV_USER = "webdav.user"
    const val WEBDAV_DIR = "webdav.dir"
}

data class RemotePreferenceSnapshot(
    val booleans: Map<String, Boolean> = emptyMap(),
    val strings: Map<String, String> = emptyMap(),
    val ints: Map<String, Int> = emptyMap(),
    val source: String = "fallback",
)

data class FeatureSettingsSnapshot(
    val enabledFeatures: Map<String, Boolean> = emptyMap(),
    val commentTimeFormat: String = "yyyy-MM-dd HH:mm:ss",
    val remoteRuleUpdateEnabled: Boolean = true,
    val videoShowPublishTime: Boolean = true,
    val videoShowPublishLocation: Boolean = true,
    val videoTextSizeSp: Int = 14,
    val videoTextColorHex: String = "#FFFFFF",
    val videoColorfulDisplay: Boolean = false,
    val commentCopyRemoveAt: Boolean = true,
    val commentPauseVideo: Boolean = true,
    val backupAutoEnabled: Boolean = false,
    val webDavBaseUrl: String? = null,
    val webDavUserName: String? = null,
    val webDavRemoteDir: String? = null,
) {
    companion object {
        fun defaults(): FeatureSettingsSnapshot = FeatureSettingsSnapshot(
            enabledFeatures = mapOf(
                "global" to true,
                "settings" to true,
                "video" to true,
                "comment" to true,
                "ward" to true,
                "backup" to true,
                "chat" to true,
            )
        )
    }
}
