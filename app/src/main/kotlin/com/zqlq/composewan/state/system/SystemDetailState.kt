package com.zqlq.composewan.state.system

import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.SystemChild

/**
 * 体系详情页面 UI 状态
 *
 * @param categoryName 分类名称
 * @param children 子分类列表
 * @param selectedChildId 当前选中的子分类ID
 * @param articles 文章列表
 * @param isLoading 是否加载中
 * @param error 错误信息
 */
data class SystemDetailState(
    val categoryName: String = "",
    val children: List<SystemChild> = emptyList(),
    val selectedChildId: Int = 0,
    val articles: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * 体系详情用户意图
 */
sealed interface SystemDetailIntent {
    /** 切换选中的子分类 */
    data class SelectChild(val childId: Int) : SystemDetailIntent
    
    /** 点击文章 */
    data class ArticleClick(val url: String) : SystemDetailIntent
    
    /** 收藏/取消收藏 */
    data class CollectClick(val articleId: Int, val isCollect: Boolean) : SystemDetailIntent
    
    /** 加载更多 */
    data object LoadMore : SystemDetailIntent
}

/**
 * 体系详情副作用（一次性事件）
 */
sealed interface SystemDetailEffect {
    /** 跳转 WebView */
    data class NavigateToWebView(val url: String) : SystemDetailEffect
}
