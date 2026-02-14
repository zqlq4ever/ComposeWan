package com.zqlq.composewan.state.home

import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.BannerItem

/**
 * 首页 UI 状态
 *
 * @param banners Banner 列表
 * @param articles 文章列表
 * @param isLoading 是否加载中
 * @param error 错误信息
 */
data class HomeState(
    val banners: List<BannerItem> = emptyList(),
    val articles: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
