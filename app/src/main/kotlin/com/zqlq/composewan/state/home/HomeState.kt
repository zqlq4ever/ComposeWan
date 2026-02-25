package com.zqlq.composewan.state.home

import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.BannerItem

/**
 * 首页 UI 状态
 *
 * @param banners Banner 列表
 * @param articles 文章列表
 * @param isLoading 是否加载更多中
 * @param isRefreshing 是否下拉刷新中
 * @param hasMore 是否还有更多数据
 * @param error 错误信息
 */
data class HomeState(
    val banners: List<BannerItem> = emptyList(),
    val articles: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val hasMore: Boolean = true,
    val error: String? = null
)
