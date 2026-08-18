package com.zqlq.composewan.ui.search.usecase

import com.zqlq.composewan.data.model.ArticleItem

/**
 * 搜索用例。本阶段返回 mock。
 */
class SearchUseCase {
    fun search(query: String): List<ArticleItem> {
        val allArticles =
            listOf(
                ArticleItem(1, "Android Jetpack Compose 入门教程", "张三", "2024-01-01", "https://www.wanandroid.com", "Android", false),
                ArticleItem(2, "Kotlin 协程实战指南", "李四", "2024-01-02", "https://www.wanandroid.com", "Kotlin", true),
                ArticleItem(3, "MVVM 架构设计模式详解", "王五", "2024-01-03", "https://www.wanandroid.com", "架构", false),
                ArticleItem(4, "Retrofit + OkHttp 网络请求最佳实践", "赵六", "2024-01-04", "https://www.wanandroid.com", "网络", false),
                ArticleItem(5, "Room 数据库使用详解", "钱七", "2024-01-05", "https://www.wanandroid.com", "数据库", true),
                ArticleItem(6, "Hilt 依赖注入入门", "孙八", "2024-01-06", "https://www.wanandroid.com", "依赖注入", false),
                ArticleItem(7, "Compose 动画效果实现", "周九", "2024-01-07", "https://www.wanandroid.com", "动画", false),
                ArticleItem(8, "Material Design 3 组件使用", "吴十", "2024-01-08", "https://www.wanandroid.com", "UI", false),
                ArticleItem(9, "Android 性能优化技巧", "郑一", "2024-01-09", "https://www.wanandroid.com", "性能", false),
                ArticleItem(10, "Kotlin Flow 响应式编程", "冯二", "2024-01-10", "https://www.wanandroid.com", "Kotlin", true),
            )
        return allArticles.filter {
            it.title.contains(query, ignoreCase = true) ||
                it.author.contains(query, ignoreCase = true) ||
                it.chapterName.contains(query, ignoreCase = true)
        }
    }
}
