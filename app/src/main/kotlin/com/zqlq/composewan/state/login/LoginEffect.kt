package com.zqlq.composewan.state.login

/**
 * 登录页面副作用
 */
sealed interface LoginEffect {
    /** 导航到注册页面 */
    data object NavigateToRegister : LoginEffect
    /** 登录成功 */
    data object LoginSuccess : LoginEffect
    /** 显示消息 */
    data class ShowMessage(val message: String) : LoginEffect
}
