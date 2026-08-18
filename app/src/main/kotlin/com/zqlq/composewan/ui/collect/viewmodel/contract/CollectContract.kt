package com.zqlq.composewan.ui.collect.viewmodel.contract

import com.zqlq.composewan.data.model.ArticleItem

/**
 * 收藏页面状态
 */
data class CollectUiState(
    val articles: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
)

/**
 * 收藏页面意图
 */
sealed interface CollectIntent {
    data object LoadCollectList : CollectIntent

    data object RefreshCollectList : CollectIntent

    data object LoadMoreCollectList : CollectIntent

    data class OnArticleClick(
        val article: ArticleItem,
    ) : CollectIntent

    data class OnUncollectClick(
        val article: ArticleItem,
    ) : CollectIntent
}

/**
 * 收藏一次性事件
 */
sealed interface CollectEvent {
    data class NavigateToWebView(
        val url: String,
    ) : CollectEvent

    data class ShowMessageRes(
        val resId: Int,
    ) : CollectEvent
}
