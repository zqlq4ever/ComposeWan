package com.zqlq.composewan.state.login

/**
 * 注册页面副作用
 */
sealed interface RegisterEffect {
    /** 注册成功 */
    data object RegisterSuccess : RegisterEffect
    /** 显示消息 */
    data class ShowMessage(val message: String) : RegisterEffect
}
