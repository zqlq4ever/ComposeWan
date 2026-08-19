package com.zqlq.composewan.ui.settings.usecase

import com.zqlq.common.utils.session.UserSession
import com.zqlq.network.MmkvCookiesStorage
import com.zqlq.network.WanRepository

/** 设置页账号相关用例。 */
class SettingsUseCase(
    private val repository: WanRepository,
) {
    /** 退出登录：请求接口、清空会话与 Cookie。 */
    suspend fun logout(): Result<Unit> =
        runCatching {
            runCatching { repository.logout() }
            UserSession.clear()
            MmkvCookiesStorage.clearStored()
        }
}
