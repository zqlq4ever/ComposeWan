package com.zqlq.composewan.ui.home.viewmodel.contract

import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.BannerItem

/**
 * 首页 UI 状态
 */
data class HomeUiState(
    val banners: List<BannerItem> = emptyList(),
    val articles: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasMore: Boolean = true,
    val error: String? = null,
)

/**
 * 首页用户意图
 */
sealed interface HomeIntent {
    data object LoadData : HomeIntent

    data object Refresh : HomeIntent

    data object LoadMore : HomeIntent

    data class BannerClick(
        val url: String,
    ) : HomeIntent

    data class ArticleClick(
        val url: String,
    ) : HomeIntent

    data object SearchClick : HomeIntent

    data class CollectClick(
        val articleId: Int,
        val isCollect: Boolean,
    ) : HomeIntent
}

/**
 * 首页一次性事件
 */
sealed interface HomeEvent {
    data class NavigateToWebView(
        val url: String,
    ) : HomeEvent

    data object NavigateToSearch : HomeEvent
}
