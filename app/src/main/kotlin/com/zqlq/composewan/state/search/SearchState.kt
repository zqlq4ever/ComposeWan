package com.zqlq.composewan.state.search

import com.zqlq.composewan.data.model.ArticleItem

/**
 * 搜索 UI 状态
 *
 * @param searchQuery 搜索关键词
 * @param searchResults 搜索结果列表
 * @param isLoading 是否加载中
 * @param error 错误信息
 */
data class SearchState(
    val searchQuery: String = "",
    val searchResults: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
