package com.zqlq.network

/**
 * 玩 Android 业务异常。
 *
 * @param errorCode 0 成功；-1001 未登录；其他为错误。
 */
class ApiException(
    val errorCode: Int,
    override val message: String,
) : Exception(message) {
    val isNotLoggedIn: Boolean get() = errorCode == NOT_LOGGED_IN

    companion object {
        const val NOT_LOGGED_IN = -1001
    }
}
