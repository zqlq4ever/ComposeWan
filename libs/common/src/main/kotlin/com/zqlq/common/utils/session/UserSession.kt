package com.zqlq.common.utils.session

import com.zqlq.common.utils.storage.MMKVUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 登录用户名与登录状态本地缓存，Cookie 由网络层单独持久化。 */
object UserSession {
    const val KEY_USERNAME = "wan_username"
    const val KEY_LOGGED_IN = "wan_logged_in"

    private val _username = MutableStateFlow(readUsername())
    val usernameFlow: StateFlow<String> = _username.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(readLoggedIn())
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    /** 登录成功：写入用户名和登录标记。 */
    fun saveLogin(username: String) {
        MMKVUtils.putString(KEY_USERNAME, username)
        MMKVUtils.putBoolean(KEY_LOGGED_IN, true)
        _username.value = username
        _isLoggedIn.value = true
    }

    fun username(): String = _username.value

    fun isLoggedIn(): Boolean = _isLoggedIn.value

    fun clear() {
        MMKVUtils.remove(KEY_USERNAME)
        MMKVUtils.remove(KEY_LOGGED_IN)
        _username.value = ""
        _isLoggedIn.value = false
    }

    private fun readUsername(): String = MMKVUtils.getString(KEY_USERNAME, "")

    private fun readLoggedIn(): Boolean = MMKVUtils.getBoolean(KEY_LOGGED_IN, false)
}
