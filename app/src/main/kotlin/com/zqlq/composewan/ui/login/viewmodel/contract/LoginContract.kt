package com.zqlq.composewan.ui.login.viewmodel.contract

/**
 * 登录页面状态
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * 登录页面意图
 */
sealed interface LoginIntent {
    data class UpdateUsername(
        val username: String,
    ) : LoginIntent

    data class UpdatePassword(
        val password: String,
    ) : LoginIntent

    data object LoginClick : LoginIntent

    data object RegisterClick : LoginIntent
}

/**
 * 登录一次性事件
 */
sealed interface LoginEvent {
    data object NavigateToRegister : LoginEvent

    data object LoginSuccess : LoginEvent

    data class ShowMessageRes(
        val resId: Int,
    ) : LoginEvent

    data class ShowMessage(
        val message: String,
    ) : LoginEvent
}

/**
 * 注册页面状态
 */
data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * 注册页面意图
 */
sealed interface RegisterIntent {
    data class UpdateUsername(
        val username: String,
    ) : RegisterIntent

    data class UpdatePassword(
        val password: String,
    ) : RegisterIntent

    data class UpdateConfirmPassword(
        val confirmPassword: String,
    ) : RegisterIntent

    data object RegisterClick : RegisterIntent
}

/**
 * 注册一次性事件
 */
sealed interface RegisterEvent {
    data object RegisterSuccess : RegisterEvent

    data class ShowMessageRes(
        val resId: Int,
    ) : RegisterEvent

    data class ShowMessage(
        val message: String,
    ) : RegisterEvent
}
