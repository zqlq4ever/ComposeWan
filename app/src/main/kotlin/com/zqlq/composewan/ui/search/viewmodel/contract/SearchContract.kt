package com.zqlq.composewan.ui.search.viewmodel.contract

import com.zqlq.composewan.data.model.ArticleItem

/**
 * 搜索 UI 状态
 */
data class SearchUiState(
    val searchQuery: String = "",
    val searchResults: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * 搜索用户意图
 */
sealed interface SearchIntent {
    data class UpdateQuery(
        val query: String,
    ) : SearchIntent

    data object Search : SearchIntent

    data object Clear : SearchIntent

    data class ArticleClick(
        val url: String,
    ) : SearchIntent
}

/**
 * 搜索一次性事件
 */
sealed interface SearchEvent {
    data class NavigateToWebView(
        val url: String,
    ) : SearchEvent
}
