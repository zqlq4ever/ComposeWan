package com.zqlq.composewan.state.login

/**
 * 登录页面意图
 */
sealed interface LoginIntent {
    /** 更新用户名 */
    data class UpdateUsername(val username: String) : LoginIntent
    /** 更新密码 */
    data class UpdatePassword(val password: String) : LoginIntent
    /** 登录按钮点击 */
    data object LoginClick : LoginIntent
    /** 注册按钮点击 */
    data object RegisterClick : LoginIntent
}
