package com.zqlq.composewan.state.login

/**
 * 注册页面意图
 */
sealed interface RegisterIntent {
    /** 更新用户名 */
    data class UpdateUsername(val username: String) : RegisterIntent
    /** 更新密码 */
    data class UpdatePassword(val password: String) : RegisterIntent
    /** 更新确认密码 */
    data class UpdateConfirmPassword(val confirmPassword: String) : RegisterIntent
    /** 注册按钮点击 */
    data object RegisterClick : RegisterIntent
}
