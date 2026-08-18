package com.zqlq.composewan.ui.home.usecase

import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.BannerItem

/**
 * 首页用例。本阶段返回 mock，后续可替换为 Repository。
 */
class HomeUseCase {
    fun loadBanners(): List<BannerItem> =
        listOf(
            BannerItem(1, "Banner 1", "https://bing.biturl.top/?resolution=1920&format=image&index=1", "https://www.wanandroid.com"),
            BannerItem(2, "Banner 2", "https://bing.biturl.top/?resolution=1920&format=image&index=2", "https://www.wanandroid.com"),
            BannerItem(3, "Banner 3", "https://bing.biturl.top/?resolution=1920&format=image&index=3", "https://www.wanandroid.com"),
            BannerItem(4, "Banner 4", "https://bing.biturl.top/?resolution=1920&format=image&index=4", "https://www.wanandroid.com"),
            BannerItem(5, "Banner 5", "https://bing.biturl.top/?resolution=1920&format=image&index=5", "https://www.wanandroid.com"),
        )

    fun loadArticles(idOffset: Int = 0): List<ArticleItem> =
        listOf(
            ArticleItem(1 + idOffset, "Android Jetpack Compose 入门教程", "张三", "2024-01-01", "https://www.wanandroid.com", "Android", false),
            ArticleItem(2 + idOffset, "Kotlin 协程实战指南", "李四", "2024-01-02", "https://www.wanandroid.com", "Kotlin", true),
            ArticleItem(3 + idOffset, "MVVM 架构设计模式详解", "王五", "2024-01-03", "https://www.wanandroid.com", "架构", false),
            ArticleItem(4 + idOffset, "Retrofit + OkHttp 网络请求最佳实践", "赵六", "2024-01-04", "https://www.wanandroid.com", "网络", false),
            ArticleItem(5 + idOffset, "Room 数据库使用详解", "钱七", "2024-01-05", "https://www.wanandroid.com", "数据库", true),
            ArticleItem(6 + idOffset, "Hilt 依赖注入入门", "孙八", "2024-01-06", "https://www.wanandroid.com", "依赖注入", false),
            ArticleItem(7 + idOffset, "Compose 动画效果实现", "周九", "2024-01-07", "https://www.wanandroid.com", "动画", false),
            ArticleItem(8 + idOffset, "Material Design 3 组件使用", "吴十", "2024-01-08", "https://www.wanandroid.com", "UI", false),
        )
}
