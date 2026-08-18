package com.zqlq.composewan.ui.login.usecase

/**
 * 登录/注册用例。本阶段仅模拟成功。
 */
class LoginUseCase {
    suspend fun login(
        username: String,
        password: String,
    ): Result<Unit> {
        if (username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("empty"))
        }
        return Result.success(Unit)
    }

    suspend fun register(
        username: String,
        password: String,
    ): Result<Unit> {
        if (username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("empty"))
        }
        return Result.success(Unit)
    }
}
