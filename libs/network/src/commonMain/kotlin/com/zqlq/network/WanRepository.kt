package com.zqlq.network

import com.zqlq.common.data.bean.BannerItem
import com.zqlq.common.data.bean.HotKeyItem
import com.zqlq.common.data.bean.SystemCategory
import com.zqlq.common.data.bean.UserInfo
import com.zqlq.common.data.bean.WebsiteItem
import com.zqlq.common.data.response.ArticleListResponse
import kotlinx.serialization.json.JsonElement

/** 玩 Android API 仓储。 */
class WanRepository(
    private val network: NetworkManager = NetworkManager.getInstance(),
) {
    suspend fun getBanners(): List<BannerItem> = network.get(WanPaths.BANNER)

    suspend fun getHomeArticles(page: Int): ArticleListResponse =
        network.get(
            WanPaths.articleList(page),
            query = mapOf("page_size" to 10),
        )

    suspend fun getHotKeys(): List<HotKeyItem> = network.get(WanPaths.HOT_KEY)

    suspend fun getWebsites(): List<WebsiteItem> = network.get(WanPaths.FRIEND)

    suspend fun getSystemTree(): List<SystemCategory> = network.get(WanPaths.TREE)

    suspend fun getArticlesByCid(
        cid: Int,
        page: Int,
    ): ArticleListResponse =
        network.get(
            WanPaths.articleList(page),
            query = mapOf("cid" to cid),
        )

    suspend fun searchArticles(
        keyword: String,
        page: Int = 0,
    ): ArticleListResponse =
        network.postFormData(
            path = WanPaths.search(page),
            form = mapOf("k" to keyword),
        )

    suspend fun login(
        username: String,
        password: String,
    ): UserInfo =
        network.postFormData(
            path = WanPaths.LOGIN,
            form =
                mapOf(
                    "username" to username,
                    "password" to password,
                ),
        )

    suspend fun register(
        username: String,
        password: String,
        repassword: String,
    ): UserInfo =
        network.postFormData(
            path = WanPaths.REGISTER,
            form =
                mapOf(
                    "username" to username,
                    "password" to password,
                    "repassword" to repassword,
                ),
        )

    suspend fun logout() {
        network.getAllowEmpty<JsonElement>(WanPaths.LOGOUT)
    }

    suspend fun collect(id: Int) {
        network.postForm(WanPaths.collect(id))
    }

    suspend fun uncollectOrigin(id: Int) {
        network.postForm(WanPaths.uncollectOrigin(id))
    }

    suspend fun uncollect(
        id: Int,
        originId: Int = -1,
    ) {
        network.postForm(
            path = WanPaths.uncollect(id),
            form = mapOf("originId" to originId.toString()),
        )
    }

    suspend fun getCollectArticles(page: Int): ArticleListResponse = network.get(WanPaths.collectList(page))
}
