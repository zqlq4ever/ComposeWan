package com.zqlq.composewan.ui.system.viewmodel.contract

import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.SystemCategory
import com.zqlq.composewan.data.model.SystemChild

/**
 * 体系页面 UI 状态
 */
data class SystemUiState(
    val categories: List<SystemCategory> = emptyList(),
    val expandedIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * 体系页面用户意图
 */
sealed interface SystemIntent {
    data object LoadData : SystemIntent

    data class ToggleExpand(
        val categoryId: Int,
    ) : SystemIntent

    data class ChildClick(
        val categoryName: String,
        val children: List<SystemChild>,
    ) : SystemIntent
}

/**
 * 体系页面一次性事件
 */
sealed interface SystemEvent {
    data class ShowToast(
        val message: String,
    ) : SystemEvent

    data class NavigateToSystemDetail(
        val categoryName: String,
        val children: List<SystemChild>,
    ) : SystemEvent
}

/**
 * 体系详情页面 UI 状态
 */
data class SystemDetailUiState(
    val categoryName: String = "",
    val children: List<SystemChild> = emptyList(),
    val selectedChildId: Int = 0,
    val articles: List<ArticleItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * 体系详情用户意图
 */
sealed interface SystemDetailIntent {
    data class SelectChild(
        val childId: Int,
    ) : SystemDetailIntent

    data class ArticleClick(
        val url: String,
    ) : SystemDetailIntent

    data class CollectClick(
        val articleId: Int,
        val isCollect: Boolean,
    ) : SystemDetailIntent

    data object LoadMore : SystemDetailIntent
}

/**
 * 体系详情一次性事件
 */
sealed interface SystemDetailEvent {
    data class NavigateToWebView(
        val url: String,
    ) : SystemDetailEvent
}
