package com.zqlq.network

/** 玩 Android 接口路径（相对 [NetworkConfig.baseUrl]）。 */
object WanPaths {
    const val BASE_URL = "https://www.wanandroid.com/"

    const val BANNER = "banner/json"
    const val HOT_KEY = "hotkey/json"
    const val FRIEND = "friend/json"
    const val TREE = "tree/json"
    const val LOGIN = "user/login"
    const val REGISTER = "user/register"
    const val LOGOUT = "user/logout/json"

    fun articleList(page: Int): String = "article/list/$page/json"

    fun search(page: Int): String = "article/query/$page/json"

    fun collect(id: Int): String = "lg/collect/$id/json"

    fun uncollectOrigin(id: Int): String = "lg/uncollect_originId/$id/json"

    fun uncollect(id: Int): String = "lg/uncollect/$id/json"

    fun collectList(page: Int): String = "lg/collect/list/$page/json"
}
