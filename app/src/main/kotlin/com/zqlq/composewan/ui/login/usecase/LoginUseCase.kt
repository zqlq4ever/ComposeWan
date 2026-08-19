package com.zqlq.composewan.ui.login.usecase

import com.zqlq.common.utils.session.UserSession
import com.zqlq.network.WanRepository

/** 登录/注册用例。 */
class LoginUseCase(
    private val repository: WanRepository,
) {
    suspend fun login(
        username: String,
        password: String,
    ): Result<Unit> =
        runCatching {
            if (username.isBlank() || password.isBlank()) {
                error("empty")
            }
            val user = repository.login(username, password)
            UserSession.saveLogin(user.nickname?.takeIf { it.isNotBlank() } ?: user.username.orEmpty())
        }

    suspend fun register(
        username: String,
        password: String,
        repassword: String,
    ): Result<Unit> =
        runCatching {
            if (username.isBlank() || password.isBlank()) {
                error("empty")
            }
            repository.register(username, password, repassword)
        }
}
