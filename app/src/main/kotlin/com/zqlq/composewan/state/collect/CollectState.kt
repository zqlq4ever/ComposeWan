package com.zqlq.composewan.state.collect

import com.zqlq.composewan.data.model.ArticleItem

/**
 * 收藏页面状态
 *
 * @param articles 收藏文章列表
 * @param isLoading 是否正在加载
 * @param isRefreshing 是否正在刷新
 * @param isLoadingMore 是否正在加载更多
 * @param hasMore 是否有更多数据
 */
data class CollectState(
    val articles: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true
)
