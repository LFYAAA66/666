package com.example.douplus.data.config.repository

import java.util.concurrent.atomic.AtomicBoolean

/**
 * 配置迁移入口（最小实现）。
 *
 * 当前仅保留结构和幂等保证，后续再按版本补充真实迁移步骤。
 */
class MigrationCoordinator {
    private val migrated = AtomicBoolean(false)

    suspend fun ensureMigrated() {
        if (migrated.compareAndSet(false, true)) {
            // TODO(待补真实迁移): 按版本迁移旧 key/旧存储。
        }
    }
}
