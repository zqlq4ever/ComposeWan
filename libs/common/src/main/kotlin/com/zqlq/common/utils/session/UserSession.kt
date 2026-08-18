package com.zqlq.common.utils.session

import com.zqlq.common.utils.storage.MMKVUtils

/** 登录用户名本地缓存，Cookie 由网络层单独持久化。 */
object UserSession {
    const val KEY_USERNAME = "wan_username"

    fun saveUsername(username: String) {
        MMKVUtils.putString(KEY_USERNAME, username)
    }

    fun username(): String = MMKVUtils.getString(KEY_USERNAME, "")

    fun isLoggedIn(): Boolean = username().isNotBlank()

    fun clear() {
        MMKVUtils.remove(KEY_USERNAME)
    }
}
