package com.zqlq.composewan.ui.collect.usecase

import com.zqlq.composewan.data.model.ArticleItem

/**
 * 收藏页用例。本阶段返回 mock。
 */
class CollectUseCase {
    fun loadCollectArticles(): List<ArticleItem> =
        listOf(
            ArticleItem(
                1,
                "Android Jetpack Compose 学习指南",
                "Compose开发者",
                "2024-01-15",
                "https://example.com/compose-guide",
                "Compose",
                true,
            ),
            ArticleItem(2, "Kotlin 协程深度解析", "Kotlin专家", "2024-01-12", "https://example.com/coroutines", "Kotlin", true),
            ArticleItem(3, "MVI 架构设计模式详解", "架构师", "2024-01-10", "https://example.com/mvi", "架构", true),
            ArticleItem(4, "Android 性能优化实战", "性能专家", "2024-01-08", "https://example.com/performance", "优化", true),
            ArticleItem(5, "Jetpack Room 数据库最佳实践", "数据库专家", "2024-01-05", "https://example.com/room", "Jetpack", true),
        )
}
