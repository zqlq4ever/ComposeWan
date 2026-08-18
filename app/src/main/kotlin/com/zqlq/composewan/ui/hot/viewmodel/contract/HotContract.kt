package com.zqlq.composewan.ui.hot.viewmodel.contract

import com.zqlq.composewan.data.model.HotKeyItem
import com.zqlq.composewan.data.model.WebsiteItem

/**
 * 热门页面 UI 状态
 */
data class HotUiState(
    val hotKeys: List<HotKeyItem> = emptyList(),
    val websites: List<WebsiteItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * 热门页面用户意图
 */
sealed interface HotIntent {
    data object LoadData : HotIntent

    data class HotKeyClick(
        val name: String,
    ) : HotIntent

    data class WebsiteClick(
        val url: String,
    ) : HotIntent
}

/**
 * 热门页面一次性事件
 */
sealed interface HotEvent {
    data class ShowToast(
        val message: String,
    ) : HotEvent

    data class NavigateToWebView(
        val url: String,
    ) : HotEvent

    data class NavigateToSearch(
        val keyword: String,
    ) : HotEvent
}
