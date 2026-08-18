package com.zqlq.composewan.ui.hot.usecase

import com.zqlq.composewan.data.model.HotKeyItem
import com.zqlq.composewan.data.model.WebsiteItem

/**
 * 热门页用例。本阶段返回 mock。
 */
class HotUseCase {
    fun loadHotKeys(): List<HotKeyItem> =
        listOf(
            HotKeyItem(1, "Kotlin"),
            HotKeyItem(2, "Java"),
            HotKeyItem(3, "Android"),
            HotKeyItem(4, "Flutter"),
            HotKeyItem(5, "Compose"),
            HotKeyItem(6, "Jetpack"),
            HotKeyItem(7, "MVVM"),
            HotKeyItem(8, "MVI"),
            HotKeyItem(9, "Retrofit"),
            HotKeyItem(10, "OkHttp"),
            HotKeyItem(11, "Room"),
            HotKeyItem(12, "Hilt"),
            HotKeyItem(13, "Coroutines"),
            HotKeyItem(14, "Flow"),
            HotKeyItem(15, "LiveData"),
        )

    fun loadWebsites(): List<WebsiteItem> =
        listOf(
            WebsiteItem(1, "玩Android", "https://www.wanandroid.com"),
            WebsiteItem(2, "掘金", "https://juejin.cn"),
            WebsiteItem(3, "CSDN", "https://www.csdn.net"),
            WebsiteItem(4, "GitHub", "https://github.com"),
            WebsiteItem(5, "Stack Overflow", "https://stackoverflow.com"),
            WebsiteItem(6, "Google Developers", "https://developers.google.com"),
            WebsiteItem(7, "Android官方", "https://developer.android.com"),
            WebsiteItem(8, "Kotlin官方", "https://kotlinlang.org"),
            WebsiteItem(9, "简书", "https://www.jianshu.com"),
        )
}
