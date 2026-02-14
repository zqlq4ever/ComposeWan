package com.zqlq.common.data.request

import kotlinx.serialization.Serializable

/**
 * 登录和注册请求
 * @property username 用户名
 * @property password 密码
 *
 * JSON示例：
 * {
 *     "username": "test",
 *     "password": "123456"
 * }
 */
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)
