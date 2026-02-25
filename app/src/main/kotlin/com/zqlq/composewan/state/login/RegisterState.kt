package com.zqlq.composewan.state.login

/**
 * 注册页面状态
 *
 * @param username 用户名
 * @param password 密码
 * @param confirmPassword 确认密码
 * @param isLoading 是否正在加载
 * @param errorMessage 错误信息
 */
data class RegisterState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
